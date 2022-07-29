package uk.ryanwong.gmap2ics.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.data.source.local.LocalDataSource
import uk.ryanwong.gmap2ics.data.source.local.LocalDataSourceImpl
import uk.ryanwong.gmap2ics.domain.models.VEvent

class LocalFileRepositoryImpl(
    private val localDataSource: LocalDataSource = LocalDataSourceImpl(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalFileRepository {

    override suspend fun getFileList(absolutePath: String, extension: String): List<String>? {
        return withContext(dispatcher) {
            localDataSource.getFileList(absolutePath = absolutePath, extension = extension)
        }
    }

    override suspend fun exportICal(filename: String, vEvents: List<VEvent>) {
        withContext(dispatcher) {
            println("💾 Exporting events in iCal format to $filename")

            // Preparing the file contents to write in one go
            val stringBuilder = StringBuilder()

            stringBuilder.run {
                append("BEGIN:VCALENDAR\n")
                append("VERSION:2.0\n")

                vEvents.forEach { vEvent ->
                    append(vEvent.export())
                }

                append("END:VCALENDAR\n")
            }

            localDataSource.fileWriter(filename, stringBuilder.toString())
        }
    }
}