/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.models.timeline

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class LocationTest : FreeSpec() {
    init {
        "should return correct formatted values" {
            // 🔴 Given
            val location = Location(
                placeId = "some-place-id",
                latitudeE7 = 343970563,
                longitudeE7 = 1324677422,
                name = "some-name",
                address = "some-address",
            )

            // 🟡 When - extra variables to improve readability
            val latitude = location.getLatitude()
            val longitude = location.getLongitude()
            val formattedLatLng = location.getFormattedLatLng()
            val googleMapsLatLngLink = location.getGoogleMapsLatLngLink()
            val googleMapsPlaceIdLink = location.getGoogleMapsPlaceIdLink()

            // 🟢 Then
            latitude shouldBe 34.3970563
            longitude shouldBe 132.4677422
            formattedLatLng shouldBe "34.397056,132.467742"
            googleMapsLatLngLink shouldBe "https://maps.google.com?q=34.397056,132.467742"
            googleMapsPlaceIdLink shouldBe "https://www.google.com/maps/place/?q=place_id:some-place-id"
        }

        "getGoogleMapsPlaceIdLink" - {
            "Should return empty string if placeId is null" {
                // 🔴 Given
                val location = Location(
                    placeId = null,
                    latitudeE7 = 1,
                    longitudeE7 = 2,
                    name = "some-name",
                    address = "some-address",
                )

                // 🟡 When - extra variables to improve readability
                val googleMapsPlaceIdLink = location.getGoogleMapsPlaceIdLink()

                // 🟢 Then
                googleMapsPlaceIdLink shouldBe ""
            }
        }
    }
}
