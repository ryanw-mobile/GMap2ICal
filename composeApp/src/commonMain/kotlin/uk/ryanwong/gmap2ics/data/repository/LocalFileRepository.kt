/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.VEvent

interface LocalFileRepository {
    suspend fun exportICal(filename: String, vEvents: List<VEvent>): Result<Unit>
    suspend fun getFileList(relativePath: String, extension: String): Result<List<String>>
}
