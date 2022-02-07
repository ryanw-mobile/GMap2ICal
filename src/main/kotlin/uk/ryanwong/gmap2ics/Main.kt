package uk.ryanwong.gmap2ics

import uk.ryanwong.gmap2ics.configs.RyanConfig
import uk.ryanwong.gmap2ics.data.ICalExporter
import uk.ryanwong.gmap2ics.data.JsonProcessor
import uk.ryanwong.gmap2ics.data.models.ActivitySegment
import uk.ryanwong.gmap2ics.data.models.PlaceVisit
import uk.ryanwong.gmap2ics.domain.ActivityType
import uk.ryanwong.gmap2ics.domain.models.GMapTimelineObject
import uk.ryanwong.gmap2ics.domain.models.VEvent
import java.nio.file.Files
import java.nio.file.Paths

private val configFile = RyanConfig() // Specify your config here

fun main(args: Array<String>) {
    val jsonProcessor = JsonProcessor()
    val fileList: List<String>?

    try {
        fileList = getResourcesJsonFileList(absolutePath = configFile.jsonPath)
    } catch (ex: Exception) {
        println("☠️ Error getting json file list: ${ex.localizedMessage}")
        return
    }

    fileList.forEach { filename ->
        println("\uD83D\uDDC2 Processing $filename")

        val eventList: MutableList<VEvent> = mutableListOf()

        val timeline = jsonProcessor.parseTimeLine(
            filename = filename
        )

        val iCalExporter = ICalExporter(configFile.icalPath)

        timeline?.timelineObjects?.let { timelineObjects ->
            for (timelineObject in timelineObjects) {
                // Should be either activity or place visited, but no harm to also support cases with both
                timelineObject.activitySegment?.let { activitySegment ->
                    processActivitySegment(activitySegment = activitySegment)?.let { gMapTimelineObject ->
                        eventList.add(VEvent.from(timelineObject = gMapTimelineObject).also { vEvent ->
                            if (configFile.displayLogs) println(vEvent.toString())
                        })
                    }
                }
                timelineObject.placeVisit?.let { placeVisit ->
                    processPlaceVisit(placeVisit = placeVisit)?.let { gMapTimelineObject ->
                        eventList.add(VEvent.from(timelineObject = gMapTimelineObject).also { vEvent ->
                            if (configFile.displayLogs) println(vEvent.toString())
                        })
                    }
                }
            }
        }

        // Exporting multiple events in one single ics file
        iCalExporter.exportICal(
            filename = filename.replace(".json", ".ics"), // casually reuse the filename
            vEvents = eventList
        )

        println("✅ Processed ${timeline?.timelineObjects?.size ?: 0} events")
    }
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
    val timelineObject = GMapTimelineObject.from(activitySegment)
    // println(obj.toString())
    return timelineObject
}

fun processPlaceVisit(placeVisit: PlaceVisit): GMapTimelineObject? {
    val timelineObject = GMapTimelineObject.from(placeVisit)
    if (configFile.ignoredVisitedLocations.contains(placeVisit.location?.name)) {
        return null
    }

    // println(obj.toString())
    return timelineObject
}