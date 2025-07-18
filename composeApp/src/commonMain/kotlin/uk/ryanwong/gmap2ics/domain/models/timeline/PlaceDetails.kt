/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import uk.ryanwong.gmap2ics.domain.models.PlaceType

data class PlaceDetails(
    val placeId: String,
    val name: String,
    val formattedAddress: String,
    val geo: LatLng,
    val types: List<String>,
    val url: String,
) {
    fun getFormattedName(): String = try {
        val placeType = resolveEnum()
        "${placeType.emoji} $name"
    } catch (ex: PlaceTypeNotFoundException) {
        ex.printStackTrace()
        "\uD83D\uDCCD $name"
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
