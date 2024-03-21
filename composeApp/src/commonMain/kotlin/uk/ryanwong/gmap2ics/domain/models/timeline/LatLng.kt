/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import java.text.DecimalFormat

data class LatLng(
    val latitude: Double,
    val longitude: Double,
) {
    private val latLngFormat = DecimalFormat("###.######")

    fun getFormattedLatLng(): String {
        val lat = latLngFormat.format(latitude)
        val lng = latLngFormat.format(longitude)
        return "$lat,$lng"
    }
}
