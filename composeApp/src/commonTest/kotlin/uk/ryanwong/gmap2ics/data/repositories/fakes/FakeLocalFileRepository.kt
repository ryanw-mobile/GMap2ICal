/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repositories.fakes

import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.repositories.LocalFileRepository

class FakeLocalFileRepository : LocalFileRepository {
    var exportICalResponse: Result<Unit>? = null
    var exportICalFilename: String? = null
    var exportICalVEvents: List<VEvent>? = null
    override suspend fun exportICal(filename: String, vEvents: List<VEvent>): Result<Unit> {
        exportICalFilename = filename
        exportICalVEvents = vEvents
        return exportICalResponse ?: Result.failure(Exception("response not defined"))
    }

    var getFileListResponse: Result<List<String>>? = null
    override suspend fun getFileList(relativePath: String, extension: String): Result<List<String>> {
        return getFileListResponse ?: Result.failure(Exception("response not defined"))
    }
}
