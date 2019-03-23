package com.acterics.dependencygraph.sample

import android.app.Application
import com.acterics.dependencygraph.sample.di.DI
import com.acterics.dependencygraph.sample.di.module.AppModule
import toothpick.Toothpick
import toothpick.configuration.Configuration
import java.util.*

class SampleApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        appCode = UUID.randomUUID().toString()
        initToothpick()
        initAppScope()
    }


    private fun initToothpick() {
        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            Toothpick.setConfiguration(Configuration.forProduction().disableReflection())
        }
    }

    private fun initAppScope() {
        Toothpick.openScope(DI.APP_SCOPE)
                .installModules(AppModule(this))
    }

    companion object {
        lateinit var appCode: String
            private set
    }

}