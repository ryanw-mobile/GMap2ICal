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
import uk.ryanwong.gmap2ics.domain.models.JFileChooserResult
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.ui.MainScreenUIState
import uk.ryanwong.gmap2ics.ui.utils.DefaultResourceBundle
import uk.ryanwong.gmap2ics.ui.utils.ResourceBundleWrapper
import java.nio.file.Paths

class MainScreenViewModel(
    val configFile: Config,
    val timelineRepository: TimelineRepository,
    private val resourceBundle: ResourceBundleWrapper = DefaultResourceBundle(),
    private val projectBasePath: String = Paths.get("").toAbsolutePath().toString().plus("/")
) {
    private var _mainScreenUIState: MutableStateFlow<MainScreenUIState> = MutableStateFlow(MainScreenUIState.Ready)
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
        if (_mainScreenUIState.value is MainScreenUIState.Ready) {
            _mainScreenUIState.value = MainScreenUIState.ShowChangeJsonPathDialog
        }
    }

    fun onChangeICalPath() {
        if (_mainScreenUIState.value is MainScreenUIState.Ready) {
            _mainScreenUIState.value = MainScreenUIState.ShowChangeICalPathDialog
        }
    }

    fun updateJsonPath(jFileChooserResult: JFileChooserResult) {
        when (jFileChooserResult) {
            is JFileChooserResult.AbsolutePath -> {
                _jsonPath.value = stripBasePath(jFileChooserResult.absolutePath)
                _mainScreenUIState.value = MainScreenUIState.Ready
            }
            is JFileChooserResult.Cancelled -> _mainScreenUIState.value = MainScreenUIState.Ready
            else -> _mainScreenUIState.value =
                MainScreenUIState.Error(errMsg = resourceBundle.getString("error.updating.json.path"))
        }
    }

    fun updateICalPath(jFileChooserResult: JFileChooserResult) {
        when (jFileChooserResult) {
            is JFileChooserResult.AbsolutePath -> {
                _iCalPath.value = stripBasePath(jFileChooserResult.absolutePath)
                _mainScreenUIState.value = MainScreenUIState.Ready
            }
            is JFileChooserResult.Cancelled -> _mainScreenUIState.value = MainScreenUIState.Ready
            else -> _mainScreenUIState.value =
                MainScreenUIState.Error(errMsg = resourceBundle.getString("error.updating.ical.path"))
        }
    }

    private fun stripBasePath(absolutePath: String): String {
        return if (absolutePath.startsWith(projectBasePath)) {
            absolutePath.removePrefix(projectBasePath)
        } else {
            absolutePath
        }
    }

    private fun appendStatus(status: String) {
        _statusMessage.value.plus("\n$status")
    }
}