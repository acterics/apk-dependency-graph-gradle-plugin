package com.acterics.dependencygraph.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException
import java.util.jar.JarFile

open class CopyWebResourcesTask: DefaultTask() {

    @OutputDirectory
    lateinit var outputDirectory: String

    @TaskAction
    fun run() {
        val resUri = javaClass.classLoader.getResource("web")
        val resString = resUri.toString()
        val bang = resString.indexOf("!")
        val prefix = "jar:file:"
        if (!resString.startsWith(prefix) || bang == -1) {
            throw IOException("Illegal resource uri: $resString")
        }
        val jarFile = JarFile(resString.substring(prefix.length, bang))
        val entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            if (!entry.name.startsWith("web")) { continue }
            val fileName = "$outputDirectory/${entry.name.substring("web/".length)}"
            val file = File(fileName)
            if (entry.isDirectory) {
                file.mkdirs()
                continue
            }

            jarFile.getInputStream(entry).use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }

}