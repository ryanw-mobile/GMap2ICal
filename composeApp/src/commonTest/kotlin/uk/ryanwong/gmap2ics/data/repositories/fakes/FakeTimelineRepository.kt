/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repositories.fakes

import uk.ryanwong.gmap2ics.domain.models.timeline.Timeline
import uk.ryanwong.gmap2ics.domain.repositories.TimelineRepository

class FakeTimelineRepository : TimelineRepository {
    var getTimeLineResponse: Result<Timeline>? = null
    override suspend fun getTimeLine(filePath: String): Result<Timeline> = getTimeLineResponse ?: Result.failure(Exception("response not defined"))
}
