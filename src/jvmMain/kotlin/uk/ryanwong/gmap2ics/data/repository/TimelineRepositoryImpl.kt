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
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.TimelineObjects
import java.io.File
import kotlin.coroutines.cancellation.CancellationException

class TimelineRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TimelineRepository {
    private val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
        registerKotlinModule()
        registerModule(JavaTimeModule())
    }

    override suspend fun parseTimeLine(filePath: String): Result<TimelineObjects> {
        return withContext(dispatcher) {
            Result.runCatching {
                val jsonString = File(filePath).readText(Charsets.UTF_8)
                val retVal: TimelineObjects = objectMapper.readValue(content = jsonString)
                retVal
            }.except<CancellationException, _>()
        }
    }
}