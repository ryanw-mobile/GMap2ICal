/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline

import kotlinx.serialization.Serializable

@Serializable
data class Duration(
    val endTimestamp: String,
    val startTimestamp: String
)