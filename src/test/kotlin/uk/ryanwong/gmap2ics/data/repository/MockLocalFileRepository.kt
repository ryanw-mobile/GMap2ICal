package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.VEvent

class MockLocalFileRepository : LocalFileRepository {
    override suspend fun exportICal(filename: String, vEvents: List<VEvent>) {
        TODO("Not yet implemented")
    }

    override suspend fun getFileList(absolutePath: String, extension: String): List<String>? {
        TODO("Not yet implemented")
    }
}