package uk.ryanwong.gmap2ics.domain.models

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