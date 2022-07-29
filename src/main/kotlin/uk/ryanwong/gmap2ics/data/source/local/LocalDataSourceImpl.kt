package uk.ryanwong.gmap2ics.data.source.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths

class LocalDataSourceImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalDataSource {

    override suspend fun getFileList(absolutePath: String, extension: String): List<String>? {
        return withContext(dispatcher) {
            try {
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
    }

    override suspend fun fileWriter(filename: String, contents: String) {
        withContext(dispatcher) {
            FileWriter(filename, false).use { fileWriter ->
                fileWriter.write(contents)
            }
        }
    }
}