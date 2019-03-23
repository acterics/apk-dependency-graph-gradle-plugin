package com.acterics.dependencygraph.sample.common

import android.util.Log

fun debugPrint(message: String) {
    Log.d("DEBUG", message)
}

fun Any.objectScopeName() = "${javaClass.simpleName}_${hashCode()}"