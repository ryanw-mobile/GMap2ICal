/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.models.places

import kotlinx.serialization.Serializable

@Serializable
data class PlaceDetails(
    val result: Result? = null,
)
