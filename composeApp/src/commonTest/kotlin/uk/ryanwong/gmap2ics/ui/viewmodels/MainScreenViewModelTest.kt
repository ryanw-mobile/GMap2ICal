/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.ui.viewmodels

import dev.icerock.moko.mvvm.test.TestViewModelScope
import gmap2ical.composeapp.generated.resources.Res
import gmap2ical.composeapp.generated.resources.error_updating_ical_path
import gmap2ical.composeapp.generated.resources.error_updating_json_path
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString
import uk.ryanwong.gmap2ics.app.configs.MockConfig
import uk.ryanwong.gmap2ics.data.repositories.mocks.MockLocalFileRepository
import uk.ryanwong.gmap2ics.data.repositories.mocks.MockTimelineRepository
import uk.ryanwong.gmap2ics.domain.models.RawTimestamp
import uk.ryanwong.gmap2ics.domain.models.UILogEntry
import uk.ryanwong.gmap2ics.domain.models.VEvent
import uk.ryanwong.gmap2ics.domain.models.timeline.LatLng
import uk.ryanwong.gmap2ics.ui.screens.MainScreenUIState
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModelTestData.mockTimeLineWithActivityVisitAndChildVisit
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModelTestData.mockTimeLineWithSingleActivity
import uk.ryanwong.gmap2ics.usecases.mocks.MockGetActivitySegmentVEventUseCase
import uk.ryanwong.gmap2ics.usecases.mocks.MockGetOutputFilenameUseCase
import uk.ryanwong.gmap2ics.usecases.mocks.MockGetPlaceVisitVEventUseCase
import uk.ryanwong.gmap2ics.usecases.mocks.MockVEventFromChildVisitUseCase
import uk.ryanwong.gmap2ics.usecases.mocks.MockVEventFromPlaceVisitUseCase

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalResourceApi::class)
internal class MainScreenViewModelTest : FreeSpec() {

    private lateinit var mainScreenViewModel: MainScreenViewModel
    private lateinit var mockTimelineRepository: MockTimelineRepository
    private lateinit var mockLocalFileRepository: MockLocalFileRepository
    private lateinit var mockVEventFromPlaceVisitUseCase: MockVEventFromPlaceVisitUseCase
    private lateinit var mockVEventFromChildVisitUseCase: MockVEventFromChildVisitUseCase
    private lateinit var mockGetActivitySegmentVEventUseCase: MockGetActivitySegmentVEventUseCase
    private lateinit var mockGetOutputFilenameUseCase: MockGetOutputFilenameUseCase
    private lateinit var mockGetPlaceVisitVEventUseCase: MockGetPlaceVisitVEventUseCase

    private val mockProjectBasePath = "/default-base-path/default-sub-folder/"

    private val mockDefaultVEvent = VEvent(
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

    init {
        beforeTest {
            TestViewModelScope.setupViewModelScope(CoroutineScope(Dispatchers.Unconfined))

            mockTimelineRepository = MockTimelineRepository()
            mockLocalFileRepository = MockLocalFileRepository()
            mockVEventFromPlaceVisitUseCase = MockVEventFromPlaceVisitUseCase()
            mockVEventFromChildVisitUseCase = MockVEventFromChildVisitUseCase()
            mockGetActivitySegmentVEventUseCase = MockGetActivitySegmentVEventUseCase()
            mockGetOutputFilenameUseCase = MockGetOutputFilenameUseCase()
            mockGetPlaceVisitVEventUseCase = MockGetPlaceVisitVEventUseCase()

            mainScreenViewModel =
                MainScreenViewModel(
                    configFile = MockConfig(),
                    timelineRepository = mockTimelineRepository,
                    localFileRepository = mockLocalFileRepository,
                    getActivitySegmentVEventUseCase = mockGetActivitySegmentVEventUseCase,
                    getOutputFilenameUseCase = mockGetOutputFilenameUseCase,
                    getPlaceVisitVEventUseCase = mockGetPlaceVisitVEventUseCase,
                    projectBasePath = mockProjectBasePath,
                    dispatcher = UnconfinedTestDispatcher(),
                )
        }

        afterTest {
            TestViewModelScope.resetViewModelScope()
        }

        "setExportPlaceVisit" - {
            "should update exportPlaceVisit correctly when mainScreenUIState is Ready" {
                val initialState = mainScreenViewModel.exportPlaceVisit.first()
                mainScreenViewModel.setExportPlaceVisit(enabled = !initialState)
                mainScreenViewModel.exportPlaceVisit.first() shouldBe !initialState
            }

            "should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeICalPathDialog" {
                mainScreenViewModel.onChangeICalPath()
                val initialState = mainScreenViewModel.exportPlaceVisit.first()

                mainScreenViewModel.setExportPlaceVisit(enabled = !initialState)

                mainScreenViewModel.exportPlaceVisit.first() shouldBe initialState
            }

            "should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeJsonPathDialog" {
                mainScreenViewModel.onChangeJsonPath()
                val initialState = mainScreenViewModel.exportPlaceVisit.first()

                mainScreenViewModel.setExportPlaceVisit(enabled = !initialState)

                mainScreenViewModel.exportPlaceVisit.first() shouldBe initialState
            }

            "should keep exportPlaceVisit unchanged when mainScreenUIState is Error" {
                mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))
                val initialState = mainScreenViewModel.exportPlaceVisit.first()

                mainScreenViewModel.setExportPlaceVisit(enabled = !initialState)

                mainScreenViewModel.exportPlaceVisit.first() shouldBe initialState
            }
        }

