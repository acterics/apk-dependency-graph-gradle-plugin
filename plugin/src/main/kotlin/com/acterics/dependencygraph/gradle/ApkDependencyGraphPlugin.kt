package com.acterics.dependencygraph.gradle

import com.acterics.dependencygraph.gradle.tasks.AnalyzeSmaliTask
import com.acterics.dependencygraph.gradle.tasks.CopyWebResourcesTask
import com.acterics.dependencygraph.gradle.tasks.DecodeApkTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.task
open class ApkDependencyGraphPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        val pluginExtension = project.extensions.create<ApkDependencyGraphPluginExtension>("apkDependencyGraph")



        project.task("decodeApk", DecodeApkTask::class) {
            group = "dependency graph"
            outputDirectory = "${project.buildDir.absolutePath}/apk-dependency-graph/smali"
            extension = pluginExtension
        }

        project.task("copyApkGraphWebResources", CopyWebResourcesTask::class) {
            group = "dependency graph"
            outputDirectory = "${project.buildDir.absolutePath}/apk-dependency-graph/web"
        }

        project.task("buildGraph", AnalyzeSmaliTask::class) {
            group = "dependency graph"
            decodedApkDirectory = "${project.buildDir.absolutePath}/apk-dependency-graph/smali"
            outputDirectory = "${project.buildDir.absolutePath}/apk-dependency-graph/web"
            extension = pluginExtension
            dependsOn("decodeApk", "copyApkGraphWebResources")
        }

    }
}