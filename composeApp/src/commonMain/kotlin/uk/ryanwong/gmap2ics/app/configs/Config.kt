/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.configs

import uk.ryanwong.gmap2ics.app.models.ActivityType

/***
 * Configuration template
 * You may implement different processing rules in order to generate ICS dataset for different purposes
 */
interface Config {
    val jsonPath: String // Location of the source Json Files
    val icalPath: String // Location of the processed iCal output
    val exportPlaceVisit: Boolean
    val exportActivitySegment: Boolean
    val ignoredActivityType: List<ActivityType>
    val ignoredVisitedPlaceIds: List<String> // Exact match
    val verboseLogs: Boolean

    val placesApiKey: String?
    val enablePlacesApiLookup: Boolean

    // Format: "locale", "Google Maps API Language Code"
    // Reference: https://developers.google.com/maps/faq#languagesupport
    // "default" will be used as catch-all
    val apiLanguageOverride: Map<String, String>
}
