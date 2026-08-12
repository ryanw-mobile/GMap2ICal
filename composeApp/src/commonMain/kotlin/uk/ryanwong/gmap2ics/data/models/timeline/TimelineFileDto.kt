/*
 * Copyright (c) 2022-2026. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.models.timeline

import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

/**
 * Root model for both the legacy Google Takeout export and the newer on-device
 * Timeline export.
 */
@Serializable
data class TimelineFileDto(
    val timelineObjects: List<TimelineObjectDto>? = null,
    val semanticSegments: List<SemanticSegmentDto>? = null,
) {
    fun toTimelineObjectsDto(): TimelineObjectsDto = when {
        timelineObjects != null -> TimelineObjectsDto(timelineObjects = timelineObjects)

        semanticSegments != null -> TimelineObjectsDto(
            timelineObjects = semanticSegments.mapNotNull(SemanticSegmentDto::toTimelineObjectDto),
        )

        else -> throw IllegalArgumentException(
            "Unsupported Timeline JSON: expected 'timelineObjects' or 'semanticSegments' at the root",
        )
    }
}

@Serializable
data class SemanticSegmentDto(
    val startTime: String? = null,
    val endTime: String? = null,
    val visit: SemanticVisitDto? = null,
    val activity: SemanticActivityDto? = null,
) {
    fun toTimelineObjectDto(): TimelineObjectDto? {
        val startTimestamp = startTime ?: return null
        val endTimestamp = endTime ?: return null

        return when {
            visit != null -> visit.toTimelineObjectDto(
                startTimestamp = startTimestamp,
                endTimestamp = endTimestamp,
            )

            activity != null -> activity.toTimelineObjectDto(
                startTimestamp = startTimestamp,
                endTimestamp = endTimestamp,
            )

            else -> null
        }
    }
}

@Serializable
data class SemanticVisitDto(
    val topCandidate: SemanticPlaceCandidateDto? = null,
) {
    fun toTimelineObjectDto(startTimestamp: String, endTimestamp: String): TimelineObjectDto? {
        val candidate = topCandidate ?: return null
        val coordinates = candidate.placeLocation?.toCoordinates() ?: return null

        return TimelineObjectDto(
            placeVisit = PlaceVisitDto(
                duration = DurationDto(
                    startTimestamp = startTimestamp,
                    endTimestamp = endTimestamp,
                ),
                location = LocationDto(
                    latitudeE7 = coordinates.latitudeE7,
                    longitudeE7 = coordinates.longitudeE7,
                    name = candidate.semanticType.toLocationName(),
                    placeId = candidate.placeId,
                ),
            ),
        )
    }
}

@Serializable
data class SemanticPlaceCandidateDto(
    val placeId: String? = null,
    val semanticType: String? = null,
    val placeLocation: LatLngStringDto? = null,
)

@Serializable
data class SemanticActivityDto(
    val start: LatLngStringDto? = null,
    val end: LatLngStringDto? = null,
    val distanceMeters: Double? = null,
    val topCandidate: SemanticActivityCandidateDto? = null,
) {
    fun toTimelineObjectDto(startTimestamp: String, endTimestamp: String): TimelineObjectDto? {
        val startCoordinates = start?.toCoordinates() ?: return null
        val endCoordinates = end?.toCoordinates() ?: return null
        val activityType = topCandidate?.type

        return TimelineObjectDto(
            activitySegment = ActivitySegmentDto(
                activityType = activityType,
                activities = activityType?.let { type -> listOf(ActivityDto(activityType = type)) },
                distance = distanceMeters?.roundToInt(),
                duration = DurationDto(
                    startTimestamp = startTimestamp,
                    endTimestamp = endTimestamp,
                ),
                startLocation = ActivityLocationDto(
                    latitudeE7 = startCoordinates.latitudeE7,
                    longitudeE7 = startCoordinates.longitudeE7,
                ),
                endLocation = ActivityLocationDto(
                    latitudeE7 = endCoordinates.latitudeE7,
                    longitudeE7 = endCoordinates.longitudeE7,
                ),
            ),
        )
    }
}

@Serializable
data class SemanticActivityCandidateDto(
    val type: String? = null,
)

@Serializable
data class LatLngStringDto(
    val latLng: String? = null,
) {
    fun toCoordinates(): E7Coordinates? {
        val match = latLng?.let(COORDINATE_PATTERN::matchEntire) ?: return null
        val latitude = match.groupValues[1].toDoubleOrNull() ?: return null
        val longitude = match.groupValues[2].toDoubleOrNull() ?: return null

        if (latitude !in -90.0..90.0 || longitude !in -180.0..180.0) {
            return null
        }

        return E7Coordinates(
            latitudeE7 = (latitude * E7_SCALE).roundToInt(),
            longitudeE7 = (longitude * E7_SCALE).roundToInt(),
        )
    }

    companion object {
        private const val E7_SCALE = 10_000_000.0
        private val COORDINATE_PATTERN = Regex(
            pattern = """^\s*(-?\d+(?:\.\d+)?)°?\s*,\s*(-?\d+(?:\.\d+)?)°?\s*$""",
        )
    }
}

data class E7Coordinates(
    val latitudeE7: Int,
    val longitudeE7: Int,
)

private fun String?.toLocationName(): String = when (this) {
    "HOME", "INFERRED_HOME" -> "Home"
    "WORK", "INFERRED_WORK" -> "Work"
    "ALIASED_LOCATION" -> "Saved place"
    "SEARCHED_ADDRESS" -> "Searched address"
    else -> "Visited place"
}
