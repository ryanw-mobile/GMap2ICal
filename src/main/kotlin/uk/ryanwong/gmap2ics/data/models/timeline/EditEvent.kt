package uk.ryanwong.gmap2ics.data.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class EditEvent(
    val editOperation: List<String>? = null,
    val uiConfiguration: UiConfiguration? = null,
)