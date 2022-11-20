package mappers

import jdk.jshell.JShell.Subscription
import models.plan.Plan

fun Plan.label(): String? = this::class.simpleName
fun Subscription.label(): String? = this::class.simpleName