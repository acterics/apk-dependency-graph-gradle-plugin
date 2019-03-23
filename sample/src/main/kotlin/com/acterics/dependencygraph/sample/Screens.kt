package com.acterics.dependencygraph.sample

import androidx.fragment.app.Fragment
import com.acterics.dependencygraph.sample.ui.login.LoginFlowFragment
import com.acterics.dependencygraph.sample.ui.login.LoginFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object LoginFlowScreen: SupportAppScreen() {
        override fun getFragment(): Fragment = LoginFlowFragment()
    }

    object LoginScreen: SupportAppScreen() {
        override fun getFragment(): Fragment = LoginFragment()
    }

}