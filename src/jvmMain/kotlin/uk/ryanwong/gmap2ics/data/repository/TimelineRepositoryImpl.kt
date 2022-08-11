/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.app.models.timeline.Timeline
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.TimelineObjects
import uk.ryanwong.gmap2ics.data.source.local.LocalDataSource
import uk.ryanwong.gmap2ics.data.source.local.LocalDataSourceImpl
import uk.ryanwong.gmap2ics.utils.timezonemap.TimeZoneMapWrapper
import kotlin.coroutines.cancellation.CancellationException

class TimelineRepositoryImpl(
    private val localDataSource: LocalDataSource = LocalDataSourceImpl(),
    private val timeZoneMap: TimeZoneMapWrapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TimelineRepository {
    private val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
        registerKotlinModule()
        registerModule(JavaTimeModule())
    }

    override suspend fun getTimeLine(filePath: String): Result<Timeline> {
        return withContext(dispatcher) {
            Result.runCatching {
                val jsonString = localDataSource.getJsonString(filePath = filePath)
                val timelineObjects: TimelineObjects = objectMapper.readValue(content = jsonString)
                Timeline.from(timelineObjects = timelineObjects, timeZoneMap = timeZoneMap)
            }.except<CancellationException, _>()
        }
    }
}