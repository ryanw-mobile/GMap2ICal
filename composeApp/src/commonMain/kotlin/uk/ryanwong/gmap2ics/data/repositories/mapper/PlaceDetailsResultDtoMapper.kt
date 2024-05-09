/*
 * Copyright (c) 2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repositories.mapper

import uk.ryanwong.gmap2ics.data.models.places.ResultDto
import uk.ryanwong.gmap2ics.domain.models.timeline.LatLng
import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails

fun ResultDto.toPlaceDetails() = PlaceDetails(
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
