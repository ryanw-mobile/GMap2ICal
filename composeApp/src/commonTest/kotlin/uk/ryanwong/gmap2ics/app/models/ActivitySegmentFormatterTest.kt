/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.models

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails

internal class ActivitySegmentFormatterTest : FreeSpec() {
    /***
     * Test plan: Simply to protect changes by matching the string outputs.
     */
    init {
        "parseActivityRouteText" - {
            "should format string using startPlaceDetails if it is not null and endPlaceDetails is null" {
                // 🔴 Given
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

                // 🟡 When
                val routeText = ActivitySegmentFormatter.parseActivityRouteText(
                    startPlaceDetails = startPlaceDetails,
                    endPlaceDetails = endPlaceDetails,
                    startLocation = startLocation,
                    endLocation = endLocation,
                )

                // 🟢 Then
                routeText shouldBe "(some-start-name ➡ null)"
            }

            "should format string using endPlaceDetails if it is not null and startPlaceDetails is null" {
                // 🔴 Given
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

                // 🟡 When
                val routeText = ActivitySegmentFormatter.parseActivityRouteText(
                    startPlaceDetails = startPlaceDetails,
                    endPlaceDetails = endPlaceDetails,
                    startLocation = startLocation,
                    endLocation = endLocation,
                )

                // 🟢 Then
                routeText shouldBe "(null ➡ some-end-name)"
            }

            "should format string using startPlaceDetails and endPlaceDetails if both are not null" {
                // 🔴 Given
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

                // 🟡 When
                val routeText = ActivitySegmentFormatter.parseActivityRouteText(
                    startPlaceDetails = startPlaceDetails,
                    endPlaceDetails = endPlaceDetails,
                    startLocation = startLocation,
                    endLocation = endLocation,
                )

                // 🟢 Then
                routeText shouldBe "(some-start-name ➡ some-end-name)"
            }

            "should format string using startLocation and endLocation if both PlaceDetails are null" {
                // 🔴 Given
                val startPlaceDetails = null
                val endPlaceDetails = null
                val startLocation = "some-start-location"
                val endLocation = "some-end-location"

                // 🟡 When
                val routeText = ActivitySegmentFormatter.parseActivityRouteText(
                    startPlaceDetails = startPlaceDetails,
                    endPlaceDetails = endPlaceDetails,
                    startLocation = startLocation,
                    endLocation = endLocation,
                )

                // 🟢 Then
                routeText shouldBe "(some-start-location ➡ some-end-location)"
            }

            "should return an empty string if all arguments are null" {
                // 🔴 Given
                val startPlaceDetails = null
                val endPlaceDetails = null
                val startLocation = null
                val endLocation = null

                // 🟡 When
                val routeText = ActivitySegmentFormatter.parseActivityRouteText(
                    startPlaceDetails = startPlaceDetails,
                    endPlaceDetails = endPlaceDetails,
                    startLocation = startLocation,
                    endLocation = endLocation,
                )

                // 🟢 Then
                routeText shouldBe ""
            }
        }

        "parseTimelineDescription" - {
            "should format string using startPlaceDetails if it is not null" {
                // 🔴 Given
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

                // 🟡 When
                val timelineDescription = ActivitySegmentFormatter.parseTimelineDescription(
                    startPlaceDetails = startPlaceDetails,
                    endPlaceDetails = endPlaceDetails,
                    startLocationText = startLocationText,
                    endLocationText = endLocationText,
                )

                // 🟢 Then
                timelineDescription shouldBe "some-start-location-textsome-end-location-textFirst segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n"
            }

            "should format string using endPlaceDetails if it is not null" {
                // 🔴 Given
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

                // 🟡 When
                val timelineDescription = ActivitySegmentFormatter.parseTimelineDescription(
                    startPlaceDetails = startPlaceDetails,
                    endPlaceDetails = endPlaceDetails,
                    startLocationText = startLocationText,
                    endLocationText = endLocationText,
                )

                // 🟢 Then
                timelineDescription shouldBe "some-start-location-textsome-end-location-textLast segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n"
            }

            "should format string using startPlaceDetails and endPlaceDetails if both are not null" {
                // 🔴 Given
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

                // 🟡 When
                val timelineDescription = ActivitySegmentFormatter.parseTimelineDescription(
                    startPlaceDetails = startPlaceDetails,
                    endPlaceDetails = endPlaceDetails,
                    startLocationText = startLocationText,
                    endLocationText = endLocationText,
                )

                // 🟢 Then
                timelineDescription shouldBe "some-start-location-textsome-end-location-textFirst segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\nLast segment: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n"
            }

            "should format string using startLocation and endLocation if both PlaceDetails are null" {
                // 🔴 Given
                val startPlaceDetails = null
                val endPlaceDetails = null
                val startLocationText = "some-start-location-text"
                val endLocationText = "some-end-location-text"

                // 🟡 When
                val timelineDescription = ActivitySegmentFormatter.parseTimelineDescription(
                    startPlaceDetails = startPlaceDetails,
                    endPlaceDetails = endPlaceDetails,
                    startLocationText = startLocationText,
                    endLocationText = endLocationText,
                )

                // 🟢 Then
                timelineDescription shouldBe "some-start-location-textsome-end-location-text"
            }
        }

        "getStartLocationText" - {
            "should format string using placeDetails if it is not null" {
                // 🔴 Given
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

                // 🟡 When
                val startLocationText = ActivitySegmentFormatter.getStartLocationText(
                    startLocation = startLocation,
                    placeDetails = placeDetails,
                )

                // 🟢 Then
                startLocationText shouldBe "Start Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n"
            }

            "should format string using startLocation if placeDetails is null" {
                // 🔴 Given
                val startLocation = Location(
                    placeId = "some-place-id",
                    latitudeE7 = 121234567,
                    longitudeE7 = 1231234567,
                    name = "some-start-location-name",
                    address = "some-start-address",
                )
                val placeDetails = null

                // 🟡 When
                val startLocationText = ActivitySegmentFormatter.getStartLocationText(
                    startLocation = startLocation,
                    placeDetails = placeDetails,
                )

                // 🟢 Then
                startLocationText shouldBe "Start Location: 12.123457,123.123457\\nhttps://maps.google.com?q=12.123457,123.123457\\n\\n"
            }
        }

        "getEndLocationText" - {
            "should format string using placeDetails if it is not null" {
                // 🔴 Given
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

                // 🟡 When
                val endLocationText = ActivitySegmentFormatter.getEndLocationText(
                    endLocation = endLocation,
                    placeDetails = placeDetails,
                )

                // 🟢 Then
                endLocationText shouldBe "End Location: some-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-place-id\\n\\n"
            }

            "should format string using endLocation if placeDetails is null" {
                // 🔴 Given
                val endLocation = Location(
                    placeId = "some-place-id",
                    latitudeE7 = 121234567,
                    longitudeE7 = 1231234567,
                    name = "some-end-location-name",
                    address = "some-end-address",
                )
                val placeDetails = null

                // 🟡 When
                val endLocationText = ActivitySegmentFormatter.getEndLocationText(
                    endLocation = endLocation,
                    placeDetails = placeDetails,
                )

                // 🟢 Then
                endLocationText shouldBe "End Location: 12.123457,123.123457\\nhttps://maps.google.com?q=12.123457,123.123457\\n\\n"
            }
        }

        "kilometersToMiles" - {
            "should return correct miles" {
                // 🔴 Given
                val meters = 3000.0

                // 🟡 When
                val miles = ActivitySegmentFormatter.kilometersToMiles(meters = meters)

                // 🟢 Then
                miles shouldBe 1863.0
            }
        }
    }
}
