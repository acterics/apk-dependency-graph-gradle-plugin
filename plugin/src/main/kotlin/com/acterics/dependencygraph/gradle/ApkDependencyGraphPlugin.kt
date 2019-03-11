package com.acterics.dependencygraph.gradle

import com.acterics.dependencygraph.gradle.tasks.AnalyzeSmaliTask
import com.acterics.dependencygraph.gradle.tasks.CopyWebResourcesTask
import com.acterics.dependencygraph.gradle.tasks.DecodeApkTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.task
open class ApkDependencyGraphPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.task("decodeApk", DecodeApkTask::class) {
            group = "dependency graph"
            outputDirectory = "${project.buildDir.absolutePath}/apk-dependency-graph/smali"
            apkFilePath = "${project.buildDir.absolutePath}/outputs/apk/debug/app-debug.apk"
        }

        project.task("copyApkGraphWebResources", CopyWebResourcesTask::class) {
            group = "dependency graph"
            outputDirectory = "${project.buildDir.absolutePath}/apk-dependency-graph/web"
        }

        project.task("analyzeSmali", AnalyzeSmaliTask::class) {
            group = "dependency graph"
            decodedApkDirectory = "${project.buildDir.absolutePath}/apk-dependency-graph/smali"
            outputDirectory = "${project.buildDir.absolutePath}/apk-dependency-graph/web"
            dependsOn("decodeApk", "copyApkGraphWebResources")
        }

    }
}