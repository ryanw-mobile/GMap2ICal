/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models

import uk.ryanwong.gmap2ics.domain.models.timeline.LatLng
import uk.ryanwong.gmap2ics.domain.models.timeline.Location
import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ActivitySegmentFormatterTest {
    /***
     * Test plan: Simply to protect changes by matching the string outputs.
     */
    @Test
    fun `formats route text using startPlaceDetails when startPlaceDetails is not null and endPlaceDetails is null`() {
        val startPlaceDetails = PlaceDetails(
            placeId = "some-place-id",
            name = "some-start-name",
            formattedAddress = "some-formatted-address",
            geo = LatLng(
                latitude = 12.1234567,
                longitude = 123.1234567,
            ),
            types = emptyList(),
            url = "https://some.url/",
        )
        val endPlaceDetails = null
        val startLocation = "some-start-location"
        val endLocation = "some-end-location"

        val routeText = ActivitySegmentFormatter.parseActivityRouteText(
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
            startLocation = startLocation,
            endLocation = endLocation,
        )

        assertEquals("(some-start-name ➡ null)", routeText)
    }

    @Test
    fun `formats route text using endPlaceDetails when endPlaceDetails is not null and startPlaceDetails is null`() {
        val startPlaceDetails = null
        val endPlaceDetails = PlaceDetails(
            placeId = "some-place-id",
            name = "some-end-name",
            formattedAddress = "some-formatted-address",
            geo = LatLng(
                latitude = 12.1234567,
                longitude = 123.1234567,
            ),
            types = emptyList(),
            url = "https://some.url/",
        )
        val startLocation = "some-start-location"
        val endLocation = "some-end-location"

        val routeText = ActivitySegmentFormatter.parseActivityRouteText(
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
            startLocation = startLocation,
            endLocation = endLocation,
        )

        assertEquals("(null ➡ some-end-name)", routeText)
    }

    @Test
    fun `formats route text using startPlaceDetails and endPlaceDetails when both are not null`() {
        val startPlaceDetails = PlaceDetails(
            placeId = "some-place-id",
            name = "some-start-name",
            formattedAddress = "some-formatted-address",
            geo = LatLng(
                latitude = 12.1234567,
                longitude = 123.1234567,
            ),
            types = emptyList(),
            url = "https://some.url/",
        )
        val endPlaceDetails = PlaceDetails(
            placeId = "some-place-id",
            name = "some-end-name",
            formattedAddress = "some-formatted-address",
            geo = LatLng(
                latitude = 12.1234567,
                longitude = 123.1234567,
            ),
            types = emptyList(),
            url = "https://some.url/",
        )
        val startLocation = "some-start-location"
        val endLocation = "some-end-location"

        val routeText = ActivitySegmentFormatter.parseActivityRouteText(
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
            startLocation = startLocation,
            endLocation = endLocation,
        )

        assertEquals("(some-start-name ➡ some-end-name)", routeText)
    }

    @Test
    fun `formats route text using startLocation and endLocation when both PlaceDetails are null`() {
        val startPlaceDetails = null
        val endPlaceDetails = null
        val startLocation = "some-start-location"
        val endLocation = "some-end-location"

        val routeText = ActivitySegmentFormatter.parseActivityRouteText(
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
            startLocation = startLocation,
            endLocation = endLocation,
        )

        assertEquals("(some-start-location ➡ some-end-location)", routeText)
    }

    @Test
    fun `returns empty string when all arguments are null`() {
        val startPlaceDetails = null
        val endPlaceDetails = null
        val startLocation = null
        val endLocation = null

        val routeText = ActivitySegmentFormatter.parseActivityRouteText(
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
            startLocation = startLocation,
            endLocation = endLocation,
        )

        assertEquals("", routeText)
    }

    @Test
    fun `formats timeline description using startPlaceDetails when startPlaceDetails is not null`() {
        val startPlaceDetails = PlaceDetails(
            placeId = "some-place-id",
            name = "some-start-name",
            formattedAddress = "some-formatted-address",
            geo = LatLng(
                latitude = 12.1234567,
                longitude = 123.1234567,
            ),
            types = emptyList(),
            url = "https://some.url/",
        )
        val endPlaceDetails = null
        val startLocationText = "some-start-location-text"
        val endLocationText = "some-end-location-text"

        val timelineDescription = ActivitySegmentFormatter.parseTimelineDescription(
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
            startLocationText = startLocationText,
            endLocationText = endLocationText,
        )

        assertEquals("some-start-location-textsome-end-location-textFirst segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n", timelineDescription)
    }

    @Test
    fun `formats timeline description using endPlaceDetails when endPlaceDetails is not null`() {
        val startPlaceDetails = null
        val endPlaceDetails = PlaceDetails(
            placeId = "some-place-id",
            name = "some-end-name",
            formattedAddress = "some-formatted-address",
            geo = LatLng(
                latitude = 12.1234567,
                longitude = 123.1234567,
            ),
            types = emptyList(),
            url = "https://some.url/",
        )
        val startLocationText = "some-start-location-text"
        val endLocationText = "some-end-location-text"

        val timelineDescription = ActivitySegmentFormatter.parseTimelineDescription(
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
            startLocationText = startLocationText,
            endLocationText = endLocationText,
        )

        assertEquals("some-start-location-textsome-end-location-textLast segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n", timelineDescription)
    }

    @Test
    fun `formats timeline description using startPlaceDetails and endPlaceDetails when both are not null`() {
        val startPlaceDetails = PlaceDetails(
            placeId = "some-place-id",
            name = "some-start-name",
            formattedAddress = "some-formatted-address",
            geo = LatLng(
                latitude = 12.1234567,
                longitude = 123.1234567,
            ),
            types = emptyList(),
            url = "https://some.url/",
        )
        val endPlaceDetails = PlaceDetails(
            placeId = "some-place-id",
            name = "some-end-name",
            formattedAddress = "some-formatted-address",
            geo = LatLng(
                latitude = 12.1234567,
                longitude = 123.1234567,
            ),
            types = emptyList(),
            url = "https://some.url/",
        )
        val startLocationText = "some-start-location-text"
        val endLocationText = "some-end-location-text"

        val timelineDescription = ActivitySegmentFormatter.parseTimelineDescription(
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
            startLocationText = startLocationText,
            endLocationText = endLocationText,
        )

        assertEquals("some-start-location-textsome-end-location-textFirst segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\nLast segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n", timelineDescription)
    }

    @Test
    fun `formats timeline description using startLocation and endLocation when both PlaceDetails are null`() {
        val startPlaceDetails = null
        val endPlaceDetails = null
        val startLocationText = "some-start-location-text"
        val endLocationText = "some-end-location-text"

        val timelineDescription = ActivitySegmentFormatter.parseTimelineDescription(
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
            startLocationText = startLocationText,
            endLocationText = endLocationText,
        )

        assertEquals("some-start-location-textsome-end-location-text", timelineDescription)
    }

    @Test
    fun `formats start location text using placeDetails when placeDetails is not null`() {
        val startLocation = Location(
            placeId = "some-place-id",
            latitudeE7 = 121234567,
            longitudeE7 = 1231234567,
            name = "some-start-location-name",
            address = "some-start-address",
        )
        val placeDetails = PlaceDetails(
            placeId = "some-place-id",
            name = "some-start-name",
            formattedAddress = "some-formatted-address",
            geo = LatLng(
                latitude = 12.1234567,
                longitude = 123.1234567,
            ),
            types = emptyList(),
            url = "https://some.url/",
        )

        val startLocationText = ActivitySegmentFormatter.getStartLocationText(
            startLocation = startLocation,
            placeDetails = placeDetails,
        )

        assertEquals("Start Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n", startLocationText)
    }

    @Test
    fun `formats start location text using startLocation when placeDetails is null`() {
        val startLocation = Location(
            placeId = "some-place-id",
            latitudeE7 = 121234567,
            longitudeE7 = 1231234567,
            name = "some-start-location-name",
            address = "some-start-address",
        )
        val placeDetails = null

        val startLocationText = ActivitySegmentFormatter.getStartLocationText(
            startLocation = startLocation,
            placeDetails = placeDetails,
        )

        assertEquals("Start Location: 12.123457,123.123457\\nhttps://maps.google.com?q=12.123457,123.123457\\n\\n", startLocationText)
    }

    @Test
    fun `formats end location text using placeDetails when placeDetails is not null`() {
        val endLocation = Location(
            placeId = "some-place-id",
            latitudeE7 = 121234567,
            longitudeE7 = 1231234567,
            name = "some-end-location-name",
            address = "some-end-address",
        )
        val placeDetails = PlaceDetails(
            placeId = "some-place-id",
            name = "some-start-name",
            formattedAddress = "some-formatted-address",
            geo = LatLng(
                latitude = 12.1234567,
                longitude = 123.1234567,
            ),
            types = emptyList(),
            url = "https://some.url/",
        )

        val endLocationText = ActivitySegmentFormatter.getEndLocationText(
            endLocation = endLocation,
            placeDetails = placeDetails,
        )

        assertEquals("End Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n", endLocationText)
    }

    @Test
    fun `formats end location text using endLocation when placeDetails is null`() {
        val endLocation = Location(
            placeId = "some-place-id",
            latitudeE7 = 121234567,
            longitudeE7 = 1231234567,
            name = "some-end-location-name",
            address = "some-end-address",
        )
        val placeDetails = null

        val endLocationText = ActivitySegmentFormatter.getEndLocationText(
            endLocation = endLocation,
            placeDetails = placeDetails,
        )

        assertEquals("End Location: 12.123457,123.123457\\nhttps://maps.google.com?q=12.123457,123.123457\\n\\n", endLocationText)
    }

    @Test
    fun `returns correct miles when converting kilometers to miles`() {
        val meters = 3000.0
        val miles = ActivitySegmentFormatter.kilometersToMiles(meters = meters)
        assertEquals(1863.0, miles)
    }
}
