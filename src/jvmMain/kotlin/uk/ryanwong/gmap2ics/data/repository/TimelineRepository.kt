/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.VEvent

interface TimelineRepository {
    suspend fun getEventList(filePath: String): Result<List<VEvent>>
}