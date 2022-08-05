/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.models.places

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
data class AddressComponent(
    @Json(name = "long_name")
    val longName: String,
    @Json(name = "short_name")
    val shortName: String,
    val types: List<String>
)