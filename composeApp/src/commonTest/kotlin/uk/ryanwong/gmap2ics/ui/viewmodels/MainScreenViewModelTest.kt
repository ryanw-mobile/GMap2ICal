/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.ui.viewmodels

import dev.icerock.moko.mvvm.test.TestViewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import uk.ryanwong.gmap2ics.app.configs.SampleConfig
import uk.ryanwong.gmap2ics.data.repositories.fakes.FakeLocalFileRepository
import uk.ryanwong.gmap2ics.data.repositories.fakes.FakeTimelineRepository
import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.UILogEntry
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.LatLng
import uk.ryanwong.gmap2ics.ui.screens.MainScreenUIState
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModelTestData.timeLineWithActivityVisitAndChildVisit
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModelTestData.timeLineWithSingleActivity
import uk.ryanwong.gmap2ics.usecases.fakes.FakeGetActivitySegmentVEventUseCase
import uk.ryanwong.gmap2ics.usecases.fakes.FakeGetOutputFilenameUseCase
import uk.ryanwong.gmap2ics.usecases.fakes.FakeGetPlaceVisitVEventUseCase
import uk.ryanwong.gmap2ics.usecases.fakes.FakeVEventFromChildVisitUseCase
import uk.ryanwong.gmap2ics.usecases.fakes.FakeVEventFromPlaceVisitUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
internal class MainScreenViewModelTest {

    private lateinit var mainScreenViewModel: MainScreenViewModel
    private lateinit var fakeTimelineRepository: FakeTimelineRepository
    private lateinit var fakeLocalFileRepository: FakeLocalFileRepository
    private lateinit var fakeVEventFromPlaceVisitUseCase: FakeVEventFromPlaceVisitUseCase
    private lateinit var fakeVEventFromChildVisitUseCase: FakeVEventFromChildVisitUseCase
    private lateinit var fakeGetActivitySegmentVEventUseCase: FakeGetActivitySegmentVEventUseCase
    private lateinit var fakeGetOutputFilenameUseCase: FakeGetOutputFilenameUseCase
    private lateinit var fakeGetPlaceVisitVEventUseCase: FakeGetPlaceVisitVEventUseCase

    private val fakeProjectBasePath = "/default-base-path/default-sub-folder/"

    private val sampleDefaultVEvent = VEvent(
        uid = "2011-11-11T11:22:22.222Z",
        placeId = "location-id-to-be-kept",
        dtStamp = "2011-11-11T11:22:22.222Z",
        organizer = null,
        dtStart = RawTimestamp(timestamp = "2011-11-11T20:11:11.111Z", timezoneId = "Asia/Tokyo"),
        dtEnd = RawTimestamp(timestamp = "2011-11-11T20:22:22.222Z", timezoneId = "Asia/Tokyo"),
        summary = "📍 some-summary",
        location = "",
        geo = LatLng(latitude = 26.33833, longitude = 127.8),
        description = "Place ID:\\nlocation-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:location-id-to-be-kept",
        url = "https://www.google.com/maps/place/?q=place_id:location-id-to-be-kept",
        lastModified = "2011-11-11T11:22:22.222Z",
    )

    @BeforeTest
    fun setup() {
        TestViewModelScope.setupViewModelScope(CoroutineScope(Dispatchers.Unconfined))

        fakeTimelineRepository = FakeTimelineRepository()
        fakeLocalFileRepository = FakeLocalFileRepository()
        fakeVEventFromPlaceVisitUseCase = FakeVEventFromPlaceVisitUseCase()
        fakeVEventFromChildVisitUseCase = FakeVEventFromChildVisitUseCase()
        fakeGetActivitySegmentVEventUseCase = FakeGetActivitySegmentVEventUseCase()
        fakeGetOutputFilenameUseCase = FakeGetOutputFilenameUseCase()
        fakeGetPlaceVisitVEventUseCase = FakeGetPlaceVisitVEventUseCase()

        mainScreenViewModel =
            MainScreenViewModel(
                configFile = SampleConfig(),
                timelineRepository = fakeTimelineRepository,
                localFileRepository = fakeLocalFileRepository,
                getActivitySegmentVEventUseCase = fakeGetActivitySegmentVEventUseCase,
                getOutputFilenameUseCase = fakeGetOutputFilenameUseCase,
                getPlaceVisitVEventUseCase = fakeGetPlaceVisitVEventUseCase,
                projectBasePath = fakeProjectBasePath,
                dispatcher = UnconfinedTestDispatcher(),
            )
    }

