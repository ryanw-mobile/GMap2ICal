/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import java.text.DecimalFormat

data class Location(
    val placeId: String? = null,
    val latitudeE7: Int,
    val longitudeE7: Int,
    val name: String? = null,
    val address: String? = null,
) {
    private val latLngFormat = DecimalFormat("###.######")

    fun getLatitude(): Double = latitudeE7 / 10000000.0

    fun getLongitude(): Double = longitudeE7 / 10000000.0

    fun getFormattedLatLng(): String {
        val lat = latLngFormat.format(latitudeE7 / 10000000.0)
        val lng = latLngFormat.format(longitudeE7 / 10000000.0)
        return "$lat,$lng"
    }

    fun getGoogleMapsLatLngLink(): String = "https://maps.google.com?q=${getFormattedLatLng()}"

    fun getGoogleMapsPlaceIdLink(): String = placeId?.let {
        "https://www.google.com/maps/place/?q=place_id:$it"
    } ?: ""
}
