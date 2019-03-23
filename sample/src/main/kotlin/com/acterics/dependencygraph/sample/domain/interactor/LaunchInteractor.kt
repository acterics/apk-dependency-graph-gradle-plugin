package com.acterics.dependencygraph.sample.domain.interactor

import com.acterics.dependencygraph.sample.domain.executor.ExecutionScheduler
import com.acterics.dependencygraph.sample.domain.repository.AuthenticateRepository
import javax.inject.Inject

class LaunchInteractor
@Inject constructor(private val authenticateRepository: AuthenticateRepository,
                    private val executionScheduler: ExecutionScheduler) {

}