/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.impl

import com.esri.core.geometry.Polygon
import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.LatLng
import uk.ryanwong.gmap2ics.domain.models.timeline.Location
import uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.ChildVisit
import uk.ryanwong.gmap2ics.domain.models.timeline.placevisit.PlaceVisit
import us.dustinj.timezonemap.TimeZone

internal object GetPlaceVisitVEventUseCaseImplTestData {
    private const val SOME_LATITUDE_E7 = 263383300
    private const val SOME_LONGITUDE_E7 = 1278000000
    private const val SOME_DEGREES_LATITUDE = 26.3383300
    private const val SOME_DEGREES_LONGITUDE = 127.8000000

    val mockPlaceVisit by lazy {
        PlaceVisit(
            // meaningless values just to match the format
            durationEndTimestamp = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            durationStartTimestamp = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
            location = Location(
                placeId = "some-place-id",
                latitudeE7 = SOME_LATITUDE_E7,
                longitudeE7 = SOME_LONGITUDE_E7,
            ),
            childVisits = emptyList(),
            eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon()),
        )
    }

    val mockVEvent by lazy {
        VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "\uD83C\uDFE7 some-place-name",
            location = "some-formatted-address",
            geo = LatLng(latitude = SOME_DEGREES_LATITUDE, longitude = SOME_DEGREES_LONGITUDE),
            description = "Place ID:\\nsome-place-id\\n\\nGoogle Maps URL:\\nhttps://some.url/",
            url = "https://some.url/",
            lastModified = "2011-11-11T11:22:22.222Z",
        )
    }

    val mockPlaceVisitWithOneChildVisit by lazy {
        PlaceVisit(
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
            childVisits = listOf(mockChildVisitSomeChildPlaceId),
            eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon()),
        )
    }

    private val mockChildVisitSomeChildPlaceId by lazy {
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
        )
    }

    val mockVEventSomeChildPlaceId by lazy {
        VEvent(
            uid = "2022-02-20T01:17:06.535Z",
            placeId = "some-child-place-id",
            dtStamp = "2022-02-20T01:17:06.535Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2022-01-03T14:19:00Z", timezoneId = "Europe/London"),
            dtEnd = RawTimestamp(timestamp = "2022-01-03T14:25:00Z", timezoneId = "Europe/London"),
            summary = "📍 some-child-name",
            location = "some-child-address",
            geo = LatLng(latitude = 53.478106999999994, longitude = -2.266677),
            description = "Place ID:\nsome-child-place-id\n\nGoogle Maps URL:\nhttps://www.google.com/maps/place/?q=place_id:some-child-place-id, url=https://www.google.com/maps/place/?q=place_id:some-child-place-id",
            lastModified = "2022-02-20T01:17:06.535Z",
        )
    }

    private val mockChildVisitSomeOtherChildPlaceId by lazy {
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
                placeId = "some-another-child-place-id",
                latitudeE7 = 534781070,
                longitudeE7 = -22666770,
                name = "some-another-child-name",
                address = "some-another-child-address",
            ),
            eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon()),
        )
    }

    val mockPlaceVisitWithTwoChildVisit by lazy {
        PlaceVisit(
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
                mockChildVisitSomeOtherChildPlaceId,
                mockChildVisitSomeChildPlaceId,
            ),
            eventTimeZone = TimeZone(zoneId = "Europe/London", region = Polygon()),
        )
    }
}
