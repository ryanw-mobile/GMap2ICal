package uk.ryanwong.gmap2ics.ui.models

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uk.ryanwong.gmap2ics.configs.Config
import uk.ryanwong.gmap2ics.data.ICalExporter
import uk.ryanwong.gmap2ics.data.getFileList
import uk.ryanwong.gmap2ics.data.repository.TimelineRepository
import uk.ryanwong.gmap2ics.domain.models.VEvent

class MainScreenViewModel(
    val configFile: Config,
    val timelineRepository: TimelineRepository
) {
    private var _statusMessage = MutableStateFlow("")
    val statusMessage: StateFlow<String> = _statusMessage

    fun startConvertion() {

        val fileList = getFileList(
            absolutePath = configFile.jsonPath,
            extension = "json"
        )

        val filenameSuffix = if (configFile.exportPlaceVisit && configFile.exportActivitySegment) "_all"
        else if (configFile.exportPlaceVisit) "_places"
        else "_activities"

        fileList?.forEach { filename ->
            appendStatus(status = "\uD83D\uDDC2 Processing $filename")
            val eventList: List<VEvent> = timelineRepository.getEventList(filePath = filename)

            // Exporting multiple events in one single ics file
            ICalExporter.exportICal(
                filename = filename.replace(oldValue = configFile.jsonPath, newValue = configFile.icalPath)
                    .replace(oldValue = ".json", newValue = "$filenameSuffix.ics"), // casually reuse the filename
                vEvents = eventList
            )
        }
    }

    private fun appendStatus(status: String) {
        _statusMessage.value.plus("\n$status")
    }
}