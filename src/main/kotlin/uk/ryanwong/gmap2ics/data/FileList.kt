package uk.ryanwong.gmap2ics.data

import java.nio.file.Files
import java.nio.file.Paths

fun getFileList(absolutePath: String, extension: String): List<String>? {
    return try {
        val fileList = mutableListOf<String>()
        val projectDirAbsolutePath = Paths.get("").toAbsolutePath().toString()
        val resourcesPath = Paths.get(projectDirAbsolutePath, absolutePath)

        Files.walk(resourcesPath)
            .filter { file -> Files.isRegularFile(file) }
            .filter { file -> file.toString().endsWith(suffix = ".$extension") }
            .forEach { file -> fileList.add(file.toString()) }
        fileList
    } catch (ex: Exception) {
        println("☠️ Error getting json file list: ${ex.localizedMessage}")
        null
    }
}