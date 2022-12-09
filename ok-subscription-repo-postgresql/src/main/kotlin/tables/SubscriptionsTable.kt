package tables

import com.benasher44.uuid.uuid4
import models.SbscrUserId
import models.plan.PlanId
import models.subscription.SbscrPaymentStatus
import models.subscription.Subscription
import models.subscription.SubscriptionId
import models.subscription.SubscriptionLock
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.statements.InsertStatement
import util.MIN_LOCAL_DATE

object SubscriptionsTable : IdTable<String>("Subscriptions") {
    override val id: Column<EntityID<String>> = varchar("id", 50).uniqueIndex().entityId()
    val ownerId = varchar("ownerId", 50)
    val planId = reference("planId", PlansTable.id).index()
    val startDate = date("startDate").nullable()
    val endDate = date("endDate").nullable()
    val isActive = bool("isActive")
    val lock = varchar("lock", 50)
    val paymentStatus = enumeration("paymentStatus", SbscrPaymentStatus::class)

    override val primaryKey = PrimaryKey(id)

    fun fromTransport(res: InsertStatement<Number>) = Subscription(
        id = SubscriptionId(res[id].toString()),
        ownerId = SbscrUserId(res[ownerId]),
        planId = PlanId(res[planId].toString()),
        startDate = res[startDate] ?: MIN_LOCAL_DATE,
        endDate = res[endDate] ?: MIN_LOCAL_DATE,
        isActive = res[isActive],
        lock = SubscriptionLock(res[lock].toString()),
        paymentStatus = res[paymentStatus]
    )

    fun fromTransport(res: ResultRow) = Subscription(
        id = SubscriptionId(res[id].toString()),
        ownerId = SbscrUserId(res[ownerId]),
        planId = PlanId(res[planId].toString()),
        startDate = res[startDate] ?: MIN_LOCAL_DATE,
        endDate = res[endDate] ?: MIN_LOCAL_DATE,
        isActive = res[isActive],
        lock = SubscriptionLock(res[lock].toString()),
        paymentStatus = res[paymentStatus]
    )
}