        "setExportActivitySegment" - {
            "should update exportActivitySegment correctly when mainScreenUIState is Ready" {
                val initialState = mainScreenViewModel.exportActivitySegment.first()
                mainScreenViewModel.setExportActivitySegment(enabled = !initialState)
                mainScreenViewModel.exportActivitySegment.first() shouldBe !initialState
            }

            "should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeICalPathDialog" {
                mainScreenViewModel.onChangeICalPath()
                val initialState = mainScreenViewModel.exportActivitySegment.first()

                mainScreenViewModel.setExportActivitySegment(enabled = !initialState)

                mainScreenViewModel.exportActivitySegment.first() shouldBe initialState
            }

            "should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeJsonPathDialog" {
                mainScreenViewModel.onChangeJsonPath()
                val initialState = mainScreenViewModel.exportActivitySegment.first()

                mainScreenViewModel.setExportActivitySegment(enabled = !initialState)

                mainScreenViewModel.exportActivitySegment.first() shouldBe initialState
            }

            "should keep exportPlaceVisit unchanged when mainScreenUIState is Error" {
                mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))
                val initialState = mainScreenViewModel.exportActivitySegment.first()

                mainScreenViewModel.setExportActivitySegment(enabled = !initialState)

                mainScreenViewModel.exportActivitySegment.first() shouldBe initialState
            }
        }

        "setEnablePlacesApiLookup" - {
            "should update enablePlacesApiLookup correctly when mainScreenUIState is Ready" {
                val initialState = mainScreenViewModel.enablePlacesApiLookup.first()

                mainScreenViewModel.setEnablePlacesApiLookup(enabled = !initialState)

                mainScreenViewModel.enablePlacesApiLookup.first() shouldBe !initialState
            }

            "should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeICalPathDialog" {
                mainScreenViewModel.onChangeICalPath()
                val initialState = mainScreenViewModel.enablePlacesApiLookup.first()

                mainScreenViewModel.setEnablePlacesApiLookup(enabled = !initialState)

                mainScreenViewModel.enablePlacesApiLookup.first() shouldBe initialState
            }

            "should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeJsonPathDialog" {
                mainScreenViewModel.onChangeJsonPath()
                val initialState = mainScreenViewModel.enablePlacesApiLookup.first()

                mainScreenViewModel.setEnablePlacesApiLookup(enabled = !initialState)

                mainScreenViewModel.enablePlacesApiLookup.first() shouldBe initialState
            }

            "should keep exportPlaceVisit unchanged when mainScreenUIState is Error" {
                mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))
                val initialState = mainScreenViewModel.enablePlacesApiLookup.first()

                mainScreenViewModel.setEnablePlacesApiLookup(enabled = !initialState)

                mainScreenViewModel.enablePlacesApiLookup.first() shouldBe initialState
            }
        }

        "setVerboseLogs" - {
            "should update verboseLogs correctly when mainScreenUIState is Ready" {
                val initialState = mainScreenViewModel.verboseLogs.first()
                mainScreenViewModel.setVerboseLogs(enabled = !initialState)
                mainScreenViewModel.verboseLogs.first() shouldBe !initialState
            }

            "should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeICalPathDialog" {
                mainScreenViewModel.onChangeICalPath()
                val initialState = mainScreenViewModel.verboseLogs.first()

                mainScreenViewModel.setVerboseLogs(enabled = !initialState)

                mainScreenViewModel.verboseLogs.first() shouldBe initialState
            }

            "should keep exportPlaceVisit unchanged when mainScreenUIState is ShowChangeJsonPathDialog" {
                mainScreenViewModel.onChangeJsonPath()
                val initialState = mainScreenViewModel.verboseLogs.first()

                mainScreenViewModel.setVerboseLogs(enabled = !initialState)

                mainScreenViewModel.verboseLogs.first() shouldBe initialState
            }

            "should keep exportPlaceVisit unchanged when mainScreenUIState is Error" {
                mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))
                val initialState = mainScreenViewModel.verboseLogs.first()

                mainScreenViewModel.setVerboseLogs(enabled = !initialState)

                mainScreenViewModel.verboseLogs.first() shouldBe initialState
            }
        }

        "onChangeJsonPath" - {
            "When mainScreenUIState is Ready" - {
                "should set mainScreenUIState to ShowChangeJsonPathDialog" {
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.Ready
                    mainScreenViewModel.onChangeJsonPath()
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.ChangeJsonPath
                }
            }

            "When mainScreenUIState is ShowChangeICalPathDialog" - {
                "should keep mainScreenUIState unchanged" {
                    mainScreenViewModel.onChangeICalPath()
                    mainScreenViewModel.onChangeJsonPath()
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.ChangeICalPath
                }
            }

            "When mainScreenUIState is Error" - {
                "should keep mainScreenUIState unchanged" {
                    val errorMessage = getString(Res.string.error_updating_json_path)
                    mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))

                    mainScreenViewModel.onChangeJsonPath()

                    val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
                    mainScreenUIState.shouldBeTypeOf<MainScreenUIState.Error>()
                    mainScreenUIState.errMsg shouldBe errorMessage
                }
            }
        }

        "onChangeICalPath" - {
            "When mainScreenUIState is Ready" - {
                "should set mainScreenUIState to ShowChangeJsonPathDialog" {
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.Ready
                    mainScreenViewModel.onChangeICalPath()
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.ChangeICalPath
                }
            }

            "When mainScreenUIState is ShowChangeICalPathDialog" - {
                "should keep mainScreenUIState unchanged" {
                    mainScreenViewModel.onChangeJsonPath()
                    mainScreenViewModel.onChangeICalPath()
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.ChangeJsonPath
                }
            }

            "When mainScreenUIState is Error" - {
                "should keep mainScreenUIState unchanged" {
                    val errorMessage = getString(Res.string.error_updating_json_path)
                    mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))

                    mainScreenViewModel.onChangeICalPath()

                    val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
                    mainScreenUIState.shouldBeTypeOf<MainScreenUIState.Error>()
                    mainScreenUIState.errMsg shouldBe errorMessage
                }
            }
        }

        "updateJsonPath" - {
            "When JFileChooserResult is AbsolutePath" - {
                "should correctly trim the path" {
                    mainScreenViewModel.onChangeJsonPath()

                    val jFileChooserResult = JFileChooserResult.AbsolutePath(
                        absolutePath = mockProjectBasePath + "sample-folder1/sample-folder2",
                    )
                    mainScreenViewModel.updateJsonPath(jFileChooserResult = jFileChooserResult)

                    mainScreenViewModel.jsonPath.first() shouldBe "sample-folder1/sample-folder2"
                }

                "should set MainScreenUIState = Ready" {
                    mainScreenViewModel.onChangeJsonPath()

                    val jFileChooserResult = JFileChooserResult.AbsolutePath(
                        absolutePath = mockProjectBasePath + "sample-folder1/sample-folder2",
                    )
                    mainScreenViewModel.updateJsonPath(jFileChooserResult = jFileChooserResult)

                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.Ready
                }
            }

            "When JFileChooserResult is Cancelled" - {
                "should set MainScreenUIState = Ready" {
                    mainScreenViewModel.onChangeJsonPath()
                    mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Cancelled)
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.Ready
                }
            }

            "When JFileChooserResult is Error" - {
                "should set MainScreenUIState = Error with correct error message" {
                    val errorMessage = getString(Res.string.error_updating_json_path)
                    mainScreenViewModel.onChangeJsonPath()

                    mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))

                    val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
                    mainScreenUIState.shouldBeTypeOf<MainScreenUIState.Error>()
                    mainScreenUIState.errMsg shouldBe errorMessage
                }
            }
        }

        "updateICalPath" - {
            "When JFileChooserResult is AbsolutePath" - {
                "should correctly trim the path if it contains the base path" {
                    mainScreenViewModel.onChangeICalPath()

                    val jFileChooserResult = JFileChooserResult.AbsolutePath(
                        absolutePath = mockProjectBasePath + "sample-folder1/sample-folder2",
                    )
                    mainScreenViewModel.updateICalPath(jFileChooserResult = jFileChooserResult)

                    mainScreenViewModel.iCalPath.first() shouldBe "sample-folder1/sample-folder2"
                }

                "should keep the correct path if AbsolutePath does not contains the base path" {
                    mainScreenViewModel.onChangeICalPath()

                    val jFileChooserResult = JFileChooserResult.AbsolutePath(
                        absolutePath = "/sample-folder3/sample-folder4",
                    )
                    mainScreenViewModel.updateICalPath(jFileChooserResult = jFileChooserResult)

                    mainScreenViewModel.iCalPath.first() shouldBe "/sample-folder3/sample-folder4"
                }

                "should set MainScreenUIState = Ready" {
                    mainScreenViewModel.onChangeICalPath()

                    val jFileChooserResult = JFileChooserResult.AbsolutePath(
                        absolutePath = mockProjectBasePath + "sample-folder1/sample-folder2",
                    )
                    mainScreenViewModel.updateICalPath(jFileChooserResult = jFileChooserResult)

                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.Ready
                }
            }

            "When JFileChooserResult is Cancelled" - {
                "should set MainScreenUIState = Ready" {
                    mainScreenViewModel.onChangeICalPath()
                    mainScreenViewModel.updateICalPath(jFileChooserResult = JFileChooserResult.Cancelled)
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.Ready
                }
            }

            "When JFileChooserResult is Error" - {
                "should set MainScreenUIState = Error with correct error message" {
                    val errorMessage = getString(Res.string.error_updating_ical_path)
                    mainScreenViewModel.onChangeICalPath()

                    mainScreenViewModel.updateICalPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))

                    val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
                    mainScreenUIState.shouldBeTypeOf<MainScreenUIState.Error>()
                    mainScreenUIState.errMsg shouldBe errorMessage
                }
            }
        }

        "notifyErrorMessageDisplayed" - {
            "Should set MainScreenUIState = Ready" {
                mainScreenViewModel.onChangeICalPath() // Randomly alter the UI state

                mainScreenViewModel.notifyErrorMessageDisplayed()

                val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
                mainScreenUIState shouldBe MainScreenUIState.Ready
            }
        }

        "startExport" - {
            "Should set MainScreenUIState = Ready after running" {
                mockLocalFileRepository.getFileListResponse = Result.success(listOf("/some-path/some-file-1.json"))
                mockGetOutputFilenameUseCase.mockUseCaseResponse = "/some-path/some-file-1.ics"
                mockTimelineRepository.getTimeLineResponse = Result.success(mockTimeLineWithActivityVisitAndChildVisit)
                mockVEventFromPlaceVisitUseCase.mockUseCaseResponse = mockDefaultVEvent
                mockGetActivitySegmentVEventUseCase.mockUseCaseResponse = mockDefaultVEvent
                mockVEventFromChildVisitUseCase.mockUseCaseResponse = mockDefaultVEvent
                mockGetPlaceVisitVEventUseCase.mockUseCaseResponse = listOf(mockDefaultVEvent)

                mainScreenViewModel.startExport()

                val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
                mainScreenUIState shouldBe MainScreenUIState.Ready
            }

            "Should set MainScreenUIState = Error if localFileRepository.getFileList returns error" {
                mockGetPlaceVisitVEventUseCase.mockUseCaseResponse = listOf(mockDefaultVEvent)
                mockLocalFileRepository.getFileListResponse =
                    Result.failure(exception = Exception("some-exception-message"))

                mainScreenViewModel.startExport()

                val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
                with(mainScreenUIState as MainScreenUIState.Error) {
                    errMsg shouldBe "☠️ Error getting json file list: some-exception-message"
                }
            }

            "getActivitySegmentVEventUseCase" - {
                "Should append Ignore Log if getActivitySegmentVEventUseCase returns null" {
                    mockLocalFileRepository.getFileListResponse = Result.success(listOf("/some-path/some-file-1.json"))
                    mockGetOutputFilenameUseCase.mockUseCaseResponse = "/some-path/some-file-1.ics"
                    mockTimelineRepository.getTimeLineResponse = Result.success(mockTimeLineWithSingleActivity)
                    mockVEventFromPlaceVisitUseCase.mockUseCaseResponse = mockDefaultVEvent
                    mockGetActivitySegmentVEventUseCase.mockUseCaseResponse = null
                    mockVEventFromChildVisitUseCase.mockUseCaseResponse = mockDefaultVEvent
                    mockGetPlaceVisitVEventUseCase.mockUseCaseResponse = listOf(mockDefaultVEvent)

                    mainScreenViewModel.startExport()

                    val ignoredLogs = mainScreenViewModel.ignoredLogs.first()
                    ignoredLogs shouldBe listOf(
                        UILogEntry(emoji = "🚫", message = "08/07/2019 12:00:33: Activity FLYING"),
                    )
                }

                "Should append Exported Log if getActivitySegmentVEventUseCase returns VEvent" {
                    mockLocalFileRepository.getFileListResponse = Result.success(listOf("/some-path/some-file-1.json"))
                    mockGetOutputFilenameUseCase.mockUseCaseResponse = "/some-path/some-file-1.ics"
                    mockTimelineRepository.getTimeLineResponse = Result.success(mockTimeLineWithSingleActivity)
                    mockVEventFromPlaceVisitUseCase.mockUseCaseResponse = null
                    mockGetActivitySegmentVEventUseCase.mockUseCaseResponse = mockDefaultVEvent
                    mockVEventFromChildVisitUseCase.mockUseCaseResponse = null
                    mockGetPlaceVisitVEventUseCase.mockUseCaseResponse = null

                    mainScreenViewModel.startExport()

                    val exportedLogs = mainScreenViewModel.exportedLogs.first()
                    exportedLogs shouldBe listOf(
                        UILogEntry(emoji = "🗓", message = "12/11/2011 05:11:11: 📍 some-summary"),
                    )
                }
            }
        }

        "observeGetPlaceVisitVEventUseCaseFlows" - {
            "Should append Ignore Log if getPlaceVisitVEventUseCase.ignoredEvents emits something" {
                TestScope(StandardTestDispatcher()).runTest {
                    val uiLogEntry = UILogEntry(emoji = "🚫", message = "08/07/2019 12:00:33: Activity FLYING")

                    mockGetPlaceVisitVEventUseCase.emitIgnoredEvent(uiLogEntry = uiLogEntry)

                    val ignoredLogs = mainScreenViewModel.ignoredLogs.first()
                    ignoredLogs shouldBe listOf(uiLogEntry)
                }
            }

            "Should append Exported Log if getPlaceVisitVEventUseCase.exportedEvents emits something" {
                TestScope(StandardTestDispatcher()).runTest {
                    val uiLogEntry = UILogEntry(emoji = "🗓", message = "12/11/2011 05:11:11: 📍 some-summary")

                    mockGetPlaceVisitVEventUseCase.emitExportedEvent(uiLogEntry = uiLogEntry)

                    val exportedLogs = mainScreenViewModel.exportedLogs.first()
                    exportedLogs shouldBe listOf(uiLogEntry)
                }
            }
        }
    }
}