    @AfterTest
    fun cleanup() {
        TestViewModelScope.resetViewModelScope()
    }

    // 🗂️ setExportPlaceVisit
    @Test
    fun `setExportPlaceVisit should update exportPlaceVisit correctly when mainScreenUIState is Ready`() = runTest {
        val initialState = mainScreenViewModel.exportPlaceVisit.first()
        mainScreenViewModel.setExportPlaceVisit(enabled = !initialState)
        assertEquals(!initialState, mainScreenViewModel.exportPlaceVisit.first())
    }

    @Test
    fun `setExportPlaceVisit should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeICalPathDialog`() = runTest {
        mainScreenViewModel.onChangeICalPath()
        val initialState = mainScreenViewModel.exportPlaceVisit.first()
        mainScreenViewModel.setExportPlaceVisit(enabled = !initialState)
        assertEquals(initialState, mainScreenViewModel.exportPlaceVisit.first())
    }

    @Test
    fun `setExportPlaceVisit should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeJsonPathDialog`() = runTest {
        mainScreenViewModel.onChangeJsonPath()
        val initialState = mainScreenViewModel.exportPlaceVisit.first()
        mainScreenViewModel.setExportPlaceVisit(enabled = !initialState)
        assertEquals(initialState, mainScreenViewModel.exportPlaceVisit.first())
    }

