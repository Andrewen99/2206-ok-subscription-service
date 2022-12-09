package repos

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.SQLException

fun <T> safeTransaction(db: Database, statement: Transaction.() -> T, handleException: Throwable.() -> T): T {
    return try {
        transaction(db, statement)
    } catch (e: SQLException) {
        throw e
    } catch (e: Throwable) {
        return handleException(e)
    }
}