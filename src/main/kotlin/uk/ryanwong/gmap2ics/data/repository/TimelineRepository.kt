package uk.ryanwong.gmap2ics.data.repository

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.runBlocking
import uk.ryanwong.gmap2ics.configs.Config
import uk.ryanwong.gmap2ics.data.models.timeline.ActivitySegment
import uk.ryanwong.gmap2ics.data.models.timeline.TimelineObjects
import uk.ryanwong.gmap2ics.domain.ActivityType
import uk.ryanwong.gmap2ics.domain.models.GMapTimelineObject
import uk.ryanwong.gmap2ics.domain.models.PlaceDetails
import uk.ryanwong.gmap2ics.domain.models.VEvent
import us.dustinj.timezonemap.TimeZoneMap
import java.io.File

class TimelineRepository(
    private val configFile: Config,
    private val placeDetailsRepository: PlaceDetailsRepository
) {
    private val timeZoneMap = TimeZoneMap.forEverywhere()
    private val objectMapper = jacksonObjectMapper().apply {
        registerKotlinModule()
        registerModule(JavaTimeModule())
    }

    fun getEventList(filePath: String): List<VEvent> {
        val eventList = mutableListOf<VEvent>()

        val timeline = parseTimeLine(
            filePath = filePath
        )

       runBlocking {
            timeline?.timelineObjects?.let { timelineDataObjects ->
                for (timelineDataObject in timelineDataObjects) {
                    // Should be either activity or place visited, but no harm to also support cases with both
                    if (configFile.exportActivitySegment)
                        timelineDataObject.activitySegment?.let { activitySegment ->
                            val gMapTimelineObject = processActivitySegment(activitySegment = activitySegment)

                            gMapTimelineObject?.let { timelineObject ->
                                eventList.add(VEvent.from(timelineObject = timelineObject).also { vEvent ->
                                    if (configFile.displayLogs) println(vEvent.toString())
                                })
                            }
                        }

                    if (configFile.exportPlaceVisit)
                        timelineDataObject.placeVisit?.let { placeVisit ->
                            val placeDetails: PlaceDetails? =
                                if (configFile.enablePlacesApiLookup) placeDetailsRepository.getPlaceDetails(
                                    placeId = placeVisit.location.placeId,
                                    placeTimeZoneId = placeVisit.getEventTimeZone(timeZoneMap)?.zoneId
                                ) else null

                            val gMapTimelineObject =
                                placeVisit.toGMapTimelineObject(
                                    timeZoneMap = timeZoneMap,
                                    placeDetails = placeDetails
                                )

                            if (!configFile.ignoredVisitedPlaceIds.contains(gMapTimelineObject.placeId)) {
                                eventList
                                    .add(VEvent.from(timelineObject = gMapTimelineObject)
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
                                    if (configFile.enablePlacesApiLookup)
                                        placeDetailsRepository.getPlaceDetails(
                                            placeId = childVisit.location.placeId,
                                            placeTimeZoneId = childVisit.getEventTimeZone(timeZoneMap)?.zoneId
                                        )
                                    else null

                                childVisit.toGMapTimelineObject(timeZoneMap, placeDetails = childPlaceDetails)
                                    ?.let { timelineObject ->
                                        eventList
                                            .add(VEvent.from(timelineObject = timelineObject))
                                            .also { vEvent ->
                                                if (configFile.displayLogs) println(vEvent.toString())
                                            }
                                    }
                            }
                        }
                }
            }
        }

        println("✅ Processed ${timeline?.timelineObjects?.size ?: 0} timeline entries.")
        return eventList
    }

    private fun parseTimeLine(filePath: String): TimelineObjects? {
        val jsonString: String
        try {
            jsonString = File(filePath).readText(Charsets.UTF_8)
        } catch (npe: NullPointerException) {
            return null
        }

        return objectMapper.readValue(content = jsonString)
    }

    private fun processActivitySegment(activitySegment: ActivitySegment): GMapTimelineObject? {
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
                println("🚫 Ignored activity type ${activitySegment.activityType} at ${activitySegment.duration.startTimestamp}")
            }
            return null
        }
        return activitySegment.toGMapTimelineObject(timeZoneMap)
    }
}