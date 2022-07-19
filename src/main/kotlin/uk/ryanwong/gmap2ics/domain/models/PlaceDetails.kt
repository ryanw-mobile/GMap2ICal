package uk.ryanwong.gmap2ics.domain.models

import uk.ryanwong.gmap2ics.data.models.places.Result
import uk.ryanwong.gmap2ics.domain.PlaceType
import uk.ryanwong.gmap2ics.domain.getLabel

data class PlaceDetails(
    val placeId: String,
    val name: String,
    val formattedAddress: String,
    val geo: LatLng,
    val types: List<String>,
    val url: String
) {
    companion object {
        fun from(placeDetailsResult: Result): PlaceDetails {
            with(placeDetailsResult) {
                return PlaceDetails(
                    placeId = place_id,
                    name = name,
                    formattedAddress = formatted_address,
                    geo = LatLng(
                        latitude = geometry.location.lat,
                        longitude = geometry.location.lng
                    ),
                    types = types,
                    url = url
                )
            }
        }
    }

    fun getFormattedName(): String {
        return resolveEnum()?.let { placeType ->
            "${placeType.getLabel()} $name"
        } ?: "\uD83D\uDCCD $name"
    }

    private fun resolveEnum(): PlaceType? {
        for (type in types) {
            try {
                return PlaceType.valueOf(type.uppercase())
            } catch (ex: IllegalArgumentException) {
                // do nothing
                ex.printStackTrace()
            }
        }
        println("⚠️ Unable to resolve any of the place types in $types for PlaceId $placeId")
        return null
    }
}

