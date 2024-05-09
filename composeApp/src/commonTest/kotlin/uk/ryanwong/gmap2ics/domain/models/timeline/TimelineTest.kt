/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.utils.timezonemap.fakes.FakeTimeZoneMap
import uk.ryanwong.gmap2ics.data.models.timeline.TimelineObjectsDto
import uk.ryanwong.gmap2ics.domain.models.timeline.TimelineTestData.timelineObjectsDto

internal class TimelineTest : FreeSpec() {

    private lateinit var fakeTimeZoneMap: FakeTimeZoneMap

    init {
        "should return correct Timeline" {
            fakeTimeZoneMap = FakeTimeZoneMap().apply {
                zoneId = "Europe/London"
            }
            val timelineObjects = timelineObjectsDto

            val timeline = Timeline.from(timelineObjectsDto = timelineObjects, timeZoneMap = fakeTimeZoneMap)

            timeline shouldBe TimelineTestData.timeline
        }

        "should still return Timeline with empty list if timelineObjects is null" {
            fakeTimeZoneMap = FakeTimeZoneMap().apply {
                zoneId = "Europe/London"
            }
            val timelineObjectsDto = TimelineObjectsDto(timelineObjectDtos = null)

            val timeline = Timeline.from(timelineObjectsDto = timelineObjectsDto, timeZoneMap = fakeTimeZoneMap)

            timeline shouldBe Timeline(timelineEntries = emptyList())
        }
    }
}
