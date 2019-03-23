package com.acterics.dependencygraph.sample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.acterics.dependencygraph.sample.R
import com.acterics.dependencygraph.sample.di.DI
import com.acterics.dependencygraph.sample.presentation.AppLauncher
import com.acterics.dependencygraph.sample.ui.common.BaseFragment
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import toothpick.Toothpick
import javax.inject.Inject

class MainActivity: AppCompatActivity() {

    @Inject lateinit var launcher: AppLauncher
    @Inject lateinit var navigatorHolder: NavigatorHolder

    private val currentFragment: BaseFragment?
        get() = supportFragmentManager.findFragmentById(R.id.rootHolder) as? BaseFragment

    private val navigator: Navigator =
            object: SupportAppNavigator(this, supportFragmentManager, R.id.rootHolder) {
                override fun setupFragmentTransaction(command: Command?,
                                                      currentFragment: Fragment?,
                                                      nextFragment: Fragment?,
                                                      fragmentTransaction: FragmentTransaction?) {
                    fragmentTransaction?.setReorderingAllowed(true)
                }
            }


    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (savedInstanceState == null) {
            launcher.launch()
        }

    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }

}