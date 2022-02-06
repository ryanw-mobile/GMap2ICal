package uk.ryanwong.gmap2ics.data.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UiConfiguration(
    val uiPlaceVisitConfiguration: String? = null,
    val uiActivitySegmentConfiguration: String? = null
)