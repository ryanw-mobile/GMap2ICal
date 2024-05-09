/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.models.places

import kotlinx.serialization.Serializable

@Serializable
data class GeometryDto(
    val location: LocationDto,
)
