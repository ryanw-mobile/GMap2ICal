/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.utils.timezonemap.fakes.FakeTimeZoneMap
import uk.ryanwong.gmap2ics.data.models.timeline.TimelineObjects
import uk.ryanwong.gmap2ics.domain.models.timeline.TimelineTestData.timelineObjects

internal class TimelineTest : FreeSpec() {

    private lateinit var fakeTimeZoneMap: FakeTimeZoneMap

    init {
        "should return correct Timeline" {
            fakeTimeZoneMap = FakeTimeZoneMap().apply {
                zoneId = "Europe/London"
            }
            val timelineObjects = timelineObjects

            val timeline = Timeline.from(timelineObjects = timelineObjects, timeZoneMap = fakeTimeZoneMap)

            timeline shouldBe TimelineTestData.timeline
        }

        "should still return Timeline with empty list if timelineObjects is null" {
            fakeTimeZoneMap = FakeTimeZoneMap().apply {
                zoneId = "Europe/London"
            }
            val timelineObjects = TimelineObjects(timelineObjects = null)

            val timeline = Timeline.from(timelineObjects = timelineObjects, timeZoneMap = fakeTimeZoneMap)

            timeline shouldBe Timeline(timelineEntries = emptyList())
        }
    }
}
