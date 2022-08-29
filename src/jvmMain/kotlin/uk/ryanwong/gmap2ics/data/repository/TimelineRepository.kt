/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.timeline.Timeline

interface TimelineRepository {
    suspend fun getTimeLine(filePath: String): Result<Timeline>
}
