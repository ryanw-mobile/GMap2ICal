/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline

import java.text.DecimalFormat

data class Location(
    val placeId: String? = null,
    val latitudeE7: Int,
    val longitudeE7: Int,
    val name: String? = null,
    val address: String? = null
) {
    companion object {
        fun from(locationDataModel: uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.Location): Location {
            with(locationDataModel) {
                return Location(
                    address = address,
                    latitudeE7 = latitudeE7,
                    longitudeE7 = longitudeE7,
                    name = name,
                    placeId = placeId
                )
            }
        }

        fun from(activityLocationDataModel: uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivityLocation): Location? {
            with(activityLocationDataModel) {
                return if (latitudeE7 == null || longitudeE7 == null) {
                    null
                } else {
                    Location(
                        address = address,
                        latitudeE7 = latitudeE7,
                        longitudeE7 = longitudeE7,
                        name = name,
                        placeId = placeId
                    )
                }
            }
        }
    }

    private val latLngFormat = DecimalFormat("###.######")

    fun getLatitude(): Double {
        return latitudeE7.times(0.0000001)
    }

    fun getLongitude(): Double {
        return longitudeE7.times(0.0000001)
    }

    fun getFormattedLatLng(): String? {
        val lat = latitudeE7.let { latitude ->
            latLngFormat.format(latitude.times(0.0000001))
        }
        val lng = longitudeE7.let { longitude ->
            latLngFormat.format(longitude.times(0.0000001))
        }

        return if (lat != null && lng != null) "$lat,$lng"
        else null
    }

    fun getGoogleMapsLatLngLink(): String {
        return if (latitudeE7 == null || longitudeE7 == null) ""
        else "https://maps.google.com?q=${getFormattedLatLng()}"
    }

    fun getGoogleMapsPlaceIdLink(): String {
        return placeId?.let {
            "https://www.google.com/maps/place/?q=place_id:$it"
        } ?: ""
    }
}