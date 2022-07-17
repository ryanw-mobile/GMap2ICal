package uk.ryanwong.gmap2ics.ui.models

import androidx.compose.ui.text.input.TextFieldValue
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

    private var _jsonPath = MutableStateFlow(TextFieldValue(""))
    val jsonPath: StateFlow<TextFieldValue> = _jsonPath

    private var _iCalPath = MutableStateFlow(TextFieldValue(""))
    val iCalPath: StateFlow<TextFieldValue> = _iCalPath

    private var _exportPlaceVisit = MutableStateFlow(false)
    val exportPlaceVisit: StateFlow<Boolean> = _exportPlaceVisit

    private var _exportActivitySegment = MutableStateFlow(false)
    val exportActivitySegment: StateFlow<Boolean> = _exportActivitySegment

    private var _enablePlacesApiLookup = MutableStateFlow(false)
    val enablePlacesApiLookup: StateFlow<Boolean> = _enablePlacesApiLookup

    init {
        // Default values, overridable from UI
        // TODO: might provide as profiles
        with(configFile) {
            _iCalPath.value = TextFieldValue(text = icalPath)
            _jsonPath.value = TextFieldValue(text = jsonPath)
            _exportPlaceVisit.value = exportPlaceVisit
            _exportActivitySegment.value = exportActivitySegment
            _enablePlacesApiLookup.value = enablePlacesApiLookup
        }
    }

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