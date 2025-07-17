/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.utils.timezonemap

import us.dustinj.timezonemap.TimeZone

/**
 * Only when the activity happened in UK we display values in miles.
 * Others are in kilometers.
 */
fun TimeZone.shouldShowMiles(): Boolean = zoneId == "Europe/London"
