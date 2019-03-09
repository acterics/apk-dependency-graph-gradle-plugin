package com.acterics.wmu.gradle.tasks

import com.acterics.wmu.gradle.core.ApkSmaliDecoder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
open class DecodeApkTask: DefaultTask() {

    init {
        description = "Decode apk file to smali"
    }

    @InputFile
    lateinit var apkFilePath: String

    @OutputDirectory
    lateinit var outputDirectory: String

    private val decoder: ApkSmaliDecoder by lazy {
        ApkSmaliDecoder(apkFilePath, outputDirectory,28)
    }

    @TaskAction
    fun run() = decoder.decode()
}