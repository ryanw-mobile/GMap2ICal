package uk.ryanwong.gmap2ics.data.models.places

data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)