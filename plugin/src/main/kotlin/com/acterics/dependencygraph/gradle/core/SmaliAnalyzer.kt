package com.acterics.dependencygraph.gradle.core

import java.io.File
import java.io.IOException
import java.util.regex.Matcher

sealed class AnalyzerResult {

    fun isSuccess() = this is Success

    object Success: AnalyzerResult()
    data class Failure(val error: Throwable): AnalyzerResult()
}



class SmaliAnalyzer(private val filter: String,
                    private val projectFolderPath: String,
                    private val withInnerClasses: Boolean = true) {

    private val dependencies: MutableMap<String, Set<String>> = hashMapOf()

    fun getDependencies(): Map<String, Set<String>> {
        return if (withInnerClasses) {
            dependencies
        } else {
            dependencies.filterInnerClasses()
        }
    }

    private val filterAsPath: String by lazy {
        val replacement = Matcher.quoteReplacement(File.separator)
        filter.replace(".", replacement)
    }

    fun run(): AnalyzerResult {
        println("filterPath: $filterAsPath")
        val projectFolder = File(projectFolderPath)
        if (projectFolder.exists()) {
            return try {
                traverseSmaliCode(projectFolder)
                AnalyzerResult.Success
            } catch (e: Exception) {
                AnalyzerResult.Failure(e)
            }
        } else {
            val error: Throwable = if (isInstantRunEnabled(projectFolderPath)) {
                IOException(WARNING_INSTANT_RUN_ENABLED)
            } else {
                IOException(WARNING_EMPTY_PROJECT_FOLDER)
            }
            return AnalyzerResult.Failure(error)

        }
    }


    private fun traverseSmaliCode(projectFolder: File) {
        projectFolder.listFiles().forEach { file ->
            when {
                file.isFile -> file.processSmaliFile()
                file.isDirectory -> traverseSmaliCode(file)
            }
        }
    }

    private fun File.processSmaliFile() {
        if (!isFileToProcess()) { return }

        val fileReader = this.bufferedReader()
        var fileName = name.substring(0, name.lastIndexOf("."))

        if (fileName.isClassR()) { return }

        if (fileName.isClassAnonymous()) {
            fileName = fileName.getAnonymousNearestOuter()
        }

        val classNames: MutableSet<String> = hashSetOf()
        val dependencyNames: MutableSet<String> = hashSetOf()

        fileReader.use { reader ->
            reader.lines().forEach { line ->
                classNames.clear()
                classNames.addParsedClassNames(line)

                classNames.forEach { fullClassName ->
                    if (fullClassName.isConformFilter()) {
                        val simpleClassName = fullClassName.getSimpleClassName()
                        if (simpleClassName.isConformClass(fileName)) {
                            dependencyNames.add(simpleClassName)
                        }
                    }
                }

            }
        }

        if (fileName.isClassInner()) {
            dependencyNames.add(fileName.getOuterClass())
        }

        if (!dependencyNames.isEmpty()) {
            addDependencies(fileName, dependencyNames)
        }


    }

    private fun addDependencies(fileName: String, dependencyNames: Set<String>) {
        println("add dependency: $fileName -> $dependencyNames")
        if (dependencies.containsKey(fileName)) {
            dependencies[fileName] = dependencies[fileName]!! + dependencyNames
        } else {
            dependencies[fileName] = dependencyNames
        }
    }

    private fun File.isFileToProcess(): Boolean {
        return name.endsWith(".smali") && absolutePath.contains(filterAsPath)
    }
    private fun String.isConformFilter() = startsWith(filter.replace(".", "/"))

    companion object {

        private const val WARNING_INSTANT_RUN_ENABLED = "Enabled Instant Run feature detected. We cannot decompile it. Please, disable Instant Run and rebuild your app."
        private const val WARNING_EMPTY_PROJECT_FOLDER = "Smali folder cannot be absent!"

        private val CLASSNAME_REGEX = Regex("[\\w\\d/$<>]*")

        private fun isInstantRunEnabled(projectFolderPath: String): Boolean {
            val unknownFolder = File("$projectFolderPath${File.separator}unknown")
            if (unknownFolder.exists()) {
                unknownFolder.listFiles().forEach { file ->
                    if (file.name == "instant-run.zip") {
                        return true
                    }
                }
            }
            return false
        }



        private fun String.isClassR() = this == "R" || startsWith("R$")
        private fun String.isClassGenerated() = contains("$$")

        private fun String.isClassAnonymous(): Boolean {
            return contains("$") && substring(lastIndexOf("$") + 1, length).toIntOrNull() != null
        }

        private fun String.isClassInner(): Boolean {
            return contains("$") && !isClassAnonymous() && !isClassGenerated()
        }

        /**
         * The last filter. Do not show anonymous classes (their dependencies belongs to outer class),
         * generated classes, avoid circular dependencies, do not show generated R class
         * @param fileName full class name
         * @return true if class is good with these conditions
         */
        private fun String.isConformClass(fileName: String): Boolean {
            return !isClassAnonymous() && !isClassGenerated() && this != fileName && !isClassR()
        }

        private fun String.getOuterClass(): String = substring(0, lastIndexOf("$"))

        private fun String.getAnonymousNearestOuter(): String {
            val classes = split("$")
            classes.withIndex().forEach { (i, className) ->
                if (className.toIntOrNull() != null) {
                    return classes.subList(0, i).withIndex().fold("") { acc, (j, value) ->
                       acc + value + if (j == i - 1) "" else "$"
                    }
                }
            }

            throw IOException("Class \"$this\" not anonymous!")

        }

        private fun String.getSimpleClassName(): String {
            var simpleClassName = substring(lastIndexOf("/") + 1, length)
            val startGenericIndex = simpleClassName.indexOf("<")
            if (startGenericIndex != -1) {
                simpleClassName = simpleClassName.substring(0, startGenericIndex)
            }
            return simpleClassName
        }


        private fun MutableSet<String>.addParsedClassNames(line: String) {
            var index = line.indexOf("L")
            while (index != -1) {
                val colonIndex = line.indexOf(";", index)
                if (colonIndex == -1) {
                    break
                }

                var className = line.substring(index + 1, colonIndex)
                if (className.matches(CLASSNAME_REGEX)) {
                    val startGenericIndex = className.indexOf("<")
                    if (startGenericIndex != -1 && className[startGenericIndex + 1] == 'L') {
                        val startGenericLineIndex = index + startGenericIndex + 1
                        val endGenericLineIndex = line.getEndGenericIndex(startGenericLineIndex)
                        val generic = line.substring(startGenericLineIndex + 1, endGenericLineIndex)
                        addParsedClassNames(generic)
                        index = line.indexOf("L", endGenericLineIndex)
                        className = className.substring(0, startGenericIndex)
                    } else {
                        index = line.indexOf("L", colonIndex)
                    }
                } else {
                    index = line.indexOf("L", index + 1)
                    continue
                }

                add(className)
            }
        }

        private fun String.getEndGenericIndex(startGenericIndex: Int): Int {
            var endIndex = indexOf(">", startGenericIndex)
            for (index: Int in (endIndex + 2)..length step 2) {
                if (get(index) == '>') {
                    endIndex = index
                }
            }
            return endIndex
        }


        private fun Map<String, Set<String>>.filterInnerClasses(): Map<String, Set<String>> {
            val filteredDependencies = hashMapOf<String, Set<String>>()
            forEach { className, dependencies ->
                if (!className.contains("$")) {
                    dependencies.filter { !it.contains("$") }.toSet().let { set ->
                        if (!set.isEmpty()) {
                            filteredDependencies[className] = set
                        }
                    }
                }
            }
            return filteredDependencies
        }




    }
}