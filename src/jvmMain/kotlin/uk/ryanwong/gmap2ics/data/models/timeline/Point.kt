/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.models.timeline

import kotlinx.serialization.Serializable

@Serializable
data class Point(
    val accuracyMeters: Int? = null,
    val latE7: Int? = null,
    val lngE7: Int? = null,
    val timestamp: String? = null
)