package com.acterics.dependencygraph.web

import com.acterics.dependencygraph.web.app.require
import org.w3c.dom.get
import kotlin.browser.document
import kotlin.browser.window

external val dependencies: dynamic
val d3: dynamic = require("d3")

val element = document.documentElement
val body = document.getElementsByTagName("body")[0]


val x: Int = window.innerWidth.nonZero() ?: element?.clientWidth.nonZero() ?: body?.clientWidth.nonZero() ?: 0
val y: Int = window.innerHeight.nonZero() ?: element?.clientHeight.nonZero() ?: body?.clientHeight.nonZero() ?: 0

private fun Int?.nonZero(): Int? {
    return if (this == 0) {
        null
    } else {
        this
    }
}