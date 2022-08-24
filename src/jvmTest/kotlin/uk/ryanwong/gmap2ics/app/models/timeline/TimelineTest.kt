/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.models.timeline.TimelineTestData.mockTimeline
import uk.ryanwong.gmap2ics.app.models.timeline.TimelineTestData.mockTimelineObjects
import uk.ryanwong.gmap2ics.app.utils.timezonemap.mocks.MockTimeZoneMap
import uk.ryanwong.gmap2ics.data.models.timeline.TimelineObjects

internal class TimelineTest : FreeSpec() {

    private lateinit var mockTimeZoneMap: MockTimeZoneMap

    init {
        "should return correct Timeline" {
            // 🔴 Given
            mockTimeZoneMap = MockTimeZoneMap().apply {
                mockZoneId = "Europe/London"
            }
            val timelineObjects = mockTimelineObjects

            // 🟡 When
            val timeline = Timeline.from(timelineObjects = timelineObjects, timeZoneMap = mockTimeZoneMap)

            // 🟢 Then
            timeline shouldBe mockTimeline
        }

        "should still return Timeline with empty list if timelineObjects is null" {
            // 🔴 Given
            mockTimeZoneMap = MockTimeZoneMap().apply {
                mockZoneId = "Europe/London"
            }
            val timelineObjects = TimelineObjects(timelineObjects = null)

            // 🟡 When
            val timeline = Timeline.from(timelineObjects = timelineObjects, timeZoneMap = mockTimeZoneMap)

            // 🟢 Then
            timeline shouldBe Timeline(timelineEntries = emptyList())
        }
    }
}