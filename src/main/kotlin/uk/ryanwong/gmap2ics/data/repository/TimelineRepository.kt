package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.configs.Config
import uk.ryanwong.gmap2ics.data.models.ActivitySegment
import uk.ryanwong.gmap2ics.data.models.PlaceVisit
import uk.ryanwong.gmap2ics.domain.ActivityType
import uk.ryanwong.gmap2ics.domain.models.GMapTimelineObject
import uk.ryanwong.gmap2ics.domain.models.VEvent
import java.nio.file.Files
import java.nio.file.Paths

class TimelineRepository(private val configFile: Config) {
    val jsonProcessor = JsonProcessor()

    fun getFileList(): List<String>? {
        return try {
            getResourcesJsonFileList(absolutePath = configFile.jsonPath)
        } catch (ex: Exception) {
            println("☠️ Error getting json file list: ${ex.localizedMessage}")
            null
        }
    }

    fun getEventList(filePath: String): List<VEvent> {
        val eventList = mutableListOf<VEvent>()

        val timeline = jsonProcessor.parseTimeLine(
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

    /**
     * Prepare a list of json files to be processed
     */
    private fun getResourcesJsonFileList(absolutePath: String): List<String> {
        val fileList = mutableListOf<String>()
        val projectDirAbsolutePath = Paths.get("").toAbsolutePath().toString()
        val resourcesPath = Paths.get(projectDirAbsolutePath, absolutePath)

        Files.walk(resourcesPath)
            .filter { file -> Files.isRegularFile(file) }
            .filter { file -> file.toString().endsWith(suffix = ".json") }
            .forEach { file -> fileList.add(file.toString()) }
        return fileList
    }

    fun processActivitySegment(activitySegment: ActivitySegment): GMapTimelineObject? {
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

    fun processPlaceVisit(placeVisit: PlaceVisit): GMapTimelineObject? {
        val timelineObject = GMapTimelineObject.from(placeVisit)
        if (configFile.ignoredVisitedLocations.contains(placeVisit.location?.name)) {
            return null
        }

        // println(obj.toString())
        return timelineObject
    }
}