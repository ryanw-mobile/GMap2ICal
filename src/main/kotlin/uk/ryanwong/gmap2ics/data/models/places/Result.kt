package uk.ryanwong.gmap2ics.data.models.places

data class Result(
    val address_components: List<AddressComponent>? = null,
    val adr_address: String? = null,
    val formatted_address: String,
    val formatted_phone_number: String? = null,
    val geometry: Geometry,
    val icon: String? = null,
    val icon_background_color: String? = null,
    val icon_mask_base_uri: String? = null,
    val name: String,
    val place_id: String,
    val plus_code: PlusCode? = null,
    val reference: String? = null,
    val types: List<String>,
    val url: String,
    val user_ratings_total: Int? = null,
    val utc_offset: Int? = null,
    val vicinity: String? = null,
    val website: String? = null
)