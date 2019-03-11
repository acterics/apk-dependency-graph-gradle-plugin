package com.acterics.dependencygraph.web

import com.acterics.dependencygraph.web.base.Application
import com.acterics.dependencygraph.web.base.module
import com.acterics.dependencygraph.web.base.require
import kotlin.browser.document
import kotlin.dom.hasClass


external val dependencies: dynamic

val d3: dynamic = require("d3")!!

fun main(args: Array<String>) {
    var application: Application? = null

    val state: dynamic = module.hot?.let { hot ->
        hot.accept()
        hot.dispose { data ->
            data.appState = application?.dispose()
            application = null
        }

        hot.data
    }

    if (document.body != null) {
        application = start(state)
    } else {
        application = null
        document.addEventListener("DOMContentLoaded", { application = start(state) })
    }
}

fun start(state: dynamic): Application? {
    return if (document.body?.hasClass("mainApplication") == true) {
        val application = MainApplication()

        @Suppress("UnsafeCastFromDynamic")
        application.start(state?.appState ?: emptyMap())

        application
    } else {
        null
    }
}

