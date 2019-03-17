package com.acterics.dependencygraph.gradle

open class ApkDependencyGraphPluginExtension {
    var filterPackage = ""
    var apkPath: String? = null
    var includeInnerClasses = false
    var apiVersion = 28
}