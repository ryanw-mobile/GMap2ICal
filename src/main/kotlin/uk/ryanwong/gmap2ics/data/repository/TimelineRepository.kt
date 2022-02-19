package uk.ryanwong.gmap2ics.data.repository

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import uk.ryanwong.gmap2ics.configs.Config
import uk.ryanwong.gmap2ics.data.models.timeline.ActivitySegment
import uk.ryanwong.gmap2ics.data.models.timeline.PlaceVisit
import uk.ryanwong.gmap2ics.data.models.timeline.TimelineObjects
import uk.ryanwong.gmap2ics.domain.ActivityType
import uk.ryanwong.gmap2ics.domain.models.GMapTimelineObject
import uk.ryanwong.gmap2ics.domain.models.VEvent
import java.io.File

class TimelineRepository(private val configFile: Config) {
    private val objectMapper = jacksonObjectMapper().apply {
        registerKotlinModule()
        registerModule(JavaTimeModule())
    }

    fun getEventList(filePath: String): List<VEvent> {
        val eventList = mutableListOf<VEvent>()

        val timeline = parseTimeLine(
            filePath = filePath
        )

        timeline?.timelineObjects?.let { timelineObjects ->
            for (timelineObject in timelineObjects) {
                // Should be either activity or place visited, but no harm to also support cases with both
                if (configFile.exportActivitySegment)
                    timelineObject.activitySegment?.let { activitySegment ->
                        processActivitySegment(activitySegment = activitySegment)?.let { gMapTimelineObject ->
                            eventList.add(VEvent.from(timelineObject = gMapTimelineObject).also { vEvent ->
                                if (configFile.displayLogs) println(vEvent.toString())
                            })
                        }
                    }

                if (configFile.exportPlaceVisit)
                    timelineObject.placeVisit?.let { placeVisit ->
                        processPlaceVisit(placeVisit = placeVisit)?.let { gMapTimelineObject ->
                            eventList.add(VEvent.from(timelineObject = gMapTimelineObject).also { vEvent ->
                                if (configFile.displayLogs) println(vEvent.toString())
                            })
                        }

                        // If we have child-visits, we export them as individual events
                        placeVisit.childVisits?.forEach { childVisit ->
                            eventList.add(VEvent.from(GMapTimelineObject.from(childVisit)))
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
        val activityType = activitySegment.activityType?.let {
            try {
                ActivityType.valueOf(activitySegment.activityType)
            } catch (e: IllegalArgumentException) {
                if (configFile.displayLogs) {
                    println("⚠️  ${activitySegment.activityType}")
                }
                ActivityType.UNKNOWN_ACTIVITY_TYPE
            }
        } ?: ActivityType.UNKNOWN_ACTIVITY_TYPE

        if (configFile.ignoredActivityType.contains(activityType)) {
            // println("🚫 ${activitySegment.activityType}")
            return null
        }
        // println(obj.toString())
        return GMapTimelineObject.from(activitySegment)
    }

    private fun processPlaceVisit(placeVisit: PlaceVisit): GMapTimelineObject? {
        val timelineObject = GMapTimelineObject.from(placeVisit)
        if (configFile.ignoredVisitedLocations.contains(placeVisit.location?.name)) {
            return null
        }

        // println(obj.toString())
        return timelineObject
    }


}