/*
 * Copyright (c) 2023-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class ActivityTypeTest : FreeSpec() {

    init {
        "parse()" - {
            "should convert correctly from known ActivityType strings to the Enums" {
                val knownActivityTypes = mapOf(
                    "BOATING" to ActivityType.BOATING,
                    "CYCLING" to ActivityType.CYCLING,
                    "FLYING" to ActivityType.FLYING,
                    "HIKING" to ActivityType.HIKING,
                    "IN_BUS" to ActivityType.IN_BUS,
                    "IN_CABLECAR" to ActivityType.IN_CABLECAR,
                    "IN_FERRY" to ActivityType.IN_FERRY,
                    "IN_PASSENGER_VEHICLE" to ActivityType.IN_PASSENGER_VEHICLE,
                    "IN_SUBWAY" to ActivityType.IN_SUBWAY,
                    "IN_TAXI" to ActivityType.IN_TAXI,
                    "IN_TRAIN" to ActivityType.IN_TRAIN,
                    "IN_TRAM" to ActivityType.IN_TRAM,
                    "IN_VEHICLE" to ActivityType.IN_VEHICLE,
                    "MOTORCYCLING" to ActivityType.MOTORCYCLING,
                    "RUNNING" to ActivityType.RUNNING,
                    "SAILING" to ActivityType.SAILING,
                    "SKIING" to ActivityType.SKIING,
                    "STILL" to ActivityType.STILL,
                    "WALKING" to ActivityType.WALKING,
                )

                knownActivityTypes.forEach { knownActivityType ->
                    val activityTypeEnum = ActivityType.parse(activityType = knownActivityType.key)

                    activityTypeEnum shouldBe knownActivityType.value
                }
            }

            "should return ActivityType.UNKNOWN_ACTIVITY_TYPE for unknown strings" {
                val unknownActivityString = "some-unknown-activity-type"
                val activityTypeEnum = ActivityType.parse(activityType = unknownActivityString)
                activityTypeEnum shouldBe ActivityType.UNKNOWN_ACTIVITY_TYPE
            }

            "should return ActivityType.UNKNOWN_ACTIVITY_TYPE for null input" {
                val unknownActivityString = null
                val activityTypeEnum = ActivityType.parse(activityType = unknownActivityString)
                activityTypeEnum shouldBe ActivityType.UNKNOWN_ACTIVITY_TYPE
            }
        }
    }
}
