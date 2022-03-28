package uk.ryanwong.gmap2ics.domain.models

import java.text.DecimalFormat

data class LatLng(
    val latitude: Double,
    val longitude: Double
) {
    private val latLngFormat = DecimalFormat("###.######")

    fun getFormattedLatLng(): String? {
        val lat = latLngFormat.format(latitude.times(0.0000001))
        val lng = latLngFormat.format(longitude.times(0.0000001))
        return "$lat, $lng"
    }
}