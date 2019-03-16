package com.acterics.dependencygraph.gradle.core

import java.io.File

class DependenciesFileWriter(private val output: File) {

    fun write(dependencies: Map<String, Set<String>>) {
        output.bufferedWriter().use { writer ->
            writer.write("var dependencies = {links:[\n")
            dependencies.forEach { className, dependencies ->
                dependencies.forEach { dependency ->
                    writer.write("{\"source\":\"$className\",\"dest\":\"$dependency\"},\n")
                }
            }
            writer.write("]};")
        }
    }

}