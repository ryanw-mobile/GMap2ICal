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
    fun getFormattedName(): String {
        val placeType = types.firstNotNullOfOrNull { type ->
            PlaceType.entries.firstOrNull { placeType -> placeType.name.equals(type, ignoreCase = true) }
        }
        val emoji = placeType?.emoji ?: "\uD83D\uDCCD"
        return "$emoji $name"
    }
}
