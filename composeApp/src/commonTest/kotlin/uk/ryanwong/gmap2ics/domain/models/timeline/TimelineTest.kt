/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import uk.ryanwong.gmap2ics.app.utils.timezonemap.fakes.FakeTimeZoneMap
import uk.ryanwong.gmap2ics.data.models.timeline.TimelineObjectsDto
import uk.ryanwong.gmap2ics.domain.models.timeline.TimelineTestData.timelineObjectsDto
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TimelineTest {

    private lateinit var fakeTimeZoneMap: FakeTimeZoneMap

    @Test
    fun `Timeline should return correct Timeline`() {
        fakeTimeZoneMap = FakeTimeZoneMap().apply {
            zoneId = "Europe/London"
        }
        val timelineObjects = timelineObjectsDto

        val timeline = Timeline.from(timelineObjectsDto = timelineObjects, timeZoneMap = fakeTimeZoneMap)
        assertEquals(TimelineTestData.timeline, timeline)
    }

    @Test
    fun `Timeline should still return Timeline with empty list if timelineObjects is null`() {
        fakeTimeZoneMap = FakeTimeZoneMap().apply {
            zoneId = "Europe/London"
        }
        val timelineObjectsDto = TimelineObjectsDto(timelineObjects = null)

        val timeline = Timeline.from(timelineObjectsDto = timelineObjectsDto, timeZoneMap = fakeTimeZoneMap)
        assertEquals(Timeline(timelineEntries = emptyList()), timeline)
    }
}
