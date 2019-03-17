package com.acterics.dependencygraph.gradle.tasks

import com.acterics.dependencygraph.gradle.ApkDependencyGraphPluginExtension
import com.acterics.dependencygraph.gradle.core.ApkSmaliDecoder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
open class DecodeApkTask: DefaultTask() {

    init {
        description = "Decode apk file to smali"
    }

    lateinit var extension: ApkDependencyGraphPluginExtension

    @InputFile
    fun getApkFilePath() = extension.apkPath ?: ""


    @OutputDirectory
    lateinit var outputDirectory: String

    private val decoder: ApkSmaliDecoder
        get() = ApkSmaliDecoder(
                getApkFilePath(),
                outputDirectory,
                extension.apiVersion
        )

    @TaskAction
    fun run() = decoder.decode()
}