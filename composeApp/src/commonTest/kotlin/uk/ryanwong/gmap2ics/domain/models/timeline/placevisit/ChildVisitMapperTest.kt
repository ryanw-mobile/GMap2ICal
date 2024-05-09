/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline.placevisit

import com.esri.core.geometry.Polygon
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.utils.timezonemap.fakes.FakeTimeZoneMap
import uk.ryanwong.gmap2ics.data.models.timeline.DurationDto
import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.timeline.Location
import us.dustinj.timezonemap.TimeZone

internal class ChildVisitMapperTest : FreeSpec() {

    private lateinit var fakeTimeZoneMap: FakeTimeZoneMap

    private val childVisitDto = uk.ryanwong.gmap2ics.data.models.timeline.ChildVisitDto(
        centerLatE7 = null,
        centerLngE7 = null,
        duration = DurationDto(endTimestamp = "2022-01-03T14:26:25Z", startTimestamp = "2022-01-03T14:18:02Z"),
        lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
        location = uk.ryanwong.gmap2ics.data.models.timeline.LocationDto(
            address = "some-address",
            latitudeE7 = 534781060,
            locationConfidence = 100.0,
            longitudeE7 = -22666767,
            name = "some-name",
            placeId = "some-place-id",
        ),
        locationConfidence = 100,
        placeConfidence = "USER_CONFIRMED",
        placeVisitType = "SINGLE_PLACE",
        visitConfidence = 100,
    )

    init {
        "should correctly map Data Model to Domain Model" {
            fakeTimeZoneMap = FakeTimeZoneMap().apply {
                zoneId = "Europe/London"
            }
            val childVisitDataModel = childVisitDto
            val expectedDomainModel = ChildVisit(
                durationEndTimestamp = RawTimestamp(timestamp = "2022-01-03T14:26:25Z", timezoneId = "Europe/London"),
                durationStartTimestamp = RawTimestamp(timestamp = "2022-01-03T14:18:02Z", timezoneId = "Europe/London"),
                lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
                location = Location(
                    placeId = "some-place-id",
                    latitudeE7 = 534781060,
                    longitudeE7 = -22666767,
                    name = "some-name",
                    address = "some-address",
                ),
                eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon()),
            )

            val childVisitDomainModel = childVisitDataModel.toDomainModel(timeZoneMap = fakeTimeZoneMap)

            childVisitDomainModel shouldBe expectedDomainModel
        }

        "should return null if Domain Model has no valid Location" {
            fakeTimeZoneMap = FakeTimeZoneMap()
            val childVisitDataModel = childVisitDto.copy(
                location = uk.ryanwong.gmap2ics.data.models.timeline.LocationDto(),
            )

            val childVisitDomainModel = childVisitDataModel.toDomainModel(timeZoneMap = fakeTimeZoneMap)

            childVisitDomainModel shouldBe null
        }

        "should return null if Domain Model has no valid Duration" {
            fakeTimeZoneMap = FakeTimeZoneMap()
            val childVisitDataModel = childVisitDto.copy(
                duration = null,
            )

            val childVisitDomainModel = childVisitDataModel.toDomainModel(timeZoneMap = fakeTimeZoneMap)

            childVisitDomainModel shouldBe null
        }
    }
}
