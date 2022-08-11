/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.models.places

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName(value = "addressComponents")
    val address_components: List<AddressComponent>? = null,
    @SerialName(value = "adrAddress")
    val adr_address: String? = null,
    @SerialName(value = "formattedAddress")
    val formatted_address: String,
    @SerialName(value = "formattedPhoneNumber")
    val formatted_phone_number: String? = null,
    val geometry: Geometry,
    val icon: String? = null,
    @SerialName(value = "iconBackgroundColor")
    val icon_background_color: String? = null,
    @SerialName(value = "iconMaskBaseUri")
    val icon_mask_base_uri: String? = null,
    val name: String,
    @SerialName(value = "placeId")
    val place_id: String,
    @SerialName(value = "plusCode")
    val plus_code: PlusCode? = null,
    val reference: String? = null,
    val types: List<String>,
    val url: String,
    @SerialName(value = "userRatingsTotal")
    val user_ratings_total: Int? = null,
    @SerialName(value = "utcOffset")
    val utc_offset: Int? = null,
    val vicinity: String? = null,
    val website: String? = null
)