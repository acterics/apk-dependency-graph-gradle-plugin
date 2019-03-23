package com.acterics.dependencygraph.sample.ui.login

import com.acterics.dependencygraph.sample.R
import com.acterics.dependencygraph.sample.presentation.login.LoginPresenter
import com.acterics.dependencygraph.sample.presentation.login.LoginView
import com.acterics.dependencygraph.sample.ui.common.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter

class LoginFragment: BaseFragment(), LoginView {

    override var layoutRes: Int = R.layout.fragment_login

    @InjectPresenter lateinit var presenter: LoginPresenter
    @ProvidePresenter fun providePresenter(): LoginPresenter =
            scope.getInstance(LoginPresenter::class.java)


}