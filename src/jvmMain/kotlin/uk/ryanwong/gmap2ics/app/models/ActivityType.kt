/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models

enum class ActivityType(val emoji: String) {
    UNKNOWN_ACTIVITY_TYPE("❓"),
    BOATING("🚣‍♀️"),
    CYCLING("🚲"),
    FLYING("✈️"),
    HIKING("⛰"),
    IN_BUS("🚎"),
    IN_CABLECAR("🚠"),
    IN_FERRY("⛴"),
    IN_PASSENGER_VEHICLE("\uD83D\uDE97"),
    IN_SUBWAY("🚇"),
    IN_TAXI("🚖"),
    IN_TRAIN("🚆"),
    IN_TRAM("🚋"),
    IN_VEHICLE("🚐"),
    MOTORCYCLING("🏍"),
    RUNNING("🏃‍♀️"),
    SAILING("⛵️"),
    SKIING("⛷"),
    STILL("\uD83E\uDDCD"),
    WALKING("🚶‍♂️");

    companion object {
        fun parse(activityType: String?): ActivityType {
            return activityType?.let {
                try {
                    ActivityType.valueOf(it)
                } catch (e: IllegalArgumentException) {
                    UNKNOWN_ACTIVITY_TYPE
                }
            } ?: UNKNOWN_ACTIVITY_TYPE
        }
    }
}
