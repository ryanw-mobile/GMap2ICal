package uk.ryanwong.gmap2ics.data.models.timeline

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class EditHistory(
    val editEvent: List<EditEvent>? = null
)