/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline

import kotlinx.serialization.Serializable

@Serializable
data class Activity(
    val activityType: String? = null,
    val probability: Double? = null
)