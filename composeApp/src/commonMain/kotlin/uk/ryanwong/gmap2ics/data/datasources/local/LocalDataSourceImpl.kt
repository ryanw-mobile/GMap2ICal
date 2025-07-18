/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.local

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.data.datasources.local.interfaces.LocalDataSource
import uk.ryanwong.gmap2ics.data.except
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths

class LocalDataSourceImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : LocalDataSource {

    /***
     * Note: Current implementation accepts paths outside the project directory
     */
    override suspend fun getFileList(absolutePath: String, extension: String): Result<List<String>> = withContext(dispatcher) {
        Result.runCatching {
            val fileList = mutableListOf<String>()

            Files.walk(Paths.get(absolutePath))
                .filter { file -> Files.isRegularFile(file) }
                .filter { file -> file.toString().endsWith(suffix = ".$extension") }
                .forEach { file -> fileList.add(file.toString()) }
            fileList
        }.except<CancellationException, _>()
    }

    override suspend fun readStringFromFile(filePath: String): String = withContext(dispatcher) {
        File(filePath).readText(Charsets.UTF_8)
    }

    override suspend fun fileWriter(filePath: String, contents: String): Result<Unit> = withContext(dispatcher) {
        Result.runCatching {
            FileWriter(filePath, false).use { fileWriter ->
                fileWriter.write(contents)
            }
        }.except<CancellationException, _>()
    }
}
