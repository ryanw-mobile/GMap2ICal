/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.models.timeline

import kotlinx.serialization.Serializable

@Serializable
data class ChildVisit(
    val centerLatE7: Int? = null,
    val centerLngE7: Int? = null,
    val duration: Duration? = null,
    val lastEditedTimestamp: String? = null,
    val location: Location,
    val locationConfidence: Int? = null,
    val placeConfidence: String? = null,
    val placeVisitType: String? = null,
    val visitConfidence: Int? = null,
)
