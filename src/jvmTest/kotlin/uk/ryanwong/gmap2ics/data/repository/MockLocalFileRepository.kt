/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.VEvent

class MockLocalFileRepository : LocalFileRepository {
    var exportICalResponse: Result<Unit>? = null
    var exportICalFilename: String? = null
    var exportICalVEvents: List<VEvent>? = null
    override suspend fun exportICal(filename: String, vEvents: List<VEvent>): Result<Unit> {
        exportICalFilename = filename
        exportICalVEvents = vEvents
        return exportICalResponse ?: Result.failure(Exception("mock response unavailable"))
    }

    var getFileListResponse: Result<List<String>>? = null
    override suspend fun getFileList(absolutePath: String, extension: String): Result<List<String>> {
        return getFileListResponse ?: Result.failure(Exception("mock response unavailable"))
    }
}