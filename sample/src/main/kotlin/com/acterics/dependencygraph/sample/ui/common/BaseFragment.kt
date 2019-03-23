package com.acterics.dependencygraph.sample.ui.common

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acterics.dependencygraph.sample.SampleApplication
import com.acterics.dependencygraph.sample.common.debugPrint
import com.acterics.dependencygraph.sample.common.objectScopeName
import com.arellomobile.mvp.MvpAppCompatFragment
import toothpick.Scope
import toothpick.Toothpick
import java.lang.RuntimeException

abstract class BaseFragment: MvpAppCompatFragment() {

    abstract var layoutRes: Int

    private var instanceStateSaved: Boolean = false
    private var viewHandler = Handler()

    protected open val parentScopeName: String by lazy {
        (parentFragment as? BaseFragment)?.fragmentScopeName
                ?: throw RuntimeException("Must be parent fragment")
    }

    protected open val scopeModuleInstaller: (Scope) -> Unit = {}

    private lateinit var fragmentScopeName: String
    protected lateinit var scope: Scope
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        val savedAppCode = savedInstanceState?.getString(STATE_LAUNCH_FLAG)
        //False - if fragment was restored without new app process (for example: activity rotation)
        val isNewInAppProcess = savedAppCode != SampleApplication.appCode
        val scopeWasClosed = savedInstanceState?.getBoolean(STATE_SCOPE_WAS_CLOSED) ?: true

        val scopeIsNotInit = isNewInAppProcess || scopeWasClosed

        fragmentScopeName = savedInstanceState?.getString(STATE_SCOPE_NAME) ?: objectScopeName()
        scope = Toothpick.openScopes(parentScopeName, fragmentScopeName)
                .apply {
                    if (scopeIsNotInit) {
                        debugPrint("Init new UI scope: $fragmentScopeName with parent: $parentScopeName")
                        scopeModuleInstaller(this)
                    } else {
                        debugPrint("Get exist UI scope: $fragmentScopeName with parent: $parentScopeName")
                    }
                }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    protected fun postViewAction(action: () -> Unit) {
        viewHandler.post(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewHandler.removeCallbacksAndMessages(null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
        outState.putString(STATE_SCOPE_NAME, fragmentScopeName)
        outState.putString(STATE_LAUNCH_FLAG, SampleApplication.appCode)
        outState.putBoolean(STATE_SCOPE_WAS_CLOSED, needCloseScope())
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needCloseScope()) {
            debugPrint("Destroy UI scope: $fragmentScopeName")
            Toothpick.closeScope(scope.name)
        }
    }

    private fun isRealRemoving(): Boolean =
            (isRemoving && !instanceStateSaved) //because isRemoving == true for fragment in backstack on screen rotation
                    || ((parentFragment as? BaseFragment)?.isRealRemoving() ?: false)

    private fun needCloseScope(): Boolean {
        return when {
            activity?.isChangingConfigurations == true -> false
            activity?.isFinishing == true -> true
            else -> isRealRemoving()
        }
    }

    open fun onBackPressed(): Unit? { return null }

    companion object {
        private const val STATE_SCOPE_NAME = "state_scope_name"
        private const val STATE_LAUNCH_FLAG = "state_launch_flag"
        private const val STATE_SCOPE_WAS_CLOSED = "state_scope_was_closed"
    }
}