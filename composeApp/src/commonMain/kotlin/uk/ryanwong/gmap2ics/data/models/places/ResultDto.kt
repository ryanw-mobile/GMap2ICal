/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.models.places

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultDto(
    @SerialName(value = "formatted_address")
    val formattedAddress: String,
    @SerialName(value = "formatted_phone_number")
    val formattedPhoneNumber: String? = null,
    val geometryDto: GeometryDto,
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
    val website: String? = null,
)
