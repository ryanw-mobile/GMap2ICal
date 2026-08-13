/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.local.interfaces

import java.io.InputStream

interface LocalDataSource {
    suspend fun getFileList(absolutePath: String, extension: String): Result<List<String>>
    suspend fun fileWriter(filePath: String, contents: String): Result<Unit>
    suspend fun openInputStream(filePath: String): InputStream
    suspend fun readStringFromFile(filePath: String): String
}
