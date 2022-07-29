package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.domain.models.VEvent

interface LocalFileRepository {
    suspend fun exportICal(filename: String, vEvents: List<VEvent>)
}