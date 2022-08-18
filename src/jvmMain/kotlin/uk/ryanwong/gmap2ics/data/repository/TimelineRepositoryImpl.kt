/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import uk.ryanwong.gmap2ics.app.models.timeline.Timeline
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapWrapper
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.models.timeline.TimelineObjects
import uk.ryanwong.gmap2ics.data.source.local.LocalDataSource
import uk.ryanwong.gmap2ics.data.source.local.LocalDataSourceImpl
import kotlin.coroutines.cancellation.CancellationException

class TimelineRepositoryImpl(
    private val localDataSource: LocalDataSource = LocalDataSourceImpl(),
    private val timeZoneMap: TimeZoneMapWrapper,
    private val kotlinJson: Json,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TimelineRepository {

    override suspend fun getTimeLine(filePath: String): Result<Timeline> {
        return withContext(dispatcher) {
            Result.runCatching {
                val jsonString = localDataSource.getJsonString(filePath = filePath)
                val timelineObjects = kotlinJson.decodeFromString(TimelineObjects.serializer(), jsonString)
                Timeline.from(timelineObjects = timelineObjects, timeZoneMap = timeZoneMap)
            }.except<CancellationException, _>()
        }
    }
}