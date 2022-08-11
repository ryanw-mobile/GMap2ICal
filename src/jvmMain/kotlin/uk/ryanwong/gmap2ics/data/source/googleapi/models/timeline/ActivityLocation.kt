/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ActivityLocation(
    val address: String? = null,
    val latitudeE7: Int? = null,
    val locationConfidence: Double? = null,
    val longitudeE7: Int? = null,
    val name: String? = null,
    val placeId: String? = null
)