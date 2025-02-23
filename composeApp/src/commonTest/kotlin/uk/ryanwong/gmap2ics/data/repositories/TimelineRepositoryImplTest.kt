/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import uk.ryanwong.gmap2ics.app.utils.timezonemap.fakes.FakeTimeZoneMap
import uk.ryanwong.gmap2ics.data.datasources.local.fakes.FakeLocalDataSource
import uk.ryanwong.gmap2ics.data.repositories.TimelineRepositoryImplTestData.JSON_STRING
import uk.ryanwong.gmap2ics.data.repositories.TimelineRepositoryImplTestData.timeLineFromJsonString
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class TimelineRepositoryImplTest {

    private lateinit var timelineRepository: TimelineRepositoryImpl
    private lateinit var localDataSource: FakeLocalDataSource
    private lateinit var fakeTimeZoneMap: FakeTimeZoneMap
    private val kotlinJson = Json { ignoreUnknownKeys = true }

    @BeforeTest
    fun setup() {
        localDataSource = FakeLocalDataSource()
        fakeTimeZoneMap = FakeTimeZoneMap()

        timelineRepository = TimelineRepositoryImpl(
            localDataSource = localDataSource,
            timeZoneMap = fakeTimeZoneMap,
            dispatcher = UnconfinedTestDispatcher(),
            kotlinJson = kotlinJson,
        )
    }

    @Test
    fun `returns TimeLine object when data source returns valid JSON String`() = runTest {
        localDataSource.getJsonStringResponse = JSON_STRING
        fakeTimeZoneMap.zoneId = "Asia/Tokyo"

        val timeLine = timelineRepository.getTimeLine(filePath = "/some-absolute-path/")

        assertTrue(timeLine.isSuccess)
        assertEquals(timeLineFromJsonString, timeLine.getOrNull())
    }

    @Test
    fun `returns failure result when data source returns invalid JSON String`() = runTest {
        // Alternatively can Mock json.decodeFromString and throw an exception
        val expectedMessage = "Unexpected JSON token at offset 0: Expected start of the object '{', but had 's' instead at path: \$\nJSON input: some-invalid-json-string"
        localDataSource.getJsonStringResponse = "some-invalid-json-string"
        fakeTimeZoneMap.zoneId = "Asia/Tokyo"

        val timeLine = timelineRepository.getTimeLine(filePath = "/some-absolute-path/")

        // kotlinx.serialization.json.internal.JsonDecodingException is internal. Assert message only.
        assertTrue(timeLine.isFailure)
        assertEquals(expectedMessage, timeLine.exceptionOrNull()!!.message)
    }
}
