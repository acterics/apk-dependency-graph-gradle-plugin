package com.acterics.dependencygraph.sample.domain.repository

import io.reactivex.Maybe

interface AuthenticateRepository {

    fun getLogin(): Maybe<String>

}