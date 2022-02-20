package uk.ryanwong.gmap2ics.domain.models

import uk.ryanwong.gmap2ics.domain.PlaceType
import uk.ryanwong.gmap2ics.domain.getLabel

data class PlaceDetails(
    val placeId: String,
    val name: String,
    val formattedAddress: String,
    val lat: Double,
    val lng: Double,
    val types: List<String>,
    val url: String
) {
    companion object {
        fun from(placeDetails: uk.ryanwong.gmap2ics.data.models.places.PlaceDetails): PlaceDetails {
            with(placeDetails.result) {
                return PlaceDetails(
                    placeId = place_id,
                    name = name,
                    formattedAddress = formatted_address,
                    lat = geometry.location.lat,
                    lng = geometry.location.lng,
                    types = types,
                    url = url
                )
            }
        }
    }

    fun getFormattedName(): String {
        return try {
            val placeType = PlaceType.valueOf(types[0].uppercase())
            "${placeType.getLabel()} $name"

        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
            "\uD83D\uDCCD $name"
        }
    }
}

