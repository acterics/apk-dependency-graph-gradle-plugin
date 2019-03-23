package com.acterics.dependencygraph.sample.di.module

import android.content.Context
import com.acterics.dependencygraph.sample.data.repository.AuthenticateRepositoryImpl
import com.acterics.dependencygraph.sample.domain.executor.ExecutionScheduler
import com.acterics.dependencygraph.sample.domain.interactor.LaunchInteractor
import com.acterics.dependencygraph.sample.domain.repository.AuthenticateRepository
import com.acterics.dependencygraph.sample.platform.ThreadScheduler
import com.acterics.dependencygraph.sample.presentation.AppLauncher
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module

class AppModule(context: Context): Module() {
    init {
        bind(Context::class.java).toInstance(context)

        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)

        bind(AppLauncher::class.java).singletonInScope()

        bind(ExecutionScheduler::class.java).to(ThreadScheduler::class.java).singletonInScope()
        bind(LaunchInteractor::class.java).singletonInScope()
        bind(AuthenticateRepository::class.java).to(AuthenticateRepositoryImpl::class.java).singletonInScope()
    }
}