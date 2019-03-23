package com.acterics.dependencygraph.sample.platform

import com.acterics.dependencygraph.sample.domain.executor.ExecutionScheduler
import com.acterics.dependencygraph.sample.domain.executor.JobExecutor
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Oleg Lipskiy on 26.10.17
 */
class ThreadScheduler @Inject constructor(): ExecutionScheduler {
    companion object {
        internal const val HIGH_PRIORITY_THREADS = 10
        internal const val LOW_PRIORITY_THREADS = 2

        internal val highPriorityScheduler: Scheduler
        internal val lowPriorityScheduler: Scheduler
        init {
            highPriorityScheduler = Schedulers.from(JobExecutor(HIGH_PRIORITY_THREADS, "High-Priority-Pool"))
            lowPriorityScheduler = Schedulers.from(JobExecutor(LOW_PRIORITY_THREADS, "Low-Priority-Pool"))
        }
    }

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
    override fun highPriority(): Scheduler = highPriorityScheduler
    override fun lowPriority(): Scheduler = lowPriorityScheduler

    override fun <T> highPrioritySingle() = { upstream: Single<T> -> upstream.subscribeOn(highPriority()).observeOn(ui()) }
    override fun <T> lowPrioritySingle()= { upstream: Single<T> -> upstream.subscribeOn(lowPriority()).observeOn(ui()) }

    override fun <T> highPriorityObservable() = { upstream: Observable<T> -> upstream.subscribeOn(highPriority()).observeOn(ui()) }
    override fun <T> lowPriorityObservable() = { upstream: Observable<T> -> upstream.subscribeOn(lowPriority()).observeOn(ui()) }

    override fun <T> highPriorityFlowable() = { upstream: Flowable<T> -> upstream.subscribeOn(highPriority()).observeOn(ui()) }
    override fun <T> lowPriorityFlowable() = { upstream: Flowable<T> -> upstream.subscribeOn(lowPriority()).observeOn(ui()) }

    override fun highPriorityCompletable() = { upstream: Completable -> upstream.subscribeOn(highPriority()).observeOn(ui()) }
    override fun lowPriorityCompletable() = { upstream: Completable -> upstream.subscribeOn(lowPriority()).observeOn(ui()) }
}