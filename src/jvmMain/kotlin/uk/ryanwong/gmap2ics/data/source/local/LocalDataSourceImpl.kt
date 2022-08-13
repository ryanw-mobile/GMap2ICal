/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.local

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.data.except
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths

class LocalDataSourceImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalDataSource {

    override suspend fun getFileList(absolutePath: String, extension: String): Result<List<String>> {
        return withContext(dispatcher) {
            Result.runCatching {
                val fileList = mutableListOf<String>()
                val projectDirAbsolutePath = Paths.get("").toAbsolutePath().toString()
                val resourcesPath = Paths.get(projectDirAbsolutePath, absolutePath)

                Files.walk(resourcesPath)
                    .filter { file -> Files.isRegularFile(file) }
                    .filter { file -> file.toString().endsWith(suffix = ".$extension") }
                    .forEach { file -> fileList.add(file.toString()) }
                fileList
            }.except<CancellationException, _>()
        }
    }

    override suspend fun getJsonString(filePath: String): String {
        return withContext(dispatcher) {
            File(filePath).readText(Charsets.UTF_8)
        }
    }

    override suspend fun fileWriter(filename: String, contents: String): Result<Unit> {
        return withContext(dispatcher) {
            Result.runCatching {
                FileWriter(filename, false).use { fileWriter ->
                    fileWriter.write(contents)
                }
            }.except<CancellationException, _>()
        }
    }
}