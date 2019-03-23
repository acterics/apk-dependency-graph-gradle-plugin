package com.acterics.dependencygraph.sample.common.navigation

import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen

class FlowRouter(private val globalRouter: Router) : Router() {

    fun startFlow(screen: SupportAppScreen) {
        globalRouter.navigateTo(screen)
    }

    fun newRootFlow(screen: SupportAppScreen) {
        globalRouter.newRootScreen(screen)
    }

    fun finishFlow() {
        globalRouter.exit()
    }
}