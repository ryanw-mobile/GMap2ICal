/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repositories

import uk.ryanwong.gmap2ics.data.datasources.local.interfaces.LocalDataSource
import uk.ryanwong.gmap2ics.data.datasources.local.LocalDataSourceImpl
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.repositories.LocalFileRepository
import java.nio.file.Paths

class LocalFileRepositoryImpl(
    private val localDataSource: LocalDataSource = LocalDataSourceImpl(),
) : LocalFileRepository {

    override suspend fun getFileList(relativePath: String, extension: String): Result<List<String>> {
        val projectDirAbsolutePath = Paths.get("").toAbsolutePath().toString()
        val absolutePath = Paths.get(projectDirAbsolutePath, relativePath).toString()

        return localDataSource.getFileList(absolutePath = absolutePath, extension = extension)
    }

    // Note: It would be nice if we could have a ready-to-use Kotlin-based serializer but not found one yet
    override suspend fun exportICal(filename: String, vEvents: List<VEvent>): Result<Unit> {
        val stringBuilder = StringBuilder().run {
            append("BEGIN:VCALENDAR\n")
            append("VERSION:2.0\n")

            vEvents.forEach { vEvent ->
                append(vEvent.export())
            }

            append("END:VCALENDAR\n")
        }

        return localDataSource.fileWriter(filename, stringBuilder.toString())
    }
}
