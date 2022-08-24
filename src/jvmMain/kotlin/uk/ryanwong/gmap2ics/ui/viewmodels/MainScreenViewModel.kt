/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.viewmodels

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ryanwong.gmap2ics.app.configs.Config
import uk.ryanwong.gmap2ics.app.models.JFileChooserResult
import uk.ryanwong.gmap2ics.app.models.UILogEntry
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.PlaceVisit
import uk.ryanwong.gmap2ics.app.usecases.GetActivitySegmentVEventUseCase
import uk.ryanwong.gmap2ics.app.usecases.GetOutputFilenameUseCase
import uk.ryanwong.gmap2ics.app.usecases.VEventFromChildVisitUseCase
import uk.ryanwong.gmap2ics.app.usecases.VEventFromPlaceVisitUseCase
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
    private val vEventFromChildVisitUseCase: VEventFromChildVisitUseCase,
    private val vEventFromPlaceVisitUseCase: VEventFromPlaceVisitUseCase,
    private val resourceBundle: ResourceBundle,
    private val projectBasePath: String = Paths.get("").toAbsolutePath().toString().plus("/"),
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
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
    }

    fun startExport() {
        _mainScreenUIState.value = MainScreenUIState.Processing(progress = 0f)
        _exportedLogs.value = emptyList()
        _ignoredLogs.value = emptyList()

        CoroutineScope(dispatcher).launch {
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
                updateStatus(message = "${fileList.getOrNull()?.size ?: 0} files to be processed")
            }

            // Exporting multiple events in one single ics file
            fileList.getOrNull()?.forEach { filename ->
                updateStatus(message = "Processing ${stripBasePath(filename)}")
                val eventList: List<VEvent> = getEventList(filePath = filename)
                val outputFileName = getOutputFilenameUseCase(
                    originalFilename = filename,
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

    private suspend fun getEventList(filePath: String): List<VEvent> {
        val eventList = mutableListOf<VEvent>()
        val timeline = timelineRepository.getTimeLine(filePath = filePath)

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
                                emoji = "\uD83D\uDDD3",
                                message = "${event.dtStart.toUITimestamp()}: ${event.summary}"
                            )
                        } ?: appendIgnoredLog(
                            emoji = "🚫",
                            message = "${activitySegment.durationStartTimestamp.toUITimestamp()}: Activity ${activitySegment.activityType}"
                        )
                    }
                }

                if (_exportPlaceVisit.value) {
                    val vEventList = timelineEntry.placeVisit?.let { getPlaceVisitVEvent(it) }
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

    private suspend fun getPlaceVisitVEvent(placeVisit: PlaceVisit): List<VEvent> {
        val eventList: MutableList<VEvent> = mutableListOf()

        // If parent visit is to be ignored, child has no meaning to stay
        if (configFile.ignoredVisitedPlaceIds.contains(placeVisit.location.placeId)) {
            appendIgnoredLog(
                emoji = "🚫",
                message = "${placeVisit.durationStartTimestamp.toUITimestamp()}: Place ID ${placeVisit.location.placeId}"
            )
        } else {
            vEventFromPlaceVisitUseCase(
                placeVisit = placeVisit,
                enablePlacesApiLookup = _enablePlacesApiLookup.value
            ).let { vEvent ->
                eventList.add(vEvent)
                appendExportedLog(
                    emoji = "\uD83D\uDDD3",
                    message = "${vEvent.dtStart.toUITimestamp()}: ${vEvent.summary}"
                )
                printVerboseConsoleLog(message = vEvent.toString())
            }

            // If we have child-visits, we export them as individual events
            // ChildVisit might have unconfirmed location which does not have a duration
            placeVisit.childVisits.forEach { childVisit ->
                if (!configFile.ignoredVisitedPlaceIds.contains(childVisit.location.placeId)) {
                    vEventFromChildVisitUseCase(
                        childVisit = childVisit,
                        enablePlacesApiLookup = _enablePlacesApiLookup.value
                    )?.let { vEvent ->
                        eventList.add(vEvent)
                        appendExportedLog(
                            emoji = "\uD83D\uDDD3",
                            message = "${vEvent.dtStart.toUITimestamp()}: ${vEvent.summary}"
                        )
                        printVerboseConsoleLog(message = vEvent.toString())
                    }
                }
            }
        }
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

    private fun printVerboseConsoleLog(message: String) {
        if (_verboseLogs.value) {
            Napier.v(tag = "MainScreenViewModel", message = message)
        }
    }

    private fun appendExportedLog(emoji: String, message: String) {
        _exportedLogs.value = _exportedLogs.value + UILogEntry(emoji = emoji, message = message)
    }

    private fun appendIgnoredLog(emoji: String, message: String) {
        _ignoredLogs.value = _ignoredLogs.value + UILogEntry(emoji = emoji, message = message)
    }

    private fun updateStatus(message: String) {
        _statusMessage.value = message
    }

    private fun processResultFailure(userFriendlyMessage: String, throwable: Throwable?): String {
        throwable?.printStackTrace()
        return "☠️ $userFriendlyMessage: ".plus(throwable?.localizedMessage ?: "unknown error")
    }
}