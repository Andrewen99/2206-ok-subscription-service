package models.plan

data class PlanFilter(
    var visibilitySet: Set<SbscrPlanVisibility> = setOf(SbscrPlanVisibility.PUBLIC)
)
