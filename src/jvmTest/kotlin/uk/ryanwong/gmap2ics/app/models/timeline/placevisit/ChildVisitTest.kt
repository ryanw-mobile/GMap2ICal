/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.placevisit

import com.esri.core.geometry.Polygon
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.utils.timezonemap.MockTimeZoneMap
import uk.ryanwong.gmap2ics.data.models.timeline.Duration
import us.dustinj.timezonemap.TimeZone

internal class ChildVisitTest : FreeSpec() {

    private lateinit var mockTimeZoneMap: MockTimeZoneMap

    private val mockChildVisit = uk.ryanwong.gmap2ics.data.models.timeline.ChildVisit(
        centerLatE7 = null,
        centerLngE7 = null,
        duration = Duration(endTimestamp = "2022-01-03T14:26:25Z", startTimestamp = "2022-01-03T14:18:02Z"),
        lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
        location = uk.ryanwong.gmap2ics.data.models.timeline.Location(
            address = "some-address",
            latitudeE7 = 534781060,
            locationConfidence = 100.0,
            longitudeE7 = -22666767,
            name = "some-name",
            placeId = "some-place-id"
        ),
        locationConfidence = 100,
        placeConfidence = "USER_CONFIRMED",
        placeVisitType = "SINGLE_PLACE",
        visitConfidence = 100
    )

    init {
        "should correctly map Data Model to App Model" {
            // 🔴 Given
            mockTimeZoneMap = MockTimeZoneMap().apply {
                mockZoneId = "Europe/London"
            }
            val childVisitDataModel = mockChildVisit

            // 🟡 When
            val childVisitAppModel =
                ChildVisit.from(childVisitDataModel = childVisitDataModel, timeZoneMap = mockTimeZoneMap)

            // 🟢 Then
            childVisitAppModel shouldBe ChildVisit(
                durationEndTimestamp = "2022-01-03T14:26:25Z",
                durationStartTimestamp = "2022-01-03T14:18:02Z",
                lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
                location = Location(
                    placeId = "some-place-id",
                    latitudeE7 = 534781060,
                    longitudeE7 = -22666767,
                    name = "some-name",
                    address = "some-address"
                ),
                eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon())
            )
        }

        "should return null if App Model has no valid Location" {
            // 🔴 Given
            mockTimeZoneMap = MockTimeZoneMap()
            val childVisitDataModel = mockChildVisit.copy(
                location = uk.ryanwong.gmap2ics.data.models.timeline.Location()
            )

            // 🟡 When
            val childVisitAppModel =
                ChildVisit.from(childVisitDataModel = childVisitDataModel, timeZoneMap = mockTimeZoneMap)

            // 🟢 Then
            childVisitAppModel shouldBe null
        }

        "should return null if App Model has no valid Duration" {
            // 🔴 Given
            mockTimeZoneMap = MockTimeZoneMap()
            val childVisitDataModel = mockChildVisit.copy(
                duration = null
            )

            // 🟡 When
            val childVisitAppModel =
                ChildVisit.from(childVisitDataModel = childVisitDataModel, timeZoneMap = mockTimeZoneMap)

            // 🟢 Then
            childVisitAppModel shouldBe null
        }
    }
}