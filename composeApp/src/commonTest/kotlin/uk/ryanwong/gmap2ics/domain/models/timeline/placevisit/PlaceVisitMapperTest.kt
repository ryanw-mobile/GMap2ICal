/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline.placevisit

import com.esri.core.geometry.Polygon
import uk.ryanwong.gmap2ics.app.utils.timezonemap.fakes.FakeTimeZoneMap
import uk.ryanwong.gmap2ics.data.models.timeline.PlaceVisitDtoTestData.placeVisitDto
import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.timeline.Location
import us.dustinj.timezonemap.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class PlaceVisitMapperTest {

    private lateinit var fakeTimeZoneMap: FakeTimeZoneMap

    @Test
    fun `PlaceVisitMapper should correctly map Data Model to Domain Model`() {
        fakeTimeZoneMap = FakeTimeZoneMap().apply {
            zoneId = "Europe/London"
        }
        val placeVisitDataModel = placeVisitDto
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

        val placeVisitDomainModel = placeVisitDataModel.toDomainModel(timeZoneMap = fakeTimeZoneMap)
        assertEquals(expectedDomainModel, placeVisitDomainModel)
    }

    @Test
    fun `PlaceVisitMapper should still correctly map Data Model to Domain Model if no child visits`() {
        fakeTimeZoneMap = FakeTimeZoneMap().apply {
            zoneId = "Europe/London"
        }
        val placeVisitDataModel = placeVisitDto.copy(
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

        val placeVisitDomainModel = placeVisitDataModel.toDomainModel(timeZoneMap = fakeTimeZoneMap)
        assertEquals(expectedDomainModel, placeVisitDomainModel)
    }

    @Test
    fun `PlaceVisitMapper should return null if Domain Model has no valid Location`() {
        fakeTimeZoneMap = FakeTimeZoneMap()
        val placeVisitDataModel = placeVisitDto.copy(
            location = uk.ryanwong.gmap2ics.data.models.timeline.LocationDto(),
        )

        val placeVisitDomainModel = placeVisitDataModel.toDomainModel(timeZoneMap = fakeTimeZoneMap)
        assertNull(placeVisitDomainModel)
    }
}
