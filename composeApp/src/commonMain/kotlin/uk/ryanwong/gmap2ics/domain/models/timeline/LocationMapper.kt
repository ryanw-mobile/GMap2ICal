/*
 * Copyright (c) 2023-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import uk.ryanwong.gmap2ics.data.models.timeline.ActivityLocationDto

fun uk.ryanwong.gmap2ics.data.models.timeline.LocationDto.toDomainModel(): Location? {
    return if (latitudeE7 == null || longitudeE7 == null) {
        null
    } else {
        return Location(
            address = address,
            latitudeE7 = latitudeE7,
            longitudeE7 = longitudeE7,
            name = name,
            placeId = placeId,
        )
    }
}

// Location without LatLng is meaningless.
// Caller should consider dropping the entry should that happens.
fun ActivityLocationDto.toDomainModel(): Location? = if (latitudeE7 == null || longitudeE7 == null) {
    null
} else {
    Location(
        address = address,
        latitudeE7 = latitudeE7,
        longitudeE7 = longitudeE7,
        name = name,
        placeId = placeId,
    )
}
