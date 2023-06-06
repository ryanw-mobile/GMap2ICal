/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline

import uk.ryanwong.gmap2ics.app.models.PlaceType
import uk.ryanwong.gmap2ics.data.models.places.Result

data class PlaceDetails(
    val placeId: String,
    val name: String,
    val formattedAddress: String,
    val geo: LatLng,
    val types: List<String>,
    val url: String,
) {
    companion object {
        fun from(placeDetailsResult: Result): PlaceDetails {
            with(placeDetailsResult) {
                return PlaceDetails(
                    placeId = placeId,
                    name = name,
                    formattedAddress = formattedAddress,
                    geo = LatLng(
                        latitude = geometry.location.lat,
                        longitude = geometry.location.lng,
                    ),
                    types = types,
                    url = url,
                )
            }
        }
    }

    fun getFormattedName(): String {
        return try {
            val placeType = resolveEnum()
            "${placeType.emoji} $name"
        } catch (ex: PlaceTypeNotFoundException) {
            ex.printStackTrace()
            "\uD83D\uDCCD $name"
        }
    }

    private fun resolveEnum(): PlaceType {
        for (type in types) {
            try {
                return PlaceType.valueOf(type.uppercase())
            } catch (ex: IllegalArgumentException) {
                // do nothing - look for the next one as substitute
                ex.printStackTrace()
            }
        }
        throw PlaceTypeNotFoundException(types = types, placeId = placeId)
    }
}

class PlaceTypeNotFoundException(val types: List<String>, val placeId: String) : Exception() {
    override val message: String
        get() = "⚠️ Unable to resolve any of the place types in $types for PlaceId $placeId"
}
