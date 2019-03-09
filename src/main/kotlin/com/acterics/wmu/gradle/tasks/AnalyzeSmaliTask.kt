package com.acterics.wmu.gradle.tasks

import com.acterics.wmu.gradle.core.AnalyzerResult
import com.acterics.wmu.gradle.core.DependenciesFileWriter
import com.acterics.wmu.gradle.core.SmaliAnalyzer
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

open class AnalyzeSmaliTask: DefaultTask() {

    @InputDirectory
    lateinit var decodedApkDirectory: String

    @OutputDirectory
    lateinit var outputDirectory: String



    private val analyzer: SmaliAnalyzer by lazy {
        SmaliAnalyzer("com.acterics", decodedApkDirectory, false)
    }

    @TaskAction
    fun run() {
        val analyzeResult = analyzer.run()

        if (analyzeResult is AnalyzerResult.Failure) {
            throw analyzeResult.error
        }

        val dependencies = analyzer.getDependencies()
        val resultFile = File("$outputDirectory/analyzed.js")
        val writer = DependenciesFileWriter(resultFile)
        writer.write(dependencies)
    }

}