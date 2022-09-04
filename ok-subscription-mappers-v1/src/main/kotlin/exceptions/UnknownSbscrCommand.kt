package exceptions

import models.plan.PlanCommand

class UnknownSbscrCommand(cmd: PlanCommand) : Throwable("Wrong command $cmd at mapping toTransport stage")