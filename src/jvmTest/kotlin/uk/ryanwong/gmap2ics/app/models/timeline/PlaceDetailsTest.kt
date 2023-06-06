/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetailsTestData.mockPlaceDetailsResult

internal class PlaceDetailsTest : FreeSpec() {

    init {
        "should convert correctly from data.models.places.Result to PLaceDetails" {
            // 🔴 Given
            val placeDetailsResult = mockPlaceDetailsResult

            // 🟡 When
            val placeDetails = PlaceDetails.from(placeDetailsResult = placeDetailsResult)

            // 🟢 Then
            placeDetails shouldBe PlaceDetails(
                placeId = "ChIJOX4_ortjeUgR2_LUcFpvLg4",
                name = "Yorkshire Sculpture Park",
                formattedAddress = "Wakefield WF4 4LG, UK",
                geo = LatLng(latitude = 53.6152405, longitude = -1.5639315),
                types = listOf(
                    "art_gallery",
                    "tourist_attraction",
                    "park",
                    "museum",
                    "point_of_interest",
                    "establishment",
                ),
                url = "https://maps.google.com/?cid=1021876599690425051",
            )
        }

        "getFormattedName" - {
            "should format the name with a correct emoji if PlaceType is known" {
                // 🔴 Given
                val placeDetails = PlaceDetails(
                    placeId = "some-place-id",
                    name = "some-name",
                    formattedAddress = "some-formatted-address",
                    geo = LatLng(latitude = 53.6152405, longitude = -1.5639315),
                    types = listOf(
                        "art_gallery",
                        "tourist_attraction",
                        "park",
                        "museum",
                        "point_of_interest",
                        "establishment",
                    ),
                    url = "https://maps.google.com/?cid=1021876599690425051",
                )

                // 🟡 When
                val formattedName = placeDetails.getFormattedName()

                // 🟢 Then
                formattedName shouldBe "\uD83D\uDDBC some-name"
            }

            "should format the name with a default emoji for unknown PlaceType" {
                // 🔴 Given
                val placeDetails = PlaceDetails(
                    placeId = "some-place-id",
                    name = "some-name",
                    formattedAddress = "some-formatted-address",
                    geo = LatLng(latitude = 53.6152405, longitude = -1.5639315),
                    types = listOf(
                        "some-unknown-type",
                    ),
                    url = "https://maps.google.com/?cid=1021876599690425051",
                )

                // 🟡 When
                val formattedName = placeDetails.getFormattedName()

                // 🟢 Then
                formattedName shouldBe "\uD83D\uDCCD some-name"
            }
        }
    }
}
