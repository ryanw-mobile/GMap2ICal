/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import kotlinx.coroutines.CancellationException
import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivitySegment
import us.dustinj.timezonemap.TimeZone
import us.dustinj.timezonemap.TimeZoneMap

class ExportActivitySegmentUseCaseImpl(
    private val placeDetailsRepository: PlaceDetailsRepository,
    private val timeZoneMap: TimeZoneMap = TimeZoneMap.forEverywhere()
) : ExportActivitySegmentUseCase {

    override suspend operator fun invoke(
        activitySegment: ActivitySegment,
        ignoredActivityType: List<ActivityType>
    ): Result<Pair<VEvent, String?>> {
        return Result.runCatching {
            var statusLog: String? = null

            // Convert to enum
            val activityType = activitySegment.activityType?.let { activityType ->
                try {
                    ActivityType.valueOf(activityType)
                } catch (e: IllegalArgumentException) {
                    statusLog = "⚠️ Unknown activity type: $activityType"
                    ActivityType.UNKNOWN_ACTIVITY_TYPE
                }
            } ?: ActivityType.UNKNOWN_ACTIVITY_TYPE

            if (ignoredActivityType.contains(activityType)) {
                throw IgnoredActivityTypeException(
                    activityType = activitySegment.activityType,
                    startTimestamp = activitySegment.duration.startTimestamp
                )
            }

            // Extra information required by timelineItem
            val eventTimeZone = activitySegment.getEventTimeZone(timeZoneMap = timeZoneMap)
            val firstPlaceDetail = activitySegment.waypointPath?.roadSegment?.first()?.placeId?.let { placeId ->
                placeDetailsRepository.getPlaceDetails(
                    placeId = placeId,
                    placeTimeZoneId = eventTimeZone?.zoneId
                ).getOrNull()
            }

            val lastPlaceDetail = activitySegment.waypointPath?.roadSegment?.last()?.placeId?.let { placeId ->
                placeDetailsRepository.getPlaceDetails(
                    placeId = placeId,
                    placeTimeZoneId = eventTimeZone?.zoneId
                ).getOrNull()
            }

            // If Location API enabled, try to fetch starting and ending from there
            val startPlaceDetail = activitySegment.startLocation.placeId?.let { placeId ->
                placeDetailsRepository.getPlaceDetails(
                    placeId = placeId,
                    placeTimeZoneId = eventTimeZone?.zoneId
                ).getOrNull()
            }
            val endPlaceDetail = activitySegment.endLocation.placeId?.let { placeId ->
                placeDetailsRepository.getPlaceDetails(
                    placeId = placeId,
                    placeTimeZoneId = eventTimeZone?.zoneId
                ).getOrNull()
            }

            val timelineItem = activitySegment.asTimelineItem(
                shouldShowMiles = shouldShowMiles(eventTimeZone),
                firstPlaceDetail = firstPlaceDetail,
                lastPlaceDetail = lastPlaceDetail,
                startPlaceDetail = startPlaceDetail,
                endPlaceDetail = endPlaceDetail,
                eventTimeZone = eventTimeZone
            )

            Pair(VEvent.from(timelineItem = timelineItem), statusLog)
        }.except<CancellationException, _>()
    }

    /**
     * Only when the activity happened in UK we display values in miles.
     * Others are in kilometers.
     */
    private fun shouldShowMiles(timezone: TimeZone?): Boolean {
        return timezone?.zoneId == "Europe/London"
    }
}

class IgnoredActivityTypeException(val activityType: String?, startTimestamp: String) : Exception() {
    override val message: String = "🚫 Ignored activity type $activityType at $startTimestamp"
}