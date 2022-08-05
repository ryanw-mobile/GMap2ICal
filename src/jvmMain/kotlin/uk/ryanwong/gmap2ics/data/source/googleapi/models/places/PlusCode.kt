/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.models.places

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
data class PlusCode(
    @Json(name = "compound_code")
    val compoundCode: String? = null,
    @Json(name = "global_code")
    val globalCode: String? = null
)