/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.repositories

import uk.ryanwong.gmap2ics.domain.models.timeline.Timeline

interface TimelineRepository {
    suspend fun getTimeLine(filePath: String): Result<Timeline>
}
