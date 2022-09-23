/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.viewmodels

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ryanwong.gmap2ics.app.configs.Config
import uk.ryanwong.gmap2ics.app.models.JFileChooserResult
import uk.ryanwong.gmap2ics.app.models.UILogEntry
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.Timeline
import uk.ryanwong.gmap2ics.app.usecases.GetActivitySegmentVEventUseCase
import uk.ryanwong.gmap2ics.app.usecases.GetOutputFilenameUseCase
import uk.ryanwong.gmap2ics.app.usecases.GetPlaceVisitVEventUseCase
import uk.ryanwong.gmap2ics.data.repository.LocalFileRepository
import uk.ryanwong.gmap2ics.data.repository.TimelineRepository
import uk.ryanwong.gmap2ics.ui.screens.MainScreenUIState
import java.nio.file.Paths
import java.util.ResourceBundle

class MainScreenViewModel(
    private val configFile: Config,
    private val timelineRepository: TimelineRepository,
    private val localFileRepository: LocalFileRepository,
    private val getOutputFilenameUseCase: GetOutputFilenameUseCase,
    private val getActivitySegmentVEventUseCase: GetActivitySegmentVEventUseCase,
    private val getPlaceVisitVEventUseCase: GetPlaceVisitVEventUseCase,
    private val resourceBundle: ResourceBundle,
    private val projectBasePath: String = Paths.get("").toAbsolutePath().toString().plus("/"),
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    private var _mainScreenUIState: MutableStateFlow<MainScreenUIState> = MutableStateFlow(MainScreenUIState.Ready)
    val mainScreenUIState: StateFlow<MainScreenUIState> = _mainScreenUIState

    private var _exportedLogs = MutableStateFlow<List<UILogEntry>>(emptyList())
    val exportedLogs: StateFlow<List<UILogEntry>> = _exportedLogs

    private var _ignoredLogs = MutableStateFlow<List<UILogEntry>>(emptyList())
    val ignoredLogs: StateFlow<List<UILogEntry>> = _ignoredLogs

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

    private var _verboseLogs = MutableStateFlow(false)
    val verboseLogs: StateFlow<Boolean> = _verboseLogs

    private var coroutineJob: Job? = null
    private val viewModelScope = CoroutineScope(Job() + dispatcher)

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
            updateStatus(message = "Applied configurations: ".plus(configFile.javaClass.toString().split(".").last()))
        }

        observeGetPlaceVisitVEventUseCaseFlows()
    }

    fun cancelExport() {
        if (_mainScreenUIState.value is MainScreenUIState.Processing) {
            coroutineJob?.run {
                cancel()
                updateStatus(message = "Operation cancelled")
                _mainScreenUIState.value = MainScreenUIState.Ready
            }
        }
    }

    fun startExport() {
        _mainScreenUIState.value = MainScreenUIState.Processing(progress = 0f)
        _exportedLogs.value = emptyList()
        _ignoredLogs.value = emptyList()

        coroutineJob = CoroutineScope(dispatcher).launch {
            val fileList = localFileRepository.getFileList(
                relativePath = _jsonPath.value,
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
                updateStatus(message = "${fileList.getOrNull()?.size ?: 0} files to be processed")
            }

            // Exporting multiple events in one single ics file
            fileList.getOrNull()?.forEach { filePath ->
                updateStatus(message = "Processing ${stripBasePath(filePath)}")
                val timeline = timelineRepository.getTimeLine(filePath = filePath)
                val eventList: List<VEvent> = getEventList(timeline = timeline)
                val outputFileName = getOutputFilenameUseCase(
                    originalFilename = filePath,
                    iCalPath = _iCalPath.value,
                    jsonPath = _jsonPath.value,
                    exportActivitySegment = _exportActivitySegment.value,
                    exportPlaceVisit = _exportPlaceVisit.value
                )

                localFileRepository.exportICal(
                    vEvents = eventList,
                    filename = outputFileName
                )
                updateStatus("iCal events saved to ${stripBasePath(outputFileName)}")
            }

            updateStatus(message = "Done. Processed ${fileList.getOrNull()?.size ?: 0} files.")
            _mainScreenUIState.value = MainScreenUIState.Ready
        }
    }

    private suspend fun getEventList(timeline: Result<Timeline>): List<VEvent> {
        val eventList = mutableListOf<VEvent>()

        timeline.getOrNull()?.let {
            val itemCount = it.timelineEntries.size
            var itemProcessed = 0

            it.timelineEntries.forEach { timelineEntry ->
                itemProcessed += 1.also {
                    _mainScreenUIState.value =
                        MainScreenUIState.Processing(progress = itemProcessed / (itemCount * 1.0f))
                }

                // Should be either activity or place visited, but no harm to also support cases with both
                if (_exportActivitySegment.value) {
                    timelineEntry.activitySegment?.let { activitySegment ->
                        val vEvent = getActivitySegmentVEventUseCase(
                            activitySegment = activitySegment,
                            ignoredActivityType = configFile.ignoredActivityType,
                            enablePlacesApiLookup = _enablePlacesApiLookup.value
                        )

                        vEvent?.let { event ->
                            eventList.add(event)
                            appendExportedLog(
                                uiLogEntry = UILogEntry(
                                    emoji = "\uD83D\uDDD3",
                                    message = "${event.dtStart.toUITimestamp()}: ${event.summary}"
                                )
                            )
                        } ?: appendIgnoredLog(
                            uiLogEntry = UILogEntry(
                                emoji = "🚫",
                                message = "${activitySegment.durationStartTimestamp.toUITimestamp()}: Activity ${activitySegment.activityType}"
                            )
                        )
                    }
                }

                if (_exportPlaceVisit.value) {
                    val vEventList =
                        timelineEntry.placeVisit?.let { placeVisit ->
                            getPlaceVisitVEventUseCase(
                                placeVisit = placeVisit,
                                enablePlacesApiLookup = _enablePlacesApiLookup.value,
                                ignoredVisitedPlaceIds = configFile.ignoredVisitedPlaceIds,
                                verboseConsoleLog = _verboseLogs.value,
                            )
                        }
                    vEventList?.let { eventList.addAll(vEventList) }
                }
            }
        }

        timeline.exceptionOrNull()?.let { throwable ->
            processResultFailure(userFriendlyMessage = "☠️ Error processing timeline", throwable)
        }
        updateStatus(message = "Processed ${timeline.getOrNull()?.timelineEntries?.size ?: 0} timeline entries.")
        return eventList
    }

    fun setExportPlaceVisit(enabled: Boolean) {
        if (_mainScreenUIState.value is MainScreenUIState.Ready) {
            _exportPlaceVisit.value = enabled
        }
    }

    fun setExportActivitySegment(enabled: Boolean) {
        if (_mainScreenUIState.value is MainScreenUIState.Ready) {
            _exportActivitySegment.value = enabled
        }
    }

    fun setEnablePlacesApiLookup(enabled: Boolean) {
        if (_mainScreenUIState.value is MainScreenUIState.Ready) {
            _enablePlacesApiLookup.value = enabled
        }
    }

    fun setVerboseLogs(enabled: Boolean) {
        if (_mainScreenUIState.value is MainScreenUIState.Ready) {
            _verboseLogs.value = enabled
        }
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
            else ->
                _mainScreenUIState.value =
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
            else ->
                _mainScreenUIState.value =
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

    private fun appendExportedLog(uiLogEntry: UILogEntry) {
        _exportedLogs.value = _exportedLogs.value + uiLogEntry
    }

    private fun appendIgnoredLog(uiLogEntry: UILogEntry) {
        _ignoredLogs.value = _ignoredLogs.value + uiLogEntry
    }

    private fun updateStatus(message: String) {
        _statusMessage.value = message
    }

    private fun processResultFailure(userFriendlyMessage: String, throwable: Throwable?): String {
        throwable?.printStackTrace()
        return "☠️ $userFriendlyMessage: ".plus(throwable?.localizedMessage ?: "unknown error")
    }

    private fun observeGetPlaceVisitVEventUseCaseFlows() {
        viewModelScope.launch {
            getPlaceVisitVEventUseCase.exportedEvents.collect { uiLogEntry ->
                appendExportedLog(uiLogEntry = uiLogEntry)
            }
        }

        viewModelScope.launch {
            getPlaceVisitVEventUseCase.ignoredEvents.collect { uiLogEntry ->
                appendIgnoredLog(uiLogEntry = uiLogEntry)
            }
        }
    }
}
