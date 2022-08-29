/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.models.places

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName(value = "formatted_address")
    val formattedAddress: String,
    @SerialName(value = "formatted_phone_number")
    val formattedPhoneNumber: String? = null,
    val geometry: Geometry,
    val icon: String? = null,
    val name: String,
    @SerialName(value = "place_id")
    val placeId: String,
    val reference: String? = null,
    val types: List<String>,
    val url: String,
    @SerialName(value = "user_ratings_total")
    val userRatingsTotal: Int? = null,
    @SerialName(value = "utc_offset")
    val utcOffset: Int? = null,
    val vicinity: String? = null,
    val website: String? = null
)
