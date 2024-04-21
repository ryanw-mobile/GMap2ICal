/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline.placevisit

import com.esri.core.geometry.Polygon
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.utils.timezonemap.mocks.MockTimeZoneMap
import uk.ryanwong.gmap2ics.data.models.timeline.PlaceVisitDataModelTestData.mockPlaceVisitDataModel
import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.timeline.Location
import us.dustinj.timezonemap.TimeZone

internal class PlaceVisitMapperTest : FreeSpec() {

    private lateinit var mockTimeZoneMap: MockTimeZoneMap

    init {
        "should correctly map Data Model to Domain Model" {
            mockTimeZoneMap = MockTimeZoneMap().apply {
                mockZoneId = "Europe/London"
            }
            val placeVisitDataModel = mockPlaceVisitDataModel
            val expectedDomainModel = PlaceVisit(
                durationEndTimestamp = RawTimestamp(
                    timestamp = "2022-01-03T14:26:25Z",
                    timezoneId = "Europe/London",
                ),
                durationStartTimestamp = RawTimestamp(
                    timestamp = "2022-01-03T14:18:02Z",
                    timezoneId = "Europe/London",
                ),
                lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
                location = Location(
                    placeId = "some-place-id",
                    latitudeE7 = 534781060,
                    longitudeE7 = -22666767,
                    name = "some-name",
                    address = "some-address",
                ),
                childVisits = listOf(
                    ChildVisit(
                        durationEndTimestamp = RawTimestamp(
                            timestamp = "2022-01-03T14:25:00Z",
                            timezoneId = "Europe/London",
                        ),
                        durationStartTimestamp = RawTimestamp(
                            timestamp = "2022-01-03T14:19:00Z",
                            timezoneId = "Europe/London",
                        ),
                        lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
                        location = Location(
                            placeId = "some-child-place-id",
                            latitudeE7 = 534781070,
                            longitudeE7 = -22666770,
                            name = "some-child-name",
                            address = "some-child-address",
                        ),
                        eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon()),
                    ),
                ),
                eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon()),
            )

            val placeVisitDomainModel = placeVisitDataModel.toDomainModel(timeZoneMap = mockTimeZoneMap)

            placeVisitDomainModel shouldBe expectedDomainModel
        }

        "should still correctly map Data Model to Domain Model if no child visits" {
            mockTimeZoneMap = MockTimeZoneMap().apply {
                mockZoneId = "Europe/London"
            }
            val placeVisitDataModel = mockPlaceVisitDataModel.copy(
                childVisits = null,
            )
            val expectedDomainModel = PlaceVisit(
                durationEndTimestamp = RawTimestamp(
                    timestamp = "2022-01-03T14:26:25Z",
                    timezoneId = "Europe/London",
                ),
                durationStartTimestamp = RawTimestamp(
                    timestamp = "2022-01-03T14:18:02Z",
                    timezoneId = "Europe/London",
                ),
                lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
                location = Location(
                    placeId = "some-place-id",
                    latitudeE7 = 534781060,
                    longitudeE7 = -22666767,
                    name = "some-name",
                    address = "some-address",
                ),
                childVisits = emptyList(),
                eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon()),
            )

            val placeVisitDomainModel = placeVisitDataModel.toDomainModel(timeZoneMap = mockTimeZoneMap)

            placeVisitDomainModel shouldBe expectedDomainModel
        }

        "should return null if Domain Model has no valid Location" {
            mockTimeZoneMap = MockTimeZoneMap()
            val placeVisitDataModel = mockPlaceVisitDataModel.copy(
                location = uk.ryanwong.gmap2ics.data.models.timeline.Location(),
            )

            val placeVisitDomainModel = placeVisitDataModel.toDomainModel(timeZoneMap = mockTimeZoneMap)

            placeVisitDomainModel shouldBe null
        }
    }
}
