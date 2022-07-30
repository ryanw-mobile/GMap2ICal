package uk.ryanwong.gmap2ics.data.repository

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.PlaceDetails
import uk.ryanwong.gmap2ics.app.models.TimelineItem
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.configs.Config
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.ActivitySegment
import uk.ryanwong.gmap2ics.data.source.googleapi.models.timeline.TimelineObjects
import us.dustinj.timezonemap.TimeZoneMap
import java.io.File
import kotlin.coroutines.cancellation.CancellationException

class TimelineRepositoryImpl(
    private val configFile: Config,
    private val placeDetailsRepository: PlaceDetailsRepositoryImpl
) : TimelineRepository {
    private val timeZoneMap = TimeZoneMap.forEverywhere()
    private val objectMapper = jacksonObjectMapper().apply {
        registerKotlinModule()
        registerModule(JavaTimeModule())
    }

    override suspend fun getEventList(filePath: String): Result<List<VEvent>> {
        return withContext(Dispatchers.IO) {
            Result.runCatching {
                val eventList = mutableListOf<VEvent>()
                val timeline = parseTimeLine(filePath = filePath)


                timeline.getOrNull()?.timelineObjects?.let { timelineDataObjects ->
                    for (timelineDataObject in timelineDataObjects) {
                        // Should be either activity or place visited, but no harm to also support cases with both
                        if (configFile.exportActivitySegment) {
                            timelineDataObject.activitySegment?.let { activitySegment ->
                                val gMapTimelineObject = processActivitySegment(activitySegment = activitySegment)

                                gMapTimelineObject.getOrNull()?.let { timelineObject ->
                                    eventList.add(VEvent.from(timelineItem = timelineObject).also { vEvent ->
                                        if (configFile.displayLogs) println(vEvent.toString())
                                    })
                                }
                            }
                        }

                        if (configFile.exportPlaceVisit) {
                            timelineDataObject.placeVisit?.let { placeVisit ->
                                val placeDetails: PlaceDetails? =
                                    if (configFile.enablePlacesApiLookup && placeVisit.location.placeId != null) placeDetailsRepository.getPlaceDetails(
                                        placeId = placeVisit.location.placeId,
                                        placeTimeZoneId = placeVisit.getEventTimeZone(timeZoneMap)?.zoneId
                                    ).getOrNull()
                                    else null

                                val gMapTimelineObject =
                                    placeVisit.asTimelineItem(
                                        timeZoneMap = timeZoneMap,
                                        placeDetails = placeDetails
                                    )

                                if (!configFile.ignoredVisitedPlaceIds.contains(gMapTimelineObject.placeId)) {
                                    eventList
                                        .add(VEvent.from(timelineItem = gMapTimelineObject)
                                            .also { vEvent ->
                                                if (configFile.displayLogs) println(vEvent.toString())
                                            })
                                }

                                // If we have child-visits, we export them as individual events
                                // ChildVisit might have unconfirmed location which does not have a duration
                                placeVisit.childVisits?.forEach { childVisit ->
                                    if (configFile.ignoredVisitedPlaceIds.contains(childVisit.location.placeId)) {
                                        return@forEach
                                    }

                                    val childPlaceDetails: PlaceDetails? =
                                        if (configFile.enablePlacesApiLookup && childVisit.location.placeId != null)
                                            placeDetailsRepository.getPlaceDetails(
                                                placeId = childVisit.location.placeId,
                                                placeTimeZoneId = childVisit.getEventTimeZone(timeZoneMap)?.zoneId
                                            ).getOrNull()
                                        else null

                                    childVisit.asTimelineItem(timeZoneMap, placeDetails = childPlaceDetails)
                                        ?.let { timelineObject ->
                                            eventList
                                                .add(VEvent.from(timelineItem = timelineObject))
                                                .also { vEvent ->
                                                    if (configFile.displayLogs) println(vEvent.toString())
                                                }
                                        }
                                }
                            }
                        }
                    }
                }

                println("✅ Processed ${timeline?.getOrNull()?.timelineObjects?.size ?: 0} timeline entries.")
                eventList

            }.except<CancellationException, _>()
        }
    }

    override suspend fun parseTimeLine(filePath: String): Result<TimelineObjects> {
        return Result.runCatching {
            val jsonString = File(filePath).readText(Charsets.UTF_8)
            val retVal: TimelineObjects = objectMapper.readValue(content = jsonString)
            retVal
        }.except<CancellationException, _>()
    }

    override suspend fun processActivitySegment(activitySegment: ActivitySegment): Result<TimelineItem> {
        return Result.runCatching {
            // Convert to enum
            val activityType = activitySegment.activityType?.let {
                try {
                    ActivityType.valueOf(activitySegment.activityType)
                } catch (e: IllegalArgumentException) {
                    if (configFile.displayLogs) {
                        println("⚠️ Unknown activity type: ${activitySegment.activityType}")
                    }
                    ActivityType.UNKNOWN_ACTIVITY_TYPE
                }
            } ?: ActivityType.UNKNOWN_ACTIVITY_TYPE

            if (configFile.ignoredActivityType.contains(activityType)) {
                if (configFile.displayLogs) {
                    throw Exception("🚫 Ignored activity type ${activitySegment.activityType} at ${activitySegment.duration.startTimestamp}")
                }
            }

            activitySegment.asTimelineItem(
                timeZoneMap = timeZoneMap,
                placeDetailsRepository = placeDetailsRepository
            )
        }.except<CancellationException, _>()
    }
}