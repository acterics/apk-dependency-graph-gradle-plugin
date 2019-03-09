package com.acterics.wmu.gradle.core

import org.jf.baksmali.Baksmali
import org.jf.baksmali.BaksmaliOptions
import org.jf.dexlib2.DexFileFactory
import org.jf.dexlib2.Opcodes
import org.jf.dexlib2.analysis.InlineMethodResolver
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.dexbacked.DexBackedOdexFile
import java.io.File
import java.io.IOException
import java.util.zip.ZipFile

class ApkSmaliDecoder(private val apkFilePath: String,
                      private val outDirPath: String,
                      private val apiVersion: Int) {


    fun decode() {
        println("ApkFile: $apkFilePath")
        val apkFile = File(apkFilePath)
        if (!apkFile.exists()) {
            throw IOException(WARNING_APK_NOT_FOUND)
        }
        val outDir = File(outDirPath)
        apkFilePath.getZipFiles(DEX_FILE_EXTENSION)
                .map(toDexFiles(apkFile, apiVersion))
                .forEach(disassembleDexFile(outDir))
    }

    private fun disassembleDexFile(outDir: File): (DexBackedDexFile) -> Unit {
        return { dexFile ->
            Baksmali.disassembleDexFile(
                    dexFile,
                    outDir,
                    getNumberOfAvailableProcessors(),
                    buildSmaliOptions(dexFile)
            )
        }
    }

    private fun buildSmaliOptions(dexFile: DexBackedDexFile): BaksmaliOptions {
        return BaksmaliOptions().apply {
            deodex = false
            implicitReferences = false
            parameterRegisters = true
            localsDirective = true
            sequentialLabels = true
            debugInfo = false
            codeOffsets = false
            accessorComments = false
            registerInfo = 0
            if (dexFile is DexBackedOdexFile) {
                inlineResolver = InlineMethodResolver.createInlineMethodResolver(dexFile.odexVersion)
            } else {
                inlineResolver = null
            }
        }
    }



    companion object {
        private const val MAX_NUMBER_OF_PROCESSORS = 6
        private const val DEX_FILE_EXTENSION = ".dex"

        private const val WARNING_APK_NOT_FOUND = "Apk file is not found!"
        private const val WARNING_DISASSEMBLING_ODEX_FILE = "Warning: You are disassembling an odex file without deodexing it."

        private fun getNumberOfAvailableProcessors(): Int {
            val jobs = Runtime.getRuntime().availableProcessors()
            return Math.min(jobs, MAX_NUMBER_OF_PROCESSORS)
        }

        private fun String.getZipFiles(extension: String = ""): List<String> {
            val files = mutableListOf<String>()
            val zipFile = ZipFile(this)
            val zipEntries = zipFile.entries()

            while(zipEntries.hasMoreElements()) {
                val zipEntry = zipEntries.nextElement()
                if (!zipEntry.isDirectory) {
                    val fileName = zipEntry.name
                    if (fileName.endsWith(extension)) {
                        files.add(fileName)
                    }
                }
            }
            return files
        }

        private fun toDexFiles(apkFile: File, apiVersion: Int): (String) -> DexBackedDexFile {
            return { filePath -> filePath.loadDexFile(apkFile, apiVersion) }
        }

        private fun String.loadDexFile(apkFile: File, apiVersion: Int): DexBackedDexFile {
            val dexFile = DexFileFactory.loadDexEntry(
                    apkFile, this, true, Opcodes.forApi(apiVersion)
            )
            if (dexFile == null || dexFile.isOdexFile) {
                throw IOException(WARNING_DISASSEMBLING_ODEX_FILE)
            }
            return dexFile
        }

    }
}