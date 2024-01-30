/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repository.mocks

import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.repository.LocalFileRepository

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
    override suspend fun getFileList(relativePath: String, extension: String): Result<List<String>> {
        return getFileListResponse ?: Result.failure(Exception("mock response unavailable"))
    }
}