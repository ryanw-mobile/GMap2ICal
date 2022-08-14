/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class LocationTest : FreeSpec() {
    init {
        "from Location Data Model" - {
            "should convert locationDataModel to Location App Model correctly" - {
                // 🔴 Given
                val locationDataModel = uk.ryanwong.gmap2ics.data.models.timeline.Location(
                    address = "some-address",
                    latitudeE7 = 343970563,
                    longitudeE7 = 1324677422,
                    locationConfidence = 0.987654,
                    name = "some-name",
                    placeId = "some-place-id"
                )

                // 🟡 When
                val locationAppModel = Location.from(locationDataModel = locationDataModel)

                // 🟢 Then
                locationAppModel shouldBe Location(
                    placeId = "some-place-id",
                    latitudeE7 = 343970563,
                    longitudeE7 = 1324677422,
                    name = "some-name",
                    address = "some-address",
                )
            }

            "should return null if locationDataModel does not contain latitudeE7" {
                // 🔴 Given
                val locationDataModel = uk.ryanwong.gmap2ics.data.models.timeline.Location(
                    address = "some-address",
                    latitudeE7 = null,
                    longitudeE7 = 1324677422,
                    locationConfidence = 0.987654,
                    name = "some-name",
                    placeId = "some-place-id"
                )

                // 🟡 When
                val locationAppModel = Location.from(locationDataModel = locationDataModel)

                // 🟢 Then
                locationAppModel shouldBe null
            }

            "should return null if locationDataModel does not contain longitudeE7" {
                // 🔴 Given
                val locationDataModel = uk.ryanwong.gmap2ics.data.models.timeline.Location(
                    address = "some-address",
                    latitudeE7 = 343970563,
                    longitudeE7 = null,
                    locationConfidence = 0.987654,
                    name = "some-name",
                    placeId = "some-place-id"
                )

                // 🟡 When
                val locationAppModel = Location.from(locationDataModel = locationDataModel)

                // 🟢 Then
                locationAppModel shouldBe null
            }
        }

        "from ActivityLocation Data Model" - {
            "should convert activityLocationDataModel to Location App Model correctly" - {
                // 🔴 Given
                val activityLocationDataModel = uk.ryanwong.gmap2ics.data.models.timeline.ActivityLocation(
                    address = "some-address",
                    latitudeE7 = 343970563,
                    longitudeE7 = 1324677422,
                    locationConfidence = 0.987654,
                    name = "some-name",
                    placeId = "some-place-id"
                )

                // 🟡 When
                val locationAppModel = Location.from(activityLocationDataModel = activityLocationDataModel)

                // 🟢 Then
                locationAppModel shouldBe Location(
                    placeId = "some-place-id",
                    latitudeE7 = 343970563,
                    longitudeE7 = 1324677422,
                    name = "some-name",
                    address = "some-address",
                )
            }


            "should return null if activityLocationDataModel does not contain latitudeE7" {
                // 🔴 Given
                val activityLocationDataModel = uk.ryanwong.gmap2ics.data.models.timeline.ActivityLocation(
                    address = "some-address",
                    latitudeE7 = null,
                    longitudeE7 = 1324677422,
                    locationConfidence = 0.987654,
                    name = "some-name",
                    placeId = "some-place-id"
                )

                // 🟡 When
                val locationAppModel = Location.from(activityLocationDataModel = activityLocationDataModel)

                // 🟢 Then
                locationAppModel shouldBe null
            }

            "should return null if activityLocationDataModel does not contain longitudeE7" {
                // 🔴 Given
                val activityLocationDataModel = uk.ryanwong.gmap2ics.data.models.timeline.ActivityLocation(
                    address = "some-address",
                    latitudeE7 = 343970563,
                    longitudeE7 = null,
                    locationConfidence = 0.987654,
                    name = "some-name",
                    placeId = "some-place-id"
                )

                // 🟡 When
                val locationAppModel = Location.from(activityLocationDataModel = activityLocationDataModel)

                // 🟢 Then
                locationAppModel shouldBe null
            }
        }

        "should return correct formatted values" {
            // 🔴 Given
            val location = Location(
                placeId = "some-place-id",
                latitudeE7 = 343970563,
                longitudeE7 = 1324677422,
                name = "some-name",
                address = "some-address"
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
                    address = "some-address"
                )

                // 🟡 When - extra variables to improve readability
                val googleMapsPlaceIdLink = location.getGoogleMapsPlaceIdLink()

                // 🟢 Then
                googleMapsPlaceIdLink shouldBe ""
            }
        }
    }
}