/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import uk.ryanwong.gmap2ics.data.datasources.local.LocalDataSourceImpl
import uk.ryanwong.gmap2ics.data.datasources.local.interfaces.LocalDataSource
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.models.timeline.TimelineFileDto
import uk.ryanwong.gmap2ics.domain.models.timeline.Timeline
import uk.ryanwong.gmap2ics.domain.repositories.TimelineRepository
import uk.ryanwong.gmap2ics.domain.utils.timezonemap.TimeZoneMapWrapper
import kotlin.coroutines.cancellation.CancellationException

class TimelineRepositoryImpl(
    private val localDataSource: LocalDataSource = LocalDataSourceImpl(),
    private val timeZoneMap: TimeZoneMapWrapper,
    private val kotlinJson: Json,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : TimelineRepository {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getTimeLine(filePath: String): Result<Timeline> = withContext(dispatcher) {
        Result.runCatching {
            val timelineFileDto = localDataSource.openInputStream(filePath = filePath).use { inputStream ->
                kotlinJson.decodeFromStream(TimelineFileDto.serializer(), inputStream)
            }
            val timelineObjectsDto = timelineFileDto.toTimelineObjectsDto()
            Timeline.from(timelineObjectsDto = timelineObjectsDto, timeZoneMap = timeZoneMap)
        }.except<CancellationException, _>()
    }
}
