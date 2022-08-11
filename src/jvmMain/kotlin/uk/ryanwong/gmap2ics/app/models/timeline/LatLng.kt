/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline

import java.text.DecimalFormat

data class LatLng(
    val latitude: Double,
    val longitude: Double
) {
    private val latLngFormat = DecimalFormat("###.######")

    fun getFormattedLatLng(): String {
        val lat = latLngFormat.format(latitude)
        val lng = latLngFormat.format(longitude)
        return "$lat,$lng"
    }
}