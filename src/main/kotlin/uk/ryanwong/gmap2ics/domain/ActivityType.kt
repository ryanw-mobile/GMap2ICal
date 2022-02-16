package uk.ryanwong.gmap2ics.domain

enum class ActivityType {
    UNKNOWN_ACTIVITY_TYPE,
    BOATING,
    CYCLING,
    FLYING,
    HIKING,
    IN_BUS,
    IN_CABLECAR,
    IN_FERRY,
    IN_PASSENGER_VEHICLE,
    IN_SUBWAY,
    IN_TAXI,
    IN_TRAIN,
    IN_TRAM,
    IN_VEHICLE,
    MOTORCYCLING,
    RUNNING,
    SAILING,
    SKIING,
    STILL,
    WALKING
}

fun ActivityType.getLabel() : String {
    return when (this) {
        ActivityType.UNKNOWN_ACTIVITY_TYPE -> "❓"
        ActivityType.BOATING -> "🚣‍♀️"
        ActivityType.CYCLING -> "🚲"
        ActivityType.FLYING -> "✈️"
        ActivityType.HIKING -> "⛰"
        ActivityType.IN_BUS -> "🚎"
        ActivityType.IN_CABLECAR -> "🚠"
        ActivityType.IN_FERRY -> "⛴"
        ActivityType.IN_PASSENGER_VEHICLE -> "\uD83D\uDE97"
        ActivityType.IN_SUBWAY -> "🚇"
        ActivityType.IN_TAXI -> "🚖"
        ActivityType.IN_TRAIN -> "🚆"
        ActivityType.IN_TRAM -> "🚋"
        ActivityType.IN_VEHICLE -> "🚐"
        ActivityType.MOTORCYCLING -> "🏍"
        ActivityType.RUNNING -> "🏃‍♀️"
        ActivityType.SAILING -> "⛵️"
        ActivityType.SKIING -> "⛷"
        ActivityType.STILL -> "\uD83E\uDDCD"
        ActivityType.WALKING -> "🚶‍♂️"
    }
}