package uk.ryanwong.gmap2ics.ui.models

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ryanwong.gmap2ics.configs.Config
import uk.ryanwong.gmap2ics.data.ICalExporter
import uk.ryanwong.gmap2ics.data.getFileList
import uk.ryanwong.gmap2ics.data.repository.TimelineRepository
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.ui.MainScreenUIState

class MainScreenViewModel(
    val configFile: Config,
    val timelineRepository: TimelineRepository
) {
    private var _mainScreenUIState = MutableStateFlow(MainScreenUIState.READY)
    val mainScreenUIState: StateFlow<MainScreenUIState> = _mainScreenUIState

    private var _statusMessage = MutableStateFlow("")
    val statusMessage: StateFlow<String> = _statusMessage

    private var _jsonPath = MutableStateFlow("")
    val jsonPath: StateFlow<String> = _jsonPath

    private var _iCalPath = MutableStateFlow("")
    val iCalPath: StateFlow<String> = _iCalPath

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
            _iCalPath.value = icalPath
            _jsonPath.value = jsonPath
            _exportPlaceVisit.value = exportPlaceVisit
            _exportActivitySegment.value = exportActivitySegment
            _enablePlacesApiLookup.value = enablePlacesApiLookup
        }
    }

    fun startConvertion() {

        val fileList = getFileList(
            absolutePath = _jsonPath.value,
            extension = "json"
        )

        val filenameSuffix = if (_exportPlaceVisit.value && _exportActivitySegment.value) "_all"
        else if (_exportPlaceVisit.value) "_places"
        else "_activities"

        CoroutineScope(Dispatchers.Default).launch {
            fileList?.forEach { filename ->
                appendStatus(status = "\uD83D\uDDC2 Processing $filename")
                val eventList: List<VEvent> = timelineRepository.getEventList(filePath = filename)

                // Exporting multiple events in one single ics file
                ICalExporter.exportICal(
                    filename = filename.replace(oldValue = _jsonPath.value, newValue = _iCalPath.value)
                        .replace(oldValue = ".json", newValue = "$filenameSuffix.ics"), // casually reuse the filename
                    vEvents = eventList
                )
            }
        }
    }

    fun setExportPlaceVisit(enabled: Boolean) {
        _exportPlaceVisit.value = enabled
    }

    fun setExportActivitySegment(enabled: Boolean) {
        _exportActivitySegment.value = enabled
    }

    fun setEnablePlacesApiLookup(enabled: Boolean) {
        _enablePlacesApiLookup.value = enabled
    }

    fun onChangeJsonPath() {
        _mainScreenUIState.value = MainScreenUIState.SHOW_CHANGE_JSON_PATH_DIALOG
    }

    fun onChangeICalPath() {
        _mainScreenUIState.value = MainScreenUIState.SHOW_CHANGE_ICAL_PATH_DIALOG
    }

    fun setJsonPath(path: String?) {
        path?.let { _jsonPath.value = it }
    }

    fun setICalPath(path: String?) {
        path?.let { _iCalPath.value = it }
    }

    private fun appendStatus(status: String) {
        _statusMessage.value.plus("\n$status")
    }
}