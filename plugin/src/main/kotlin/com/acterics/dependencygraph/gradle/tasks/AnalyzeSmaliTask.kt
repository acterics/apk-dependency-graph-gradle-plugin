package com.acterics.dependencygraph.gradle.tasks

import com.acterics.dependencygraph.gradle.ApkDependencyGraphPluginExtension
import com.acterics.dependencygraph.gradle.core.AnalyzerResult
import com.acterics.dependencygraph.gradle.core.DependenciesFileWriter
import com.acterics.dependencygraph.gradle.core.SmaliAnalyzer
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.impldep.com.beust.jcommander.internal.Maps
import java.io.File

open class AnalyzeSmaliTask: DefaultTask() {

    lateinit var extension: ApkDependencyGraphPluginExtension

    @InputDirectory
    lateinit var decodedApkDirectory: String

    @OutputDirectory
    lateinit var outputDirectory: String



    private val analyzer: SmaliAnalyzer
        get() = SmaliAnalyzer(
                extension.filterPackage,
                decodedApkDirectory,
                extension.includeInnerClasses
        )

    @TaskAction
    fun run() {
        val analyzer = analyzer
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