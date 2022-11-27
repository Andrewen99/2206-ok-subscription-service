import repos.RepoPlanSql
import repos.RepoSubscriptionSql
import tables.PlansTable
import tables.SubscriptionsTable
import tables.UsersTable

object PostgresRepoFactory {
    fun getTables(
        url: String = "jdbc:postgresql://localhost:5432/sbscr",
        user: String = "postgres",
        password: String = "sbscr-pass",
        schema: String = "sbscr",
    ): Pair<RepoPlanSql, RepoSubscriptionSql> {
        val db = SqlConnector(url, user, password, schema).connect(UsersTable, PlansTable, SubscriptionsTable)
        return Pair(RepoPlanSql(db), RepoSubscriptionSql(db))
    }
}