/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.models.places

data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)