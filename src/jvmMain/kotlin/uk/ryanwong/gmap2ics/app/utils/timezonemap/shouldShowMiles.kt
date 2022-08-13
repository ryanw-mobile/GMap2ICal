/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.utils.timezonemap

import us.dustinj.timezonemap.TimeZone

/**
 * Only when the activity happened in UK we display values in miles.
 * Others are in kilometers.
 */
fun TimeZone.shouldShowMiles(): Boolean {
    return zoneId == "Europe/London"
}