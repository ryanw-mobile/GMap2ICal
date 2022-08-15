/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.models.places

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName(value = "address_components")
    val addressComponents: List<AddressComponent>? = null,
    @SerialName(value = "adr_address")
    val adrAddress: String? = null,
    @SerialName(value = "formatted_address")
    val formattedAddress: String,
    @SerialName(value = "formatted_phone_number")
    val formattedPhoneNumber: String? = null,
    val geometry: Geometry,
    val icon: String? = null,
    @SerialName(value = "icon_background_color")
    val iconBackgroundColor: String? = null,
    @SerialName(value = "icon_mask_base_uri")
    val iconMaskBaseUri: String? = null,
    val name: String,
    @SerialName(value = "place_id")
    val placeId: String,
    @SerialName(value = "plus_code")
    val plusCode: PlusCode? = null,
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