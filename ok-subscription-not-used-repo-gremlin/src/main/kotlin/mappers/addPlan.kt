package mappers

import CommonGremlinConst.FIELD_ID
import CommonGremlinConst.FIELD_LOCK
import CommonGremlinConst.RESULT_SUCCESS
import PlanGremlinConst.FIELD_CONDITIONS
import PlanGremlinConst.FIELD_DURATION
import PlanGremlinConst.FIELD_PRICE
import PlanGremlinConst.FIELD_TITLE
import PlanGremlinConst.FIELD_VISIBILITY
import exceptions.WrongEnumException
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as gr
import models.plan.Plan
import models.plan.PlanId
import models.plan.PlanLock
import models.plan.SbscrPlanVisibility
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty

fun GraphTraversal<Vertex, Vertex>.addPlan(plan: Plan): GraphTraversal<Vertex, Vertex> =
    this
        .property(VertexProperty.Cardinality.single, FIELD_TITLE, plan.title.takeIf { it.isNotBlank() })
        .property(VertexProperty.Cardinality.set, FIELD_CONDITIONS, plan.conditions.takeIf { it.isNotEmpty() })
        .property(VertexProperty.Cardinality.single, FIELD_DURATION, plan.duration)
        .property(VertexProperty.Cardinality.single, FIELD_PRICE, plan.price)
        .property(VertexProperty.Cardinality.single, FIELD_LOCK, plan.lock.takeIf { it != PlanLock.NONE }?.asString())
        .property(VertexProperty.Cardinality.single, FIELD_VISIBILITY, plan.visibility.takeIf { it != SbscrPlanVisibility.NONE }?.name)

fun GraphTraversal<Vertex, Vertex>.listPlan(result: String = RESULT_SUCCESS) : GraphTraversal<Vertex, MutableMap<String, Any>> =
    project<Any?>(
        FIELD_ID,
        FIELD_TITLE,
        FIELD_CONDITIONS,
        FIELD_DURATION,
        FIELD_PRICE,
        FIELD_LOCK,
        FIELD_VISIBILITY
    )
        .by(gr.id<Vertex>())
        .by(FIELD_TITLE)
        .by(FIELD_CONDITIONS)
        .by(FIELD_DURATION)
        .by(FIELD_PRICE)
        .by(FIELD_LOCK)
        .by(FIELD_VISIBILITY)
        .by(gr.constant(result))

fun Map<String, Any?>.toPlan(): Plan = Plan(
    id = (this[FIELD_ID] as? String)?.let { PlanId(it) } ?: PlanId.NONE,
    title = (this[FIELD_TITLE] as? String) ?: "",
    conditions = (this[FIELD_CONDITIONS] as? Set<String>)?.toMutableSet() ?: mutableSetOf<String>(),
    duration = (this[FIELD_DURATION] as? Int) ?: 0,
    price = (this[FIELD_PRICE] as? String) ?: "0",
    lock = (this[FIELD_LOCK] as? String)?.let { PlanLock(it) } ?: PlanLock.NONE,
    visibility = when ( val value = this[FIELD_VISIBILITY]) {
        SbscrPlanVisibility.PUBLIC.name -> SbscrPlanVisibility.PUBLIC
        SbscrPlanVisibility.ADMIN_ONLY.name -> SbscrPlanVisibility.ADMIN_ONLY
        null -> SbscrPlanVisibility.NONE
        else -> throw WrongEnumException(
            "Cannot convert object from DB. " +
                    "visibility = '$value' cannot be converted to ${SbscrPlanVisibility::class}"
        )
    }

)