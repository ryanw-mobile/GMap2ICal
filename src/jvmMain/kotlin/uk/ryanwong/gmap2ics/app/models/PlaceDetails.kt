/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models

import uk.ryanwong.gmap2ics.app.PlaceType

data class PlaceDetails(
    val placeId: String,
    val name: String,
    val formattedAddress: String,
    val geo: LatLng,
    val types: List<String>,
    val url: String
) {
    companion object {
        fun from(placeDetailsResult: uk.ryanwong.gmap2ics.data.source.googleapi.models.places.Result): PlaceDetails {
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
            "${placeType.emoji} $name"
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

