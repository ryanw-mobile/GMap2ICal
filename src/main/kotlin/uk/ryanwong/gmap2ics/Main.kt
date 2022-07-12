package uk.ryanwong.gmap2ics

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import uk.ryanwong.gmap2ics.configs.RyanConfig
import uk.ryanwong.gmap2ics.data.ICalExporter
import uk.ryanwong.gmap2ics.data.getFileList
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.repository.TimelineRepository
import uk.ryanwong.gmap2ics.domain.models.VEvent


private val configFile = RyanConfig() // Specify your config here

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "GMap2iCal - Google Maps to iCal",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        MaterialTheme {
            Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
                Text(
                    text = "Google Maps to iCal converter",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 24.dp),
                )
                Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { legacyMain() })
                {
                    Text("Convert")
                }
            }
        }
    }
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
