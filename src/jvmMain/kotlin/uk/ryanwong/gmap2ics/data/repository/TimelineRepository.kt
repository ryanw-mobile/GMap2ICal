/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import kotlinx.coroutines.flow.StateFlow
import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.VEvent

interface TimelineRepository {
    val statusLog: StateFlow<String?>
    suspend fun getEventList(
        filePath: String,
        ignoredVisitedPlaceIds: List<String>,
        ignoredActivityType: List<ActivityType>,
        exportActivitySegment: Boolean,
        exportPlaceVisit: Boolean,
        verboseLogs: Boolean,
        enablePlacesApiLookup: Boolean
    ): Result<List<VEvent>>
}