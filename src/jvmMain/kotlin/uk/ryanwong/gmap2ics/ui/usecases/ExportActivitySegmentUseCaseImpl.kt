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

            val timelineItem = activitySegment.asTimelineItem(
                timeZoneMap = timeZoneMap,
                placeDetailsRepository = placeDetailsRepository
            )

            Pair(VEvent.from(timelineItem = timelineItem), statusLog)
        }.except<CancellationException, _>()
    }
}

class IgnoredActivityTypeException(val activityType: String?, startTimestamp: String) : Exception() {
    override val message: String = "🚫 Ignored activity type $activityType at $startTimestamp"
}