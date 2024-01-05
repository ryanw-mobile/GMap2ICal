/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.models.timeline.activity

import uk.ryanwong.gmap2ics.app.models.ActivityType

data class Activity(
    val activityType: ActivityType,
    val rawActivityType: String,
)
