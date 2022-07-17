package uk.ryanwong.gmap2ics

import androidx.compose.ui.window.application
import uk.ryanwong.gmap2ics.configs.RyanConfig
import uk.ryanwong.gmap2ics.data.ICalExporter
import uk.ryanwong.gmap2ics.data.getFileList
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.repository.TimelineRepository
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.ui.mainScreen


private val configFile = RyanConfig() // Specify your config here

fun main() = application {
    mainScreen(onCloseRequest = { ::exitApplication })
}

fun legacyMain() {
    val placeDetailsRepository = PlaceDetailsRepository(configFile = configFile)
    val timelineRepository = TimelineRepository(
        configFile = configFile,
        placeDetailsRepository = placeDetailsRepository
    )
    val fileList = getFileList(
        absolutePath = configFile.jsonPath,
        extension = "json"
    )

    val filenameSuffix = if (configFile.exportPlaceVisit && configFile.exportActivitySegment) "_all"
    else if (configFile.exportPlaceVisit) "_places"
    else "_activities"

    fileList?.forEach { filename ->
        println("\uD83D\uDDC2 Processing $filename")
        val eventList: List<VEvent> = timelineRepository.getEventList(filePath = filename)

        // Exporting multiple events in one single ics file
        ICalExporter.exportICal(
            filename = filename.replace(oldValue = configFile.jsonPath, newValue = configFile.icalPath)
                .replace(oldValue = ".json", newValue = "$filenameSuffix.ics"), // casually reuse the filename
            vEvents = eventList
        )
    }
}
