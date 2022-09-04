package exceptions

import models.subscription.SubscriptionCommand

class UnknownAcqSbscrCommand(cmd: SubscriptionCommand) : Throwable("Wrong command $cmd at mapping toTransport stage")