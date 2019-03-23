package com.acterics.dependencygraph.sample.presentation

import com.acterics.dependencygraph.sample.Screens
import com.acterics.dependencygraph.sample.domain.interactor.LaunchInteractor
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class AppLauncher
@Inject constructor(private val router: Router,
                    private val launchInteractor: LaunchInteractor) {

    fun launch() {
        router.newRootScreen(Screens.LoginFlowScreen)
    }

}