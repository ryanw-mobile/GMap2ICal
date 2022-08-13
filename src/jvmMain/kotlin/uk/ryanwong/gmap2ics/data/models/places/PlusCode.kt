/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.models.places

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlusCode(
    @SerialName(value = "compound_code")
    val compoundCode: String? = null,
    @SerialName(value = "global_code")
    val globalCode: String? = null
)