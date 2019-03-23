package com.acterics.dependencygraph.sample.ui.login

import android.os.Bundle
import com.acterics.dependencygraph.sample.Screens
import com.acterics.dependencygraph.sample.common.navigation.FlowRouter
import com.acterics.dependencygraph.sample.common.navigation.setLaunchScreen
import com.acterics.dependencygraph.sample.di.module.FlowNavigationModule
import com.acterics.dependencygraph.sample.ui.common.FlowFragment
import ru.terrakok.cicerone.Router
import toothpick.Scope
import toothpick.Toothpick
import javax.inject.Inject

class LoginFlowFragment: FlowFragment() {

    @Inject
    lateinit var router: Router

    override val scopeModuleInstaller: (Scope) -> Unit = { scope ->
        scope.installModules(
            FlowNavigationModule(scope.getInstance(FlowRouter::class.java))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
        if (childFragmentManager.fragments.isEmpty()) {
            navigator.setLaunchScreen(Screens.LoginScreen)
        }
    }

    override fun onExit() {
        router.exit()
    }


}