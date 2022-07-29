package uk.ryanwong.gmap2ics.data.source.local

interface LocalDataSource {
    suspend fun getFileList(absolutePath: String, extension: String): List<String>?
}