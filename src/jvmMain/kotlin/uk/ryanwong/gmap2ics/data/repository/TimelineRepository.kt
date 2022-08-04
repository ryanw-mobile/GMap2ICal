/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.TimelineObjects

interface TimelineRepository {
    suspend fun parseTimeLine(filePath: String): Result<TimelineObjects>
}