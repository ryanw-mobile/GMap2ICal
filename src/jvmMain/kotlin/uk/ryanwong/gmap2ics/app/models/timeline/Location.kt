/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline

import uk.ryanwong.gmap2ics.data.models.timeline.ActivityLocation
import java.text.DecimalFormat

data class Location(
    val placeId: String? = null,
    val latitudeE7: Int,
    val longitudeE7: Int,
    val name: String? = null,
    val address: String? = null
) {
    companion object {
        fun from(locationDataModel: uk.ryanwong.gmap2ics.data.models.timeline.Location): Location? {
            with(locationDataModel) {
                return if (latitudeE7 == null || longitudeE7 == null) {
                    null
                } else {
                    return Location(
                        address = address,
                        latitudeE7 = latitudeE7,
                        longitudeE7 = longitudeE7,
                        name = name,
                        placeId = placeId
                    )
                }
            }
        }

        // Location without LatLng is meaningless.
        // Caller should consider dropping the entry should that happens.
        fun from(activityLocationDataModel: ActivityLocation): Location? {
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
        return latitudeE7 / 10000000.0
    }

    fun getLongitude(): Double {
        return longitudeE7 / 10000000.0
    }

    fun getFormattedLatLng(): String {
        val lat = latLngFormat.format(latitudeE7 / 10000000.0)
        val lng = latLngFormat.format(longitudeE7 / 10000000.0)
        return "$lat,$lng"
    }

    fun getGoogleMapsLatLngLink(): String {
        return "https://maps.google.com?q=${getFormattedLatLng()}"
    }

    fun getGoogleMapsPlaceIdLink(): String {
        return placeId?.let {
            "https://www.google.com/maps/place/?q=place_id:$it"
        } ?: ""
    }
}