    @Test
    fun `setExportPlaceVisit should keep exportPlaceVisit unchanged when mainScreenUIState is Error`() = runTest {
        mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))
        val initialState = mainScreenViewModel.exportPlaceVisit.first()
        mainScreenViewModel.setExportPlaceVisit(enabled = !initialState)
        assertEquals(initialState, mainScreenViewModel.exportPlaceVisit.first())
    }

    // 🗂️ setExportActivitySegment
    @Test
    fun `setExportActivitySegment should update exportActivitySegment correctly when mainScreenUIState is Ready`() = runTest {
        val initialState = mainScreenViewModel.exportActivitySegment.first()
        mainScreenViewModel.setExportActivitySegment(enabled = !initialState)
        assertEquals(!initialState, mainScreenViewModel.exportActivitySegment.first())
    }

    @Test
    fun `setExportActivitySegment should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeICalPathDialog`() = runTest {
        mainScreenViewModel.onChangeICalPath()
        val initialState = mainScreenViewModel.exportActivitySegment.first()
        mainScreenViewModel.setExportActivitySegment(enabled = !initialState)
        assertEquals(initialState, mainScreenViewModel.exportActivitySegment.first())
    }

    @Test
    fun `setExportActivitySegment should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeJsonPathDialog`() = runTest {
        mainScreenViewModel.onChangeJsonPath()
        val initialState = mainScreenViewModel.exportActivitySegment.first()
        mainScreenViewModel.setExportActivitySegment(enabled = !initialState)
        assertEquals(initialState, mainScreenViewModel.exportActivitySegment.first())
    }

    @Test
    fun `setExportActivitySegment should keep exportPlaceVisit unchanged when mainScreenUIState is Error`() = runTest {
        mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))
        val initialState = mainScreenViewModel.exportActivitySegment.first()
        mainScreenViewModel.setExportActivitySegment(enabled = !initialState)
        assertEquals(initialState, mainScreenViewModel.exportActivitySegment.first())
    }

    // 🗂️ setEnablePlacesApiLookup
    @Test
    fun `setEnablePlacesApiLookup should update enablePlacesApiLookup correctly when mainScreenUIState is Ready`() = runTest {
        val initialState = mainScreenViewModel.enablePlacesApiLookup.first()
        mainScreenViewModel.setEnablePlacesApiLookup(enabled = !initialState)
        assertEquals(!initialState, mainScreenViewModel.enablePlacesApiLookup.first())
    }

    @Test
    fun `setEnablePlacesApiLookup should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeICalPathDialog`() = runTest {
        mainScreenViewModel.onChangeICalPath()
        val initialState = mainScreenViewModel.enablePlacesApiLookup.first()
        mainScreenViewModel.setEnablePlacesApiLookup(enabled = !initialState)
        assertEquals(initialState, mainScreenViewModel.enablePlacesApiLookup.first())
    }

    @Test
    fun `setEnablePlacesApiLookup should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeJsonPathDialog`() = runTest {
        mainScreenViewModel.onChangeJsonPath()
        val initialState = mainScreenViewModel.enablePlacesApiLookup.first()
        mainScreenViewModel.setEnablePlacesApiLookup(enabled = !initialState)
        assertEquals(initialState, mainScreenViewModel.enablePlacesApiLookup.first())
    }

    @Test
    fun `setEnablePlacesApiLookup should keep exportPlaceVisit unchanged when mainScreenUIState is Error`() = runTest {
        mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))
        val initialState = mainScreenViewModel.enablePlacesApiLookup.first()
        mainScreenViewModel.setEnablePlacesApiLookup(enabled = !initialState)
        assertEquals(initialState, mainScreenViewModel.enablePlacesApiLookup.first())
    }

    // 🗂️ setVerboseLogs
    @Test
    fun `setVerboseLogs should update verboseLogs correctly when mainScreenUIState is Ready`() = runTest {
        val initialState = mainScreenViewModel.verboseLogs.first()
        mainScreenViewModel.setVerboseLogs(enabled = !initialState)
        assertEquals(!initialState, mainScreenViewModel.verboseLogs.first())
    }

    @Test
    fun `setVerboseLogs should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeICalPathDialog`() = runTest {
        mainScreenViewModel.onChangeICalPath()
        val initialState = mainScreenViewModel.verboseLogs.first()
        mainScreenViewModel.setVerboseLogs(enabled = !initialState)
        assertEquals(initialState, mainScreenViewModel.verboseLogs.first())
    }

    @Test
    fun `setVerboseLogs should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeJsonPathDialog`() = runTest {
        mainScreenViewModel.onChangeJsonPath()
        val initialState = mainScreenViewModel.verboseLogs.first()
        mainScreenViewModel.setVerboseLogs(enabled = !initialState)
        assertEquals(initialState, mainScreenViewModel.verboseLogs.first())
    }

    @Test
    fun `setVerboseLogs should keep exportPlaceVisit unchanged when mainScreenUIState is Error`() = runTest {
        mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))
        val initialState = mainScreenViewModel.verboseLogs.first()
        mainScreenViewModel.setVerboseLogs(enabled = !initialState)
        assertEquals(initialState, mainScreenViewModel.verboseLogs.first())
    }

    // 🗂️ onChangeJsonPath
    @Test
    fun `onChangeJsonPath when mainScreenUIState is Ready should set mainScreenUIState to ShowChangeJsonPathDialog`() = runTest {
        assertEquals(MainScreenUIState.Ready, mainScreenViewModel.mainScreenUIState.first())
        mainScreenViewModel.onChangeJsonPath()
        assertEquals(MainScreenUIState.ChangeJsonPath, mainScreenViewModel.mainScreenUIState.first())
    }

    @Test
    fun `onChangeJsonPath when mainScreenUIState is ShowChangeICalPathDialog should keep mainScreenUIState unchanged`() = runTest {
        mainScreenViewModel.onChangeICalPath()
        mainScreenViewModel.onChangeJsonPath()
        assertEquals(MainScreenUIState.ChangeICalPath, mainScreenViewModel.mainScreenUIState.first())
    }

    @Test
    fun `onChangeJsonPath when mainScreenUIState is Error should keep mainScreenUIState unchanged`() = runTest {
        val errorMessage = "Error updating JSON path"
        mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))

        mainScreenViewModel.onChangeJsonPath()

        val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
        assertIs<MainScreenUIState.Error>(mainScreenUIState)
        assertEquals(errorMessage, mainScreenUIState.errMsg)
    }

    // 🗂️ onChangeICalPath
    @Test
    fun `onChangeICalPath when mainScreenUIState is Ready should set mainScreenUIState to ShowChangeJsonPathDialog`() = runTest {
        assertEquals(MainScreenUIState.Ready, mainScreenViewModel.mainScreenUIState.first())
        mainScreenViewModel.onChangeICalPath()
        assertEquals(MainScreenUIState.ChangeICalPath, mainScreenViewModel.mainScreenUIState.first())
    }

    @Test
    fun `onChangeICalPath when mainScreenUIState is ShowChangeICalPathDialog should keep mainScreenUIState unchanged`() = runTest {
        mainScreenViewModel.onChangeJsonPath()
        mainScreenViewModel.onChangeICalPath()
        assertEquals(MainScreenUIState.ChangeJsonPath, mainScreenViewModel.mainScreenUIState.first())
    }

    @Test
    fun `onChangeICalPath when mainScreenUIState is Error should keep mainScreenUIState unchanged`() = runTest {
        val errorMessage = "Error updating JSON path"
        mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))

        mainScreenViewModel.onChangeICalPath()

        val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
        assertIs<MainScreenUIState.Error>(mainScreenUIState)
        assertEquals(errorMessage, mainScreenUIState.errMsg)
    }

    // 🗂️ updateJsonPath
    @Test
    fun `updateJsonPath when JFileChooserResult is AbsolutePath should correctly trim the path`() = runTest {
        mainScreenViewModel.onChangeJsonPath()

        val jFileChooserResult = JFileChooserResult.AbsolutePath(
            absolutePath = fakeProjectBasePath + "sample-folder1/sample-folder2",
        )
        mainScreenViewModel.updateJsonPath(jFileChooserResult = jFileChooserResult)
        assertEquals("sample-folder1/sample-folder2", mainScreenViewModel.jsonPath.first())
    }

    @Test
    fun `updateJsonPath when JFileChooserResult is AbsolutePath should set MainScreenUIState = Ready`() = runTest {
        mainScreenViewModel.onChangeJsonPath()

        val jFileChooserResult = JFileChooserResult.AbsolutePath(
            absolutePath = fakeProjectBasePath + "sample-folder1/sample-folder2",
        )
        mainScreenViewModel.updateJsonPath(jFileChooserResult = jFileChooserResult)
        assertEquals(MainScreenUIState.Ready, mainScreenViewModel.mainScreenUIState.first())
    }

    @Test
    fun `updateJsonPath when JFileChooserResult is Cancelled should set MainScreenUIState = Ready`() = runTest {
        mainScreenViewModel.onChangeJsonPath()
        mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Cancelled)
        assertEquals(MainScreenUIState.Ready, mainScreenViewModel.mainScreenUIState.first())
    }

    @Test
    fun `updateJsonPath when JFileChooserResult is Error should set MainScreenUIState = Error with correct error message`() = runTest {
        val errorMessage = "Error updating JSON path"
        mainScreenViewModel.onChangeJsonPath()

        mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))

        val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
        assertIs<MainScreenUIState.Error>(mainScreenUIState)
        assertEquals(errorMessage, mainScreenUIState.errMsg)
    }

    // 🗂️ updateICalPath
    @Test
    fun `when JFileChooserResult is AbsolutePath should correctly trim the path if it contains the base path`() = runTest {
        mainScreenViewModel.onChangeICalPath()

        val jFileChooserResult = JFileChooserResult.AbsolutePath(
            absolutePath = fakeProjectBasePath + "sample-folder1/sample-folder2",
        )
        mainScreenViewModel.updateICalPath(jFileChooserResult = jFileChooserResult)
        assertEquals("sample-folder1/sample-folder2", mainScreenViewModel.iCalPath.first())
    }

    @Test
    fun `when JFileChooserResult is AbsolutePath should keep the correct path if AbsolutePath does not contains the base path`() = runTest {
        mainScreenViewModel.onChangeICalPath()

        val jFileChooserResult = JFileChooserResult.AbsolutePath(
            absolutePath = "/sample-folder3/sample-folder4",
        )
        mainScreenViewModel.updateICalPath(jFileChooserResult = jFileChooserResult)
        assertEquals("/sample-folder3/sample-folder4", mainScreenViewModel.iCalPath.first())
    }

    @Test
    fun `when JFileChooserResult is AbsolutePath should set MainScreenUIState = Ready`() = runTest {
        mainScreenViewModel.onChangeICalPath()

        val jFileChooserResult = JFileChooserResult.AbsolutePath(
            absolutePath = fakeProjectBasePath + "sample-folder1/sample-folder2",
        )
        mainScreenViewModel.updateICalPath(jFileChooserResult = jFileChooserResult)
        assertEquals(MainScreenUIState.Ready, mainScreenViewModel.mainScreenUIState.first())
    }

    @Test
    fun `when JFileChooserResult is Cancelled should set MainScreenUIState = Ready`() = runTest {
        mainScreenViewModel.onChangeICalPath()
        mainScreenViewModel.updateICalPath(jFileChooserResult = JFileChooserResult.Cancelled)
        assertEquals(MainScreenUIState.Ready, mainScreenViewModel.mainScreenUIState.first())
    }

    @Test
    fun `when JFileChooserResult is Error should set MainScreenUIState = Error with correct error message`() = runTest {
        val errorMessage = "Error updating iCal path"
        mainScreenViewModel.onChangeICalPath()

        mainScreenViewModel.updateICalPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))

        val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
        assertIs<MainScreenUIState.Error>(mainScreenUIState)
        assertEquals(errorMessage, mainScreenUIState.errMsg)
    }

    // 🗂️ notifyErrorMessageDisplayed
    @Test
    fun `should set MainScreenUIState = Ready`() = runTest {
        mainScreenViewModel.onChangeICalPath() // Randomly alter the UI state
        mainScreenViewModel.notifyErrorMessageDisplayed()

        val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
        assertEquals(MainScreenUIState.Ready, mainScreenUIState)
    }

    // 🗂️ startExport
    @Test
    fun `should set MainScreenUIState = Ready after running`() = runTest {
        fakeLocalFileRepository.getFileListResponse = Result.success(listOf("/some-path/some-file-1.json"))
        fakeGetOutputFilenameUseCase.useCaseResponse = "/some-path/some-file-1.ics"
        fakeTimelineRepository.getTimeLineResponse = Result.success(timeLineWithActivityVisitAndChildVisit)
        fakeVEventFromPlaceVisitUseCase.useCaseResponse = sampleDefaultVEvent
        fakeGetActivitySegmentVEventUseCase.useCaseResponse = sampleDefaultVEvent
        fakeVEventFromChildVisitUseCase.useCaseResponse = sampleDefaultVEvent
        fakeGetPlaceVisitVEventUseCase.useCaseResponse = listOf(sampleDefaultVEvent)

        mainScreenViewModel.startExport()

        val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
        assertEquals(MainScreenUIState.Ready, mainScreenUIState)
    }

    @Test
    fun `should set MainScreenUIState = Error if localFileRepository,getFileList returns error`() = runTest {
        fakeGetPlaceVisitVEventUseCase.useCaseResponse = listOf(sampleDefaultVEvent)
        fakeLocalFileRepository.getFileListResponse =
            Result.failure(exception = Exception("some-exception-message"))

        mainScreenViewModel.startExport()

        val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
        with(mainScreenUIState as MainScreenUIState.Error) {
            assertEquals("☠️ Error getting json file list: some-exception-message", errMsg)
        }
    }

    // ➡️ getActivitySegmentVEventUseCase
    @Test
    fun `should append Ignore Log if getActivitySegmentVEventUseCase returns null`() = runTest {
        fakeLocalFileRepository.getFileListResponse = Result.success(listOf("/some-path/some-file-1.json"))
        fakeGetOutputFilenameUseCase.useCaseResponse = "/some-path/some-file-1.ics"
        fakeTimelineRepository.getTimeLineResponse = Result.success(timeLineWithSingleActivity)
        fakeVEventFromPlaceVisitUseCase.useCaseResponse = sampleDefaultVEvent
        fakeGetActivitySegmentVEventUseCase.useCaseResponse = null
        fakeVEventFromChildVisitUseCase.useCaseResponse = sampleDefaultVEvent
        fakeGetPlaceVisitVEventUseCase.useCaseResponse = listOf(sampleDefaultVEvent)
        val expectedLogs = listOf(
            UILogEntry(emoji = "🚫", message = "08/07/2019 12:00:33: Activity FLYING"),
        )

        mainScreenViewModel.startExport()

        val ignoredLogs = mainScreenViewModel.ignoredLogs.first()
        assertEquals(expectedLogs, ignoredLogs)
    }

    @Test
    fun `should append Exported Log if getActivitySegmentVEventUseCase returns VEvent`() = runTest {
        fakeLocalFileRepository.getFileListResponse = Result.success(listOf("/some-path/some-file-1.json"))
        fakeGetOutputFilenameUseCase.useCaseResponse = "/some-path/some-file-1.ics"
        fakeTimelineRepository.getTimeLineResponse = Result.success(timeLineWithSingleActivity)
        fakeVEventFromPlaceVisitUseCase.useCaseResponse = null
        fakeGetActivitySegmentVEventUseCase.useCaseResponse = sampleDefaultVEvent
        fakeVEventFromChildVisitUseCase.useCaseResponse = null
        fakeGetPlaceVisitVEventUseCase.useCaseResponse = null
        val expectedLogs = listOf(
            UILogEntry(emoji = "🗓", message = "12/11/2011 05:11:11: 📍 some-summary"),
        )

        mainScreenViewModel.startExport()

        val exportedLogs = mainScreenViewModel.exportedLogs.first()
        assertEquals(expectedLogs, exportedLogs)
    }

    // 🗂️ observeGetPlaceVisitVEventUseCaseFlows
    @Test
    fun `should append Ignore Log if getPlaceVisitVEventUseCase,ignoredEvents emits something`() = TestScope(StandardTestDispatcher()).runTest {
        val uiLogEntry = UILogEntry(emoji = "🚫", message = "08/07/2019 12:00:33: Activity FLYING")
        fakeGetPlaceVisitVEventUseCase.emitIgnoredEvent(uiLogEntry = uiLogEntry)

        val ignoredLogs = mainScreenViewModel.ignoredLogs.first()
        assertEquals(listOf(uiLogEntry), ignoredLogs)
    }

    @Test
    fun `should append Exported Log if getPlaceVisitVEventUseCase,exportedEvents emits something`() = TestScope(StandardTestDispatcher()).runTest {
        val uiLogEntry = UILogEntry(emoji = "🗓", message = "12/11/2011 05:11:11: 📍 some-summary")
        fakeGetPlaceVisitVEventUseCase.emitExportedEvent(uiLogEntry = uiLogEntry)

        val exportedLogs = mainScreenViewModel.exportedLogs.first()
        assertEquals(listOf(uiLogEntry), exportedLogs)
    }
}
