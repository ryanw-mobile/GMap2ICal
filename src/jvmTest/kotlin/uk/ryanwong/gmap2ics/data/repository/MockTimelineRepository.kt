/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.VEvent

class MockTimelineRepository : TimelineRepository {

    var getEventListResponse: Result<List<VEvent>>? = null
    override suspend fun getEventList(
        filePath: String,
        ignoredVisitedPlaceIds: List<String>,
        ignoredActivityType: List<ActivityType>,
        exportActivitySegment: Boolean,
        exportPlaceVisit: Boolean,
        verboseLogs: Boolean,
        enablePlacesApiLookup: Boolean
    ): Result<List<VEvent>> {
        return getEventListResponse ?: Result.failure(Exception("mock response unavailable"))
    }

    var statusLogResponse: String? = null
    override val statusLog: StateFlow<String?>
        get() = MutableStateFlow(statusLogResponse)

}