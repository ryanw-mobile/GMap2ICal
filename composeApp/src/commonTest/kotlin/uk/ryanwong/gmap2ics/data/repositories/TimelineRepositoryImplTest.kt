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
import kotlin.test.assertNotNull
import kotlin.test.assertNull
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

    @Test
    fun `converts on-device semantic segments into timeline entries`() = runTest {
        localDataSource.getJsonStringResponse = MODERN_TIMELINE_JSON_STRING
        fakeTimeZoneMap.zoneId = "America/Denver"

        val result = timelineRepository.getTimeLine(filePath = "/some-absolute-path/Timeline.json")

        assertTrue(result.isSuccess)
        val entries = assertNotNull(result.getOrNull()).timelineEntries
        assertEquals(2, entries.size)

        val placeVisit = assertNotNull(entries[0].placeVisit)
        assertNull(entries[0].activitySegment)
        assertEquals("ChIJ-example", placeVisit.location.placeId)
        assertEquals("Home", placeVisit.location.name)
        assertEquals(39.7392, placeVisit.location.getLatitude())
        assertEquals(-104.9903, placeVisit.location.getLongitude())
        assertEquals("2026-08-10T08:30:00.000-06:00", placeVisit.durationStartTimestamp.timestamp)

        val activitySegment = assertNotNull(entries[1].activitySegment)
        assertNull(entries[1].placeVisit)
        assertEquals("IN_PASSENGER_VEHICLE", activitySegment.rawActivityType)
        assertEquals(1235, activitySegment.distance)
        assertEquals(39.7392, activitySegment.startLocation.getLatitude())
        assertEquals(-105.0103, activitySegment.endLocation.getLongitude())
    }

    @Test
    fun `returns failure for valid JSON with an unsupported root schema`() = runTest {
        localDataSource.getJsonStringResponse = """{"rawSignals": []}"""

        val result = timelineRepository.getTimeLine(filePath = "/some-absolute-path/Timeline.json")

        assertTrue(result.isFailure)
        assertEquals(
            "Unsupported Timeline JSON: expected 'timelineObjects' or 'semanticSegments' at the root",
            result.exceptionOrNull()?.message,
        )
    }

    private companion object {
        const val MODERN_TIMELINE_JSON_STRING = """
            {
              "semanticSegments": [
                {
                  "startTime": "2026-08-10T08:30:00.000-06:00",
                  "endTime": "2026-08-10T09:00:00.000-06:00",
                  "visit": {
                    "topCandidate": {
                      "placeId": "ChIJ-example",
                      "semanticType": "HOME",
                      "placeLocation": {"latLng": "39.7392°, -104.9903°"}
                    }
                  }
                },
                {
                  "startTime": "2026-08-10T09:00:00.000-06:00",
                  "endTime": "2026-08-10T09:15:00.000-06:00",
                  "activity": {
                    "start": {"latLng": "39.7392°, -104.9903°"},
                    "end": {"latLng": "39.7492°, -105.0103°"},
                    "distanceMeters": 1234.6,
                    "topCandidate": {"type": "IN_PASSENGER_VEHICLE"}
                  }
                },
                {
                  "startTime": "2026-08-10T09:00:00.000-06:00",
                  "endTime": "2026-08-10T10:00:00.000-06:00",
                  "timelinePath": []
                }
              ],
              "rawSignals": [{"ignored": true}]
            }
        """
    }
}
