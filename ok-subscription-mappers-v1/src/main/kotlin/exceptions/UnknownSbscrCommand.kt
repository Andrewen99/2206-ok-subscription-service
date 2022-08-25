package exceptions

import models.SbscrCommand

class UnknownSbscrCommand(cmd: SbscrCommand) : Throwable("Wrong command $cmd at mapping toTransport stage")