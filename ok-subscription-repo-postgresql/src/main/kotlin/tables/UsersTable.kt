package tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object UsersTable : IdTable<String>("Users") {
    override val id: Column<EntityID<String>> = varchar("id", 50).uniqueIndex().entityId()

    override val primaryKey = PrimaryKey(id)
}