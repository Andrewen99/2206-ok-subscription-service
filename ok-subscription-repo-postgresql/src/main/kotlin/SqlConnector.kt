import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.DEFAULT_ISOLATION_LEVEL
import org.jetbrains.exposed.sql.transactions.transaction

class SqlConnector(
    private val url: String = "jdbc:postgresql://localhost:5432/sbscrdevdb",
    private val user: String = "postgres",
    private val password: String = "sbscr-pass",
    private val schema: String = "sbscr",
    private val databaseConfig: DatabaseConfig = DatabaseConfig { defaultIsolationLevel = DEFAULT_ISOLATION_LEVEL }
) {
    // Sample of describing different db drivers in case of multiple DB connections with different data bases
    private enum class DbType(val driver: String) {
        MYSQL("com.mysql.cj.jdbc.Driver"),
        POSTGRESQL("org.postgresql.Driver")
    }

    // Sample of different db types
    private val dbType: DbType = url.let { url ->
        when {
            url.startsWith("jdbc:mysql://") -> DbType.MYSQL
            url.startsWith("jdbc:postgresql://") -> DbType.POSTGRESQL
            else -> error("Cannot parse database type from url: $url. `jdbc:mysql://...` and `jdbc:postgresql://` are supported only.")
        }
    }

    // Global connection to PSQL
    private val globalConnection = Database.connect(url, dbType.driver, user, password, databaseConfig = databaseConfig)

    // Ensure creation of new connection with options to migrate/pre-drop database
    fun connect(
        vararg tables: Table,
        defaultDbAction: DefaultDbAction = DefaultDbAction.CREATE_MISSING_TABLES
    ): Database {
        // Create schema if such not exists
        transaction(globalConnection) {
            when (dbType) {
                DbType.MYSQL -> {
                    val dbSettings = " DEFAULT CHARACTER SET utf8mb4\n" +
                            " DEFAULT COLLATE utf8mb4_general_ci"
                    connection.prepareStatement("CREATE DATABASE IF NOT EXISTS $schema\n$dbSettings", false)
                        .executeUpdate()
                    connection.prepareStatement("ALTER DATABASE $schema\n$dbSettings", false).executeUpdate()
                }
                DbType.POSTGRESQL -> {
                    connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS $schema", false).executeUpdate()
                }
            }
        }

        // Create connection for all supported db types
        val connect = Database.connect(
            url, dbType.driver, user, password,
            databaseConfig = databaseConfig,
            setupConnection = { connection ->
                when (dbType) {
                    DbType.MYSQL -> {
                        connection.transactionIsolation = DEFAULT_ISOLATION_LEVEL
                        connection.schema = schema
                        connection.catalog = schema
                    }
                    DbType.POSTGRESQL -> {
                        connection.transactionIsolation = DEFAULT_ISOLATION_LEVEL
                        connection.schema = schema
                    }
                }
            }
        )

        // Update schema:
        //   - drop if needed (for ex, in tests);
        //   - exec migrations if needed;
        //   - otherwise unsure to create tables
        transaction(connect) {
            if (System.getenv("ok.mp.sql_drop_db")?.toBoolean() == true || defaultDbAction == DefaultDbAction.DROP_AND_RECREATE) {
                SchemaUtils.drop(*tables, inBatch = true)
                SchemaUtils.create(*tables, inBatch = true)
            } else if (System.getenv("ok.mp.sql_fast_migration").toBoolean() || defaultDbAction == DefaultDbAction.FAST_MIGRATION) {
                // TODO: Place to exec migration: create and ensure tables
            } else if (defaultDbAction == DefaultDbAction.CREATE_MISSING_TABLES) {
                try {
                    val nonExistingTables = tables.filter { !it.exists() }.toTypedArray()
                    SchemaUtils.createMissingTablesAndColumns(*nonExistingTables, inBatch = true)
                } catch (e: Exception) {
                    println("Error while creating missing Tables: " + e.stackTrace)
                }

            }
        }

        return connect
    }
    enum class DefaultDbAction {
        DROP_AND_RECREATE,
        FAST_MIGRATION,
        CREATE_MISSING_TABLES
    }
}
