/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.TimelineItem
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivitySegment
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ChildVisit
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.PlaceVisit
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.TimelineObjects
import us.dustinj.timezonemap.TimeZoneMap
import java.io.File
import kotlin.coroutines.cancellation.CancellationException

class TimelineRepositoryImpl(
    private val timeZoneMap: TimeZoneMap = TimeZoneMap.forEverywhere(),
    // TODO: Should refactor so it doesn't depend on placeDetailRepository
    private val placeDetailsRepository: PlaceDetailsRepositoryImpl,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TimelineRepository {
    private val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
        registerKotlinModule()
        registerModule(JavaTimeModule())
    }

    private var _statusLog: MutableStateFlow<String?> = MutableStateFlow(null)
    override val statusLog: StateFlow<String?> = _statusLog

    // TODO: Should be ViewModel use-case accessing two repositories
    override suspend fun getEventList(
        filePath: String,
        ignoredVisitedPlaceIds: List<String>,
        ignoredActivityType: List<ActivityType>,
        exportActivitySegment: Boolean,
        exportPlaceVisit: Boolean,
        verboseLogs: Boolean,
        enablePlacesApiLookup: Boolean
    ): Result<List<VEvent>> {
        return withContext(dispatcher) {
            Result.runCatching {
                val eventList = mutableListOf<VEvent>()
                val timeline = parseTimeLine(filePath = filePath)

                timeline.getOrNull()?.timelineObjects?.let { timelineDataObjects ->
                    for (timelineDataObject in timelineDataObjects) {
                        // Should be either activity or place visited, but no harm to also support cases with both
                        if (exportActivitySegment) {
                            timelineDataObject.activitySegment?.let { activitySegment ->
                                exportActivitySegment(
                                    gMapTimelineObject = getActivityTimelineItem(
                                        activitySegment = activitySegment,
                                        ignoredActivityType = ignoredActivityType,
                                        verboseLogs = verboseLogs
                                    ),
                                    verboseLogs = verboseLogs
                                )?.let { vEvent ->
                                    eventList.add(vEvent)
                                    if (verboseLogs) _statusLog.value = vEvent.toString()
                                }
                            }
                        }

                        if (exportPlaceVisit) {
                            timelineDataObject.placeVisit?.let { placeVisit ->
                                exportPlaceVisit(
                                    placeVisit = placeVisit,
                                    enablePlacesApiLookup = enablePlacesApiLookup,
                                    ignoredVisitedPlaceIds = ignoredVisitedPlaceIds
                                )?.let { vEvent ->
                                    eventList.add(vEvent)
                                    if (verboseLogs) _statusLog.value = vEvent.toString()
                                }

                                // If we have child-visits, we export them as individual events
                                // ChildVisit might have unconfirmed location which does not have a duration
                                placeVisit.childVisits?.forEach { childVisit ->
                                    exportChildVisit(
                                        childVisit = childVisit,
                                        ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                                        enablePlacesApiLookup = enablePlacesApiLookup
                                    )?.let { vEvent ->
                                        eventList.add(vEvent)
                                        if (verboseLogs) _statusLog.value = vEvent.toString()
                                    }
                                }
                            }
                        }
                    }
                }

                _statusLog.value = "✅ Processed ${timeline?.getOrNull()?.timelineObjects?.size ?: 0} timeline entries."
                eventList

            }.except<CancellationException, _>()
        }
    }

    private fun parseTimeLine(filePath: String): Result<TimelineObjects> {
        // Caller already picked the dispatcher
        return Result.runCatching {
            val jsonString = File(filePath).readText(Charsets.UTF_8)
            val retVal: TimelineObjects = objectMapper.readValue(content = jsonString)
            retVal
        }.except<CancellationException, _>()
    }

    private suspend fun exportActivitySegment(gMapTimelineObject: Result<TimelineItem>, verboseLogs: Boolean): VEvent? {
        if (gMapTimelineObject.isSuccess) {
            gMapTimelineObject.getOrNull()?.let { timelineObject ->
                VEvent.from(timelineItem = timelineObject)
            }
        }

        if (gMapTimelineObject.isFailure && verboseLogs) {
            gMapTimelineObject.exceptionOrNull()?.let { _statusLog.value = it.message }
        }
        return null
    }

    private suspend fun getActivityTimelineItem(
        activitySegment: ActivitySegment,
        ignoredActivityType: List<ActivityType>,
        verboseLogs: Boolean
    ): Result<TimelineItem> {
        // Caller already picked the dispatcher
        return Result.runCatching {
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

            activitySegment.asTimelineItem(
                timeZoneMap = timeZoneMap,
                placeDetailsRepository = placeDetailsRepository
            )
        }.except<CancellationException, _>()
    }

    private suspend fun exportPlaceVisit(
        placeVisit: PlaceVisit,
        enablePlacesApiLookup: Boolean,
        ignoredVisitedPlaceIds: List<String>
    ): VEvent? {
        val placeDetails: PlaceDetails? =
            if (enablePlacesApiLookup && placeVisit.location.placeId != null) placeDetailsRepository.getPlaceDetails(
                placeId = placeVisit.location.placeId,
                placeTimeZoneId = placeVisit.getEventTimeZone(timeZoneMap)?.zoneId
            ).getOrNull()
            else null

        val gMapTimelineObject = placeVisit.asTimelineItem(timeZoneMap = timeZoneMap, placeDetails = placeDetails)

        return if (!ignoredVisitedPlaceIds.contains(gMapTimelineObject.placeId)) {
            VEvent.from(timelineItem = gMapTimelineObject)
        } else null
    }

    private suspend fun exportChildVisit(
        childVisit: ChildVisit,
        ignoredVisitedPlaceIds: List<String>,
        enablePlacesApiLookup: Boolean
    ): VEvent? {
        // If we have child-visits, we export them as individual events
        // ChildVisit might have unconfirmed location which does not have a duration

        if (ignoredVisitedPlaceIds.contains(childVisit.location.placeId)) {
            return null
        }

        val childPlaceDetails: PlaceDetails? =
            if (enablePlacesApiLookup && childVisit.location.placeId != null)
                placeDetailsRepository.getPlaceDetails(
                    placeId = childVisit.location.placeId,
                    placeTimeZoneId = childVisit.getEventTimeZone(timeZoneMap)?.zoneId
                ).getOrNull()
            else null

        return childVisit.asTimelineItem(timeZoneMap = timeZoneMap, placeDetails = childPlaceDetails)
            ?.let { timelineItem ->
                VEvent.from(timelineItem = timelineItem)
            }
    }
}