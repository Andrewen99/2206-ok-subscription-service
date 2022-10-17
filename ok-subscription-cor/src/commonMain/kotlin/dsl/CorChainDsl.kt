package dsl

import CorChain
import ICorExec
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@CorDslMarker
class CorChainDsl<T>(
    title: String = "",
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(ex: Throwable) -> Unit = {},
    private var workers: MutableList<ICorExecDsl<T>> = mutableListOf(),
    private var handler: suspend (T, List<ICorExec<T>>) -> Unit = ::executeSequential
) : CorExecDsl<T>(title, description, blockOn, blockExcept) {

    override fun build(): ICorExec<T> {
        return CorChain(
            title = title,
            description = description,
            blockOn = blockOn,
            execs = workers.map { it.build() },
            handler = handler
        )
    }

    fun add(corExecDsl: CorExecDsl<T>) {
        workers.add(corExecDsl)
    }
}

/**
 * Создает рабочего
 */
fun <T> CorChainDsl<T>.worker(workerDsl: CorWorkerDsl<T>.() -> Unit) {
    add(CorWorkerDsl<T>().apply(workerDsl))
}

/**
 * Создает рабочего с on и except по умолчанию
 */
fun <T> CorChainDsl<T>.worker(title: String,
           description: String = "",
           function: suspend T.() -> Unit
) {
    add(CorWorkerDsl<T>(title, description).apply {
        handle(function)
    })
}

fun <T> CorChainDsl<T>.chain(chainDsl: CorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>().apply(chainDsl))
}

fun <T> CorChainDsl<T>.parallel(chainDsl: CorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>(handler = ::executeParallel).apply(chainDsl))
}

/**
 * Точка входа в dsl построения цепочек.
 * Элементы исполняются последовательно.
 *
 * Пример:
 * ```
 *  chain<SomeContext> {
 *      worker {
 *      }
 *      chain {
 *          worker(...) {
 *          }
 *          worker(...) {
 *          }
 *      }
 *      parallel {
 *         ...
 *      }
 *  }
 * ```
 */
fun <T> rootChain(chainDsl: CorChainDsl<T>.() -> Unit) : CorChainDsl<T> {
    return CorChainDsl<T>().apply(chainDsl)
}

/**
 * Стратегия последовательного исполнения
 */
suspend fun <T> executeSequential(context: T, execs: List<ICorExec<T>>): Unit =
    execs.forEach {
        it.exec(context)
    }

/**
 * Стратегия параллельного исполнения
 */
suspend fun <T> executeParallel(context: T, execs: List<ICorExec<T>>): Unit = coroutineScope {
    execs.forEach {
        launch { it.exec(context) }
    }
}