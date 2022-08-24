/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.local.mocks

import uk.ryanwong.gmap2ics.data.source.local.LocalDataSource

class MockLocalDataSource : LocalDataSource {
    var getFileListResponse: Result<List<String>>? = null
    override suspend fun getFileList(absolutePath: String, extension: String): Result<List<String>> {
        return getFileListResponse ?: Result.failure(Exception("mock response unavailable"))
    }

    var fileWriterFileName: String? = null
        private set
    var fileWriterContents: String? = null
        private set
    var fileWriterResponse: Result<Unit>? = null
    override suspend fun fileWriter(filename: String, contents: String): Result<Unit> {
        fileWriterFileName = filename
        fileWriterContents = contents
        return fileWriterResponse ?: Result.failure(Exception("mock response unavailable"))
    }

    var getJsonStringResponse: String = ""
    override suspend fun getJsonString(filePath: String): String {
        return getJsonStringResponse
    }
}