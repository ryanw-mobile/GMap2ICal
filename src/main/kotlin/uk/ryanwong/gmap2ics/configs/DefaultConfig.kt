package uk.ryanwong.gmap2ics.configs

import uk.ryanwong.gmap2ics.domain.ActivityType

class DefaultConfig : Config {
    override val jsonPath = "./src/main/resources"

    override val icalPath = "./src/main/resources"

    override val exportPlaceVisit = true
    override val exportActivitySegment = true

    override val ignoredActivityType = listOf(
        ActivityType.WALKING,
        ActivityType.IN_TAXI,
        ActivityType.IN_BUS,
        ActivityType.IN_SUBWAY,
        ActivityType.FLYING,
        ActivityType.IN_TRAIN,
        ActivityType.IN_TRAM,
        ActivityType.IN_FERRY,
        ActivityType.UNKNOWN_ACTIVITY_TYPE
    )

    override val ignoredVisitedLocations = listOf(
        "My Home",
    )

    override val displayLogs = true
}
