/*
 * Copyright (c) 2023. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models.timeline

import uk.ryanwong.gmap2ics.data.models.timeline.ActivityLocation

fun uk.ryanwong.gmap2ics.data.models.timeline.Location.toDomainModel(): Location? {
    return if (latitudeE7 == null || longitudeE7 == null) {
        null
    } else {
        return Location(
            address = address,
            latitudeE7 = latitudeE7,
            longitudeE7 = longitudeE7,
            name = name,
            placeId = placeId
        )
    }
}

// Location without LatLng is meaningless.
// Caller should consider dropping the entry should that happens.
fun ActivityLocation.toDomainModel(): Location? {
    return if (latitudeE7 == null || longitudeE7 == null) {
        null
    } else {
        Location(
            address = address,
            latitudeE7 = latitudeE7,
            longitudeE7 = longitudeE7,
            name = name,
            placeId = placeId
        )
    }
}
