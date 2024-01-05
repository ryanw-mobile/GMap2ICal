/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.app.configs

import uk.ryanwong.gmap2ics.app.models.ActivityType

class DefaultConfig : Config {
    override val jsonPath = "./src/commonMain/resources"

    override val icalPath = "./src/commonMain/resources"

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
        ActivityType.UNKNOWN_ACTIVITY_TYPE,
    )

    override val ignoredVisitedPlaceIds = listOf(
        "PlaceIds extracted from Json",
//        "ChIJ85tY7jeue0gRZ9Sik4vVUnk"
    )

    override val verboseLogs = true
    override val placesApiKey: String? = null
    override val enablePlacesApiLookup = false
    override val apiLanguageOverride = mapOf(
        Pair("default", "en"),
//        Pair("Asia/Hong_Kong", "zh-HK"),
//        Pair("Asia/Tokyo", "ja"),
//        Pair("Asia/Bangkok", "th"),
//        Pair("Asia/Seoul", "ko"),
//        Pair("Asia/Shanghai", "zh-CN"),
//        Pair("Asia/Taipei", "zh-TW"),
//        Pair("Europe/London", "en-GB")
    )
}
