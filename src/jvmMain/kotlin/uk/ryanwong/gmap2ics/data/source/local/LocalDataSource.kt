/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.local

interface LocalDataSource {
    suspend fun getFileList(absolutePath: String, extension: String): Result<List<String>>
    suspend fun fileWriter(filename: String, contents: String): Result<Unit>
}