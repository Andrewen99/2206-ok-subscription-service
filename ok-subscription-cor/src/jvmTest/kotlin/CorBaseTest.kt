package ru.otus.otuskotlin.subscription.cor

import CorChain
import CorWorker
import dsl.*
import kotlinx.coroutines.runBlocking

import kotlin.test.Test
import kotlin.test.assertEquals

class CorBaseTest {
    @Test
    fun `worker should execute handle`() = runBlocking {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockHandle = { history += "w1; " }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("w1; ", ctx.history)
    }

    @Test
    fun `worker should not execute when off`() = runBlocking {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockOn = { status == CorStatuses.ERROR },
            blockHandle = { history += "w1; " }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("", ctx.history)
    }

    @Test
    fun `worker should handle exception`() = runBlocking {
        val worker = CorWorker<TestContext>(
            title = "w1",
            blockHandle = { throw RuntimeException("some error") },
            blockExcept = { e -> history += e.message }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        assertEquals("some error", ctx.history)
    }

    @Test
    fun `chain should execute workers`() = runBlocking {
        val createWorker = { title: String ->
            CorWorker<TestContext>(
                title = title,
                blockOn = { status == CorStatuses.NONE },
                blockHandle = { history += "$title; " }
            )
        }
        val chain = CorChain<TestContext>(
            execs = listOf(createWorker("w1"), createWorker("w2")),
            title = "chain",
            handler = ::executeSequential
        )
        val ctx = TestContext()
        chain.exec(ctx)
        assertEquals("w1; w2; ", ctx.history)
    }

    private fun execute(dsl: CorExecDsl<TestContext>): TestContext = runBlocking {
        val ctx = TestContext()
        dsl.build().exec(ctx)
        ctx
    }

    @Test
    fun `handle should execute`() {
        assertEquals("w1; ", execute(rootChain {
            worker {
                handle { history += "w1; " }
            }
        }).history)
    }

    @Test
    fun `on should check condition`() {
        assertEquals("w2; w3; ", execute(rootChain {
            worker {
                on { status == CorStatuses.ERROR }
                handle { history += "w1; " }
            }
            worker {
                on { status == CorStatuses.NONE }
                handle {
                    history += "w2; "
                    status = CorStatuses.FAILING
                }
            }
            worker {
                on { status == CorStatuses.FAILING }
                handle { history += "w3; " }
            }
        }).history)
    }

    @Test
    fun `except should execute when exception`() {
        assertEquals("some error", execute(rootChain {
            worker {
                handle { throw RuntimeException("some error") }
                except { history += it.message }
            }
        }).history)
    }

    @Test
    fun `should throw when exception and no except`() {

            execute(rootChain {
                worker("throw") { throw RuntimeException("some error") }
            })

    }

    @Test
    fun `complex chain example`() = runBlocking {
        val chain = rootChain<TestContext> {
            worker {
                title = "Инициализация статуса"
                description = "При старте обработки цепочки, статус еще не установлен. Проверяем его"

                on { status == CorStatuses.NONE }
                handle { status = CorStatuses.RUNNING }
                except { status = CorStatuses.ERROR }
            }

            chain {
                on { status == CorStatuses.RUNNING }

                worker(
                    title = "Лямбда обработчик",
                    description = "Пример использования обработчика в виде лямбды"
                ) {
                    some += 4
                }
            }

            parallel {
                on {
                    some < 15
                }

                worker(title = "Increment some") {
                    some++
                }
            }

            printResult()

        }.build()

        val ctx = TestContext()
        chain.exec(ctx)
        println("Complete: $ctx")
    }

    @Test
    fun `multiple chains with blockOn`() = runBlocking {
        val x = 5;
        val chain = rootChain<TestContext> {
            chain {
                on { x == 2 }

                worker(title = "some is x * 2") {
                    some = x*2
                    historyList.add("x*2")
                }
            }

            chain {
                on { x == 4 }

                worker(title = "some is x * 4") {
                    some = x*4
                    historyList.add("x*4")
                }
            }

            chain {
                on { x == 5 }

                worker(title = "some is x * 5") {
                    some = x*5
                    historyList.add("x*5")
                }
            }

            chain {
                on { x == 7 }

                worker(title = "some is x * 7") {
                    some = x*7
                    historyList.add("x*7")
                }
            }
        }.build()
        val ctx = TestContext()
        chain.exec(ctx)
        assertEquals(1, ctx.historyList.size)
        assertEquals("x*5", ctx.historyList[0])
        assertEquals(25, ctx.some)
    }
}


private fun CorChainDsl<TestContext>.printResult() = worker(title = "Print example") {
    println("some = $some")
}

data class TestContext(
    var status: CorStatuses = CorStatuses.NONE,
    var some: Int = 0,
    var history: String = "",
    var historyList: MutableList<String> = mutableListOf()
)

enum class CorStatuses {
    NONE,
    RUNNING,
    FAILING,
    DONE,
    ERROR
}
