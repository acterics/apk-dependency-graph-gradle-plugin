package com.acterics.dependencygraph.sample.domain.executor

import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Oleg Lipskiy on 26.10.17
 */
class JobExecutor(numberOfThreads: Int, threadIdentifier: String): Executor {
    private val threadPoolExecutor = Executors.newFixedThreadPool(numberOfThreads,
            JobThreadFactory(threadIdentifier)) as ThreadPoolExecutor

    override fun execute(runnable: Runnable) = threadPoolExecutor.execute(runnable)

    private class JobThreadFactory(private val threadIdentifier: String) : ThreadFactory {
        private val counter = AtomicInteger()

        override fun newThread(runnable: Runnable?) =
                Thread(runnable, "thread:$threadIdentifier:${counter.incrementAndGet()}")
    }
}