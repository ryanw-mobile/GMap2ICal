/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.usecases

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivitySegment
import us.dustinj.timezonemap.TimeZoneMap

class ExportActivitySegmentUseCaseImpl(
    private val placeDetailsRepository: PlaceDetailsRepository,
    private val timeZoneMap: TimeZoneMap = TimeZoneMap.forEverywhere(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExportActivitySegmentUseCase {

    private var _statusLog: MutableStateFlow<String?> = MutableStateFlow(null)
    override val statusLog: StateFlow<String?> = _statusLog

    override suspend operator fun invoke(
        activitySegment: ActivitySegment,
        ignoredActivityType: List<ActivityType>,
        verboseLogs: Boolean
    ): Result<VEvent> {
        return withContext(dispatcher) {
            Result.runCatching {
                // Convert to enum
                val activityType = activitySegment.activityType?.let {
                    try {
                        ActivityType.valueOf(activitySegment.activityType)
                    } catch (e: IllegalArgumentException) {
                        if (verboseLogs) {
                            _statusLog.value = "⚠️ Unknown activity type: ${activitySegment.activityType}"
                        }
                        ActivityType.UNKNOWN_ACTIVITY_TYPE
                    }
                } ?: ActivityType.UNKNOWN_ACTIVITY_TYPE

                if (ignoredActivityType.contains(activityType)) {
                    throw Exception("🚫 Ignored activity type ${activitySegment.activityType} at ${activitySegment.duration.startTimestamp}")
                }

                val gMapTimelineObject = activitySegment.asTimelineItem(
                    timeZoneMap = timeZoneMap,
                    placeDetailsRepository = placeDetailsRepository
                )

                VEvent.from(timelineItem = gMapTimelineObject)
            }.except<CancellationException, _>()
        }
    }
}