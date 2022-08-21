/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.placevisit

import com.esri.core.geometry.Polygon
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.utils.timezonemap.MockTimeZoneMap
import uk.ryanwong.gmap2ics.data.models.timeline.PlaceVisitDataModelTestData.mockPlaceVisitDataModel
import us.dustinj.timezonemap.TimeZone

internal class PlaceVisitTest : FreeSpec() {

    private lateinit var mockTimeZoneMap: MockTimeZoneMap

    init {
        "should correctly map Data Model to App Model" {
            // 🔴 Given
            mockTimeZoneMap = MockTimeZoneMap().apply {
                mockZoneId = "Europe/London"
            }
            val placeVisitDataModel = mockPlaceVisitDataModel

            // 🟡 When
            val placeVisitAppModel =
                PlaceVisit.from(placeVisitDataModel = placeVisitDataModel, timeZoneMap = mockTimeZoneMap)

            // 🟢 Then
            placeVisitAppModel shouldBe PlaceVisit(
                durationEndTimestamp = RawTimestamp(
                    timestamp = "2022-01-03T14:26:25Z", timezoneId = "Europe/London"
                ),
                durationStartTimestamp = RawTimestamp(
                    timestamp = "2022-01-03T14:18:02Z", timezoneId = "Europe/London"
                ),
                lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
                location = Location(
                    placeId = "some-place-id",
                    latitudeE7 = 534781060,
                    longitudeE7 = -22666767,
                    name = "some-name",
                    address = "some-address"
                ),
                childVisits = listOf(
                    ChildVisit(
                        durationEndTimestamp = RawTimestamp(
                            timestamp = "2022-01-03T14:25:00Z",
                            timezoneId = "Europe/London"
                        ),
                        durationStartTimestamp = RawTimestamp(
                            timestamp = "2022-01-03T14:19:00Z",
                            timezoneId = "Europe/London"
                        ),
                        lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
                        location = Location(
                            placeId = "some-child-place-id",
                            latitudeE7 = 534781070,
                            longitudeE7 = -22666770,
                            name = "some-child-name", address = "some-child-address"
                        ),
                        eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon())
                    )
                ),
                eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon())
            )
        }

        "should still correctly map Data Model to App Model if no child visits" {
            // 🔴 Given
            mockTimeZoneMap = MockTimeZoneMap().apply {
                mockZoneId = "Europe/London"
            }
            val placeVisitDataModel = mockPlaceVisitDataModel.copy(
                childVisits = null
            )

            // 🟡 When
            val placeVisitAppModel =
                PlaceVisit.from(placeVisitDataModel = placeVisitDataModel, timeZoneMap = mockTimeZoneMap)

            // 🟢 Then
            placeVisitAppModel shouldBe PlaceVisit(
                durationEndTimestamp = RawTimestamp(
                    timestamp = "2022-01-03T14:26:25Z", timezoneId = "Europe/London"
                ),
                durationStartTimestamp = RawTimestamp(
                    timestamp = "2022-01-03T14:18:02Z", timezoneId = "Europe/London"
                ),
                lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
                location = Location(
                    placeId = "some-place-id",
                    latitudeE7 = 534781060,
                    longitudeE7 = -22666767,
                    name = "some-name",
                    address = "some-address"
                ),
                childVisits = emptyList(),
                eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon())
            )
        }

        "should return null if App Model has no valid Location" {
            // 🔴 Given
            mockTimeZoneMap = MockTimeZoneMap()
            val placeVisitDataModel = mockPlaceVisitDataModel.copy(
                location = uk.ryanwong.gmap2ics.data.models.timeline.Location()
            )

            // 🟡 When
            val placeVisitAppModel =
                PlaceVisit.from(placeVisitDataModel = placeVisitDataModel, timeZoneMap = mockTimeZoneMap)

            // 🟢 Then
            placeVisitAppModel shouldBe null
        }
    }
}