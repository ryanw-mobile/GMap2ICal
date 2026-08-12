/*
 * Copyright (c) 2022-2026. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.models.timeline

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

internal class TimelineFileDtoTest {

    @Test
    fun `prefers legacy timeline objects when both schemas are present`() {
        val legacyObject = TimelineObjectDto()

        val result = TimelineFileDto(
            timelineObjects = listOf(legacyObject),
            semanticSegments = listOf(SemanticSegmentDto()),
        ).toTimelineObjectsDto()

        val timelineObjects = assertNotNull(result.timelineObjects)
        assertEquals(1, timelineObjects.size)
        assertSame(legacyObject, timelineObjects.single())
    }

    @Test
    fun `skips incomplete and unsupported semantic segments`() {
        assertNull(SemanticSegmentDto().toTimelineObjectDto())
        assertNull(SemanticSegmentDto(startTime = START_TIME).toTimelineObjectDto())
        assertNull(
            SemanticSegmentDto(
                startTime = START_TIME,
                endTime = END_TIME,
            ).toTimelineObjectDto(),
        )
    }

    @Test
    fun `skips visits without a candidate or usable coordinates`() {
        assertNull(SemanticVisitDto().toTimelineObjectDto(START_TIME, END_TIME))
        assertNull(
            SemanticVisitDto(
                topCandidate = SemanticPlaceCandidateDto(),
            ).toTimelineObjectDto(START_TIME, END_TIME),
        )
        assertNull(
            SemanticVisitDto(
                topCandidate = SemanticPlaceCandidateDto(
                    placeLocation = LatLngStringDto("not coordinates"),
                ),
            ).toTimelineObjectDto(START_TIME, END_TIME),
        )
    }

    @Test
    fun `maps semantic visit labels to location names`() {
        val expectedNames = listOf(
            "HOME" to "Home",
            "INFERRED_HOME" to "Home",
            "WORK" to "Work",
            "INFERRED_WORK" to "Work",
            "ALIASED_LOCATION" to "Saved place",
            "SEARCHED_ADDRESS" to "Searched address",
            "UNKNOWN" to "Visited place",
            null to "Visited place",
        )

        expectedNames.forEach { (semanticType, expectedName) ->
            val result = SemanticVisitDto(
                topCandidate = SemanticPlaceCandidateDto(
                    placeId = "place-id",
                    semanticType = semanticType,
                    placeLocation = LatLngStringDto("39.7392°, -104.9903°"),
                ),
            ).toTimelineObjectDto(START_TIME, END_TIME)

            val location = assertNotNull(result?.placeVisit).location
            assertEquals(expectedName, location.name)
            assertEquals("place-id", location.placeId)
        }
    }

    @Test
    fun `skips activities without usable start and end coordinates`() {
        assertNull(SemanticActivityDto().toTimelineObjectDto(START_TIME, END_TIME))
        assertNull(
            SemanticActivityDto(
                start = LatLngStringDto("39.7392, -104.9903"),
            ).toTimelineObjectDto(START_TIME, END_TIME),
        )
        assertNull(
            SemanticActivityDto(
                start = LatLngStringDto("39.7392, -104.9903"),
                end = LatLngStringDto("invalid"),
            ).toTimelineObjectDto(START_TIME, END_TIME),
        )
    }

    @Test
    fun `maps an activity when optional metadata is absent`() {
        val result = SemanticActivityDto(
            start = LatLngStringDto("39.7392, -104.9903"),
            end = LatLngStringDto("39.7492, -105.0103"),
            topCandidate = SemanticActivityCandidateDto(),
        ).toTimelineObjectDto(START_TIME, END_TIME)

        val activity = assertNotNull(result?.activitySegment)
        assertNull(activity.activityType)
        assertNull(activity.activities)
        assertNull(activity.distance)
        assertEquals(397392000, activity.startLocation.latitudeE7)
        assertEquals(-1050103000, activity.endLocation.longitudeE7)
    }

    @Test
    fun `parses coordinate boundaries and rejects invalid coordinate strings`() {
        assertEquals(
            E7Coordinates(latitudeE7 = -900000000, longitudeE7 = 1800000000),
            LatLngStringDto("-90°, 180°").toCoordinates(),
        )

        listOf(
            LatLngStringDto(),
            LatLngStringDto("not coordinates"),
            LatLngStringDto("91, 0"),
            LatLngStringDto("0, -181"),
        ).forEach { coordinates ->
            assertNull(coordinates.toCoordinates())
        }
    }

    private companion object {
        const val START_TIME = "2026-08-10T08:30:00.000-06:00"
        const val END_TIME = "2026-08-10T09:00:00.000-06:00"
    }
}
