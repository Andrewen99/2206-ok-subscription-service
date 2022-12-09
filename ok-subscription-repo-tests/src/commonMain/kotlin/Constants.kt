import models.SbscrError

fun createNotFoundAssertionErrorText(errors: List<SbscrError>) = """Errors list should contain "not-found" error. Current errors: $errors"""
fun createConcurrencyAssertionErrorText(errors: List<SbscrError>) = """Errors list should contain "concurrency" error. Current errors: $errors"""