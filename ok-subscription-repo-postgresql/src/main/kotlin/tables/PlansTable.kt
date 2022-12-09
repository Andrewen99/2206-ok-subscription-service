package tables

import com.benasher44.uuid.uuid4
import models.plan.Plan
import models.plan.PlanId
import models.plan.PlanLock
import models.plan.SbscrPlanVisibility
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

object PlansTable: IdTable<String>("Plans") {
    override val id: Column<EntityID<String>> = varchar("id", 50).uniqueIndex().entityId()
    val title = varchar("title", 128)
    val conditions = varchar("conditions", 1024)
    val duration = integer("duration")
    val price = varchar("price", 50)
    val lock = varchar("lock", 128)
    val visibility = enumeration("visibility", SbscrPlanVisibility::class)

    override val primaryKey = PrimaryKey(id)

    fun fromTransport(res: InsertStatement<Number>) = Plan(
        id = PlanId(res[id].toString()),
        title = res[title],
        conditions = res[conditions].split("~~").toMutableSet(),
        duration = res[duration],
        price = res[price],
        lock = PlanLock(res[lock]),
        visibility = res[visibility]
    )

    fun fromTransport(res: ResultRow) = Plan(
        id = PlanId(res[id].toString()),
        title = res[title],
        conditions = res[conditions].split("~~").toMutableSet(),
        duration = res[duration],
        price = res[price],
        lock = PlanLock(res[lock]),
        visibility = res[visibility]
    )

}
