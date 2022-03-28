package uk.ryanwong.gmap2ics.data.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.text.DecimalFormat

@JsonIgnoreProperties(ignoreUnknown = true)
data class ActivityLocation(
    val address: String? = null,
    val calibratedProbability: Double? = null,
    val latitudeE7: Int? = null,
    val locationConfidence: Double? = null,
    val longitudeE7: Int? = null,
    val name: String? = null,
    val placeId: String? = null,
    val semanticType: String? = null,
) {
    private val latLngFormat = DecimalFormat("###.######")

    fun getLatitude(): Double? {
        return latitudeE7?.times(0.0000001)
    }

    fun getLongitude(): Double? {
        return longitudeE7?.times(0.0000001)
    }

    fun getFormattedLatLng(): String? {
        val lat = latitudeE7?.let { latitude ->
            latLngFormat.format(latitude.times(0.0000001))
        }
        val lng = longitudeE7?.let { longitude ->
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