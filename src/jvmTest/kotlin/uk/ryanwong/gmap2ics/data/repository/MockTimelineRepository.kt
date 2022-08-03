/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.VEvent

class MockTimelineRepository : TimelineRepository {

    var getEventListResponse: Result<List<VEvent>>? = null
    override suspend fun getEventList(filePath: String): Result<List<VEvent>> {
        return getEventListResponse ?: Result.failure(Exception("mock response unavailable"))
    }
}