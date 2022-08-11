/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.viewmodels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ryanwong.gmap2ics.app.models.JFileChooserResult
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.configs.Config
import uk.ryanwong.gmap2ics.data.repository.LocalFileRepository
import uk.ryanwong.gmap2ics.data.repository.TimelineRepository
import uk.ryanwong.gmap2ics.ui.MainScreenUIState
import uk.ryanwong.gmap2ics.ui.usecases.VEventFromActivitySegmentUseCase
import uk.ryanwong.gmap2ics.ui.usecases.VEventFromChildVisitUseCase
import uk.ryanwong.gmap2ics.ui.usecases.VEventFromPlaceVisitUseCase
import uk.ryanwong.gmap2ics.ui.utils.DefaultResourceBundle
import uk.ryanwong.gmap2ics.ui.utils.ResourceBundleWrapper
import java.nio.file.Paths

class MainScreenViewModel(
    private val configFile: Config,
    private val timelineRepository: TimelineRepository,
    private val localFileRepository: LocalFileRepository,
    private val exportActivitySegmentUseCase: VEventFromActivitySegmentUseCase,
    private val exportChildVisitUseCase: VEventFromChildVisitUseCase,
    private val exportPlaceVisitUseCase: VEventFromPlaceVisitUseCase,
    private val resourceBundle: ResourceBundleWrapper = DefaultResourceBundle(),
    private val projectBasePath: String = Paths.get("").toAbsolutePath().toString().plus("/")
) {
    private var _mainScreenUIState: MutableStateFlow<MainScreenUIState> = MutableStateFlow(MainScreenUIState.Ready)
    val mainScreenUIState: StateFlow<MainScreenUIState> = _mainScreenUIState

    private var _statusMessage = MutableStateFlow<List<String>>(emptyList())
    val statusMessage: StateFlow<List<String>> = _statusMessage

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

    private var _verboseLogs = MutableStateFlow(false)
    val verboseLogs: StateFlow<Boolean> = _verboseLogs

    init {
        // Default values, overridable from UI
        // TODO: might provide as profiles
        with(configFile) {
            _iCalPath.value = icalPath
            _jsonPath.value = jsonPath
            _exportPlaceVisit.value = exportPlaceVisit
            _exportActivitySegment.value = exportActivitySegment
            _enablePlacesApiLookup.value = enablePlacesApiLookup
            _verboseLogs.value = verboseLogs
            appendStatus(status = "Config file loaded".plus(configFile.javaClass.packageName))
        }
    }

    fun startConversion() {
        _mainScreenUIState.value = MainScreenUIState.Processing

        CoroutineScope(Dispatchers.Default).launch {
            val fileList = localFileRepository.getFileList(
                absolutePath = _jsonPath.value,
                extension = "json"
            )

            if (fileList.isFailure) {
                _mainScreenUIState.value =
                    MainScreenUIState.Error(
                        errMsg = processResultFailure(
                            userFriendlyMessage = "Error getting json file list",
                            throwable = fileList.exceptionOrNull()
                        )
                    )
                return@launch
            } else {
                appendStatus(status = "${fileList.getOrNull()?.size ?: 0} files to be processed")
            }

            // Exporting multiple events in one single ics file
            fileList.getOrNull()?.forEach { filename ->
                appendStatus(status = "\uD83D\uDDC2 Processing $filename")
                val eventList: List<VEvent> = getEventList(filePath = filename)
                appendStatus(status = "💾 Exporting events in iCal format to $filename")
                localFileRepository.exportICal(
                    vEvents = eventList,
                    filename = getOutputFilename(originalFilename = filename)
                )
            }

            appendStatus(status = "conversion completed.")
            _mainScreenUIState.value = MainScreenUIState.Ready
        }
    }

    private fun getOutputFilename(originalFilename: String): String {
        val outputFilenameSuffix = when {
            _exportPlaceVisit.value && _exportActivitySegment.value -> "_all"
            _exportPlaceVisit.value -> "_places"
            else -> "_activities"
        }

        return originalFilename.replace(oldValue = _jsonPath.value, newValue = _iCalPath.value)
            // casually reuse the filename
            .replace(oldValue = ".json", newValue = "$outputFilenameSuffix.ics")
    }

    private suspend fun getEventList(filePath: String): List<VEvent> {
        val eventList = mutableListOf<VEvent>()
        val timeline = timelineRepository.parseTimeLine(filePath = filePath)

        timeline.getOrNull()?.timelineObjects?.let { timelineDataObjects ->
            for (timelineDataObject in timelineDataObjects) {
                // Should be either activity or place visited, but no harm to also support cases with both
                if (_exportActivitySegment.value) {
                    timelineDataObject.activitySegment?.let { activitySegment ->
                        val vEventResult = exportActivitySegmentUseCase(
                            activitySegment = activitySegment,
                            ignoredActivityType = configFile.ignoredActivityType
                        )

                        vEventResult.getOrNull()?.let { (vEvent, logEntry) ->
                            eventList.add(vEvent)
                            logEntry?.let { appendStatus(status = it) }
                            printLogForVerboseMode(status = vEvent.toString())
                        }
                        vEventResult.exceptionOrNull()?.message?.let { appendStatusForVerboseMode(status = it) }
                    }
                }

                if (_exportPlaceVisit.value) {
                    timelineDataObject.placeVisit?.let { placeVisit ->
                        // If parent visit is to be ignored, child has no meaning to stay
                        if (!configFile.ignoredVisitedPlaceIds.contains(placeVisit.location.placeId)) {
                            exportPlaceVisitUseCase(
                                placeVisit = placeVisit,
                                enablePlacesApiLookup = _enablePlacesApiLookup.value
                            ).let { vEvent ->
                                eventList.add(vEvent)
                                printLogForVerboseMode(status = vEvent.toString())
                            }

                            // If we have child-visits, we export them as individual events
                            // ChildVisit might have unconfirmed location which does not have a duration
                            placeVisit.childVisits?.forEach { childVisit ->
                                if (!configFile.ignoredVisitedPlaceIds.contains(childVisit.location.placeId)) {
                                    exportChildVisitUseCase(
                                        childVisit = childVisit,
                                        enablePlacesApiLookup = _enablePlacesApiLookup.value
                                    )?.let { vEvent ->
                                        eventList.add(vEvent)
                                        printLogForVerboseMode(status = vEvent.toString())
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        appendStatus("✅ Processed ${timeline.getOrNull()?.timelineObjects?.size ?: 0} timeline entries.")
        return eventList
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

    fun setVerboseLogs(enabled: Boolean) {
        _verboseLogs.value = enabled
    }

    fun onChangeJsonPath() {
        if (_mainScreenUIState.value is MainScreenUIState.Ready) {
            _mainScreenUIState.value = MainScreenUIState.ChangeJsonPath
        }
    }

    fun onChangeICalPath() {
        if (_mainScreenUIState.value is MainScreenUIState.Ready) {
            _mainScreenUIState.value = MainScreenUIState.ChangeICalPath
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

    fun notifyErrorMessageDisplayed() {
        _mainScreenUIState.value = MainScreenUIState.Ready
    }

    private fun stripBasePath(absolutePath: String): String {
        return if (absolutePath.startsWith(projectBasePath)) {
            absolutePath.removePrefix(projectBasePath)
        } else {
            absolutePath
        }
    }

    private fun appendStatusForVerboseMode(status: String) {
        if (_verboseLogs.value) {
            appendStatus(status)
        }
    }

    // Designed for object printout - output to console instead of UI
    private fun printLogForVerboseMode(status: String) {
        if (_verboseLogs.value) {
            println(status)
        }
    }

    private fun appendStatus(status: String) {
        _statusMessage.value = _statusMessage.value + status
    }

    private fun processResultFailure(throwable: Throwable?): String {
        throwable?.printStackTrace()
        return throwable?.localizedMessage ?: "unknown error"
    }

    private fun processResultFailure(userFriendlyMessage: String, throwable: Throwable?): String {
        throwable?.printStackTrace()
        return "☠️ $userFriendlyMessage: ".plus(throwable?.localizedMessage ?: "unknown error")
    }
}