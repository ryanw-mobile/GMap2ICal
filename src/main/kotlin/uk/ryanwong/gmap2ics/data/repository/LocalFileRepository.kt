package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.VEvent

interface LocalFileRepository {
    suspend fun exportICal(filename: String, vEvents: List<VEvent>)
    suspend fun getFileList(absolutePath: String, extension: String): List<String>?
}