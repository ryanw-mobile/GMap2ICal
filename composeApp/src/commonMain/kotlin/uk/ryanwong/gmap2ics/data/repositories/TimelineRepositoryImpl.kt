/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import uk.ryanwong.gmap2ics.data.datasources.local.LocalDataSource
import uk.ryanwong.gmap2ics.data.datasources.local.impl.LocalDataSourceImpl
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.models.timeline.TimelineObjects
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

    override suspend fun getTimeLine(filePath: String): Result<Timeline> {
        return withContext(dispatcher) {
            Result.runCatching {
                val jsonString = localDataSource.readStringFromFile(filePath = filePath)
                val timelineObjects = kotlinJson.decodeFromString(TimelineObjects.serializer(), jsonString)
                Timeline.from(timelineObjects = timelineObjects, timeZoneMap = timeZoneMap)
            }.except<CancellationException, _>()
        }
    }
}
