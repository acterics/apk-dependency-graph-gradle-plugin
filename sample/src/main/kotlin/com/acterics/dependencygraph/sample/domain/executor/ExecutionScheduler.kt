package com.acterics.dependencygraph.sample.domain.executor

import io.reactivex.*

/**
 * Created by Oleg Lipskiy on 26.10.17
 */
interface ExecutionScheduler {
    fun ui(): Scheduler
    fun highPriority(): Scheduler
    fun lowPriority(): Scheduler

    fun <T> highPrioritySingle(): (Single<T>) -> Single<T>
    fun <T> lowPrioritySingle(): (Single<T>) -> Single<T>

    fun <T> highPriorityObservable(): (Observable<T>) -> Observable<T>
    fun <T> lowPriorityObservable(): (Observable<T>) -> Observable<T>

    fun <T> highPriorityFlowable(): (Flowable<T>) -> Flowable<T>
    fun <T> lowPriorityFlowable(): (Flowable<T>) -> Flowable<T>

    fun highPriorityCompletable(): (Completable) -> Completable
    fun lowPriorityCompletable(): (Completable) -> Completable
}