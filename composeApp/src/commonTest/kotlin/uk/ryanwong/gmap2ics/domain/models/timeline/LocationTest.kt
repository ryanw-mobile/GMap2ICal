/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import org.junit.Assert.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals

internal class LocationTest {
    @Test
    fun `returns correct formatted values when all parameters are valid`() {
        val location = Location(
            placeId = "some-place-id",
            latitudeE7 = 343970563,
            longitudeE7 = 1324677422,
            name = "some-name",
            address = "some-address",
        )

        // extra variables to improve readability
        val latitude = location.getLatitude()
        val longitude = location.getLongitude()
        val formattedLatLng = location.getFormattedLatLng()
        val googleMapsLatLngLink = location.getGoogleMapsLatLngLink()
        val googleMapsPlaceIdLink = location.getGoogleMapsPlaceIdLink()

        assertEquals(34.3970563, latitude)
        assertEquals(132.4677422, longitude)
        assertEquals("34.397056,132.467742", formattedLatLng)
        assertEquals("https://maps.google.com?q=34.397056,132.467742", googleMapsLatLngLink)
        assertEquals("https://www.google.com/maps/place/?q=place_id:some-place-id", googleMapsPlaceIdLink)
    }

    @Test
    fun `returns empty string when placeId is null`() {
        val location = Location(
            placeId = null,
            latitudeE7 = 1,
            longitudeE7 = 2,
            name = "some-name",
            address = "some-address",
        )

        // extra variables to improve readability
        val googleMapsPlaceIdLink = location.getGoogleMapsPlaceIdLink()

        assertTrue(googleMapsPlaceIdLink.isEmpty())
    }
}
