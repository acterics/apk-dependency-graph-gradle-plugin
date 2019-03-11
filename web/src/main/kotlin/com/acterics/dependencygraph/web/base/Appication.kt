package com.acterics.dependencygraph.web.base

abstract class Application {
    abstract val stateKeys: List<String>

    abstract fun start(state: Map<String, Any>)
    abstract fun dispose(): Map<String, Any>
}