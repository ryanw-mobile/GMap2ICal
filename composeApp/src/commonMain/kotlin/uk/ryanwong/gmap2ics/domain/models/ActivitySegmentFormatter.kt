/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models

import uk.ryanwong.gmap2ics.domain.models.timeline.Location
import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails

object ActivitySegmentFormatter {
    fun parseActivityRouteText(
        startPlaceDetails: PlaceDetails?,
        endPlaceDetails: PlaceDetails?,
        startLocation: String?,
        endLocation: String?,
    ): String {
        // PlaceDetails are the most reliable source
        return when {
            startPlaceDetails != null || endPlaceDetails != null -> {
                "(${startPlaceDetails?.name} ➡ ${endPlaceDetails?.name})"
            }

            startLocation == null && endLocation == null -> ""
            else -> "($startLocation ➡ $endLocation)"
        }
    }

    fun parseTimelineDescription(
        startPlaceDetails: PlaceDetails?,
        endPlaceDetails: PlaceDetails?,
        startLocationText: String,
        endLocationText: String,
    ): String {
        // Segments are less accurate than start and end locations,
        // but still have some values if the start and end locations do not have a valid placeId
        val firstSegmentText = startPlaceDetails?.let { placeDetails ->
            "First segment: ${placeDetails.formattedAddress}\\nhttps://www.google.com/maps/place/?q=place_id:${placeDetails.placeId}\\n\\n"
        } ?: ""

        val lastSegmentText = endPlaceDetails?.let { placeDetails ->
            "Last segment: ${placeDetails.formattedAddress}\\nhttps://www.google.com/maps/place/?q=place_id:${placeDetails.placeId}\\n\\n"
        } ?: ""

        return startLocationText +
            endLocationText +
            firstSegmentText +
            lastSegmentText
    }

    fun getStartLocationText(startLocation: Location, placeDetails: PlaceDetails?): String {
        return placeDetails?.let { place ->
            "Start Location: ${place.formattedAddress}\\n${startLocation.getGoogleMapsPlaceIdLink()}\\n\\n"
        }
            ?: "Start Location: ${startLocation.getFormattedLatLng()}\\n${startLocation.getGoogleMapsLatLngLink()}\\n\\n"
    }

    fun getEndLocationText(endLocation: Location, placeDetails: PlaceDetails?): String {
        return placeDetails?.let { place ->
            "End Location: ${place.formattedAddress}\\n${endLocation.getGoogleMapsPlaceIdLink()}\\n\\n"
        }
            ?: "End Location: ${endLocation.getFormattedLatLng()}\\n${endLocation.getGoogleMapsLatLngLink()}\\n\\n"
    }

    fun kilometersToMiles(meters: Double): Double = meters * 0.621
}
