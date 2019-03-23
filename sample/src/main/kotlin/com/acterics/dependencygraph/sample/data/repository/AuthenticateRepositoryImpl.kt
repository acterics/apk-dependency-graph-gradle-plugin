package com.acterics.dependencygraph.sample.data.repository

import com.acterics.dependencygraph.sample.domain.repository.AuthenticateRepository
import io.reactivex.Maybe
import javax.inject.Inject

class AuthenticateRepositoryImpl
@Inject constructor(): AuthenticateRepository {
    override fun getLogin(): Maybe<String> {
        return Maybe.fromAction {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}