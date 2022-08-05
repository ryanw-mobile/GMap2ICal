/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.models.places

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @Json(name = "addressComponents")
    val address_components: List<AddressComponent>? = null,
    @Json(name = "adrAddress")
    val adr_address: String? = null,
    @Json(name = "formattedAddress")
    val formatted_address: String,
    @Json(name = "formattedPhoneNumber")
    val formatted_phone_number: String? = null,
    val geometry: Geometry,
    val icon: String? = null,
    @Json(name = "iconBackgroundColor")
    val icon_background_color: String? = null,
    @Json(name = "iconMaskBaseUri")
    val icon_mask_base_uri: String? = null,
    val name: String,
    @Json(name = "placeId")
    val place_id: String,
    @Json(name = "plusCode")
    val plus_code: PlusCode? = null,
    val reference: String? = null,
    val types: List<String>,
    val url: String,
    @Json(name = "userRatingsTotal")
    val user_ratings_total: Int? = null,
    @Json(name = "utcOffset")
    val utc_offset: Int? = null,
    val vicinity: String? = null,
    val website: String? = null
)