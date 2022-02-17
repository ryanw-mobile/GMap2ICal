package uk.ryanwong.gmap2ics.configs

import uk.ryanwong.gmap2ics.domain.ActivityType

/***
 * Configuration template
 * You may implement different processing rules in order to generate ICS dataset for different purposes
 */
sealed interface Config {
    val jsonPath: String  // Location of the source Json Files
    val icalPath: String  // Location of the processed iCal output
    val exportPlaceVisit: Boolean
    val exportActivitySegment: Boolean
    val ignoredActivityType: List<ActivityType>
    val ignoredVisitedLocations: List<String> // Exact match
    val displayLogs: Boolean

    val placesApiKey: String?
    val enablePlacesApiLookup: Boolean
}