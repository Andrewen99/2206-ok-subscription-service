package exceptions

import models.AcqSbscrCommand

class UnknownAcqSbscrCommand(cmd: AcqSbscrCommand) : Throwable("Wrong command $cmd at mapping toTransport stage")