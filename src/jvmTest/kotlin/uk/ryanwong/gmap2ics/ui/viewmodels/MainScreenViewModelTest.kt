/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.viewmodels

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import uk.ryanwong.gmap2ics.app.configs.MockConfig
import uk.ryanwong.gmap2ics.app.models.JFileChooserResult
import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.usecases.GetActivitySegmentVEventUseCase
import uk.ryanwong.gmap2ics.app.usecases.GetOutputFilenameUseCase
import uk.ryanwong.gmap2ics.app.usecases.VEventFromChildVisitUseCase
import uk.ryanwong.gmap2ics.app.usecases.VEventFromPlaceVisitUseCase
import uk.ryanwong.gmap2ics.app.usecases.mocks.MockGetActivitySegmentVEventUseCase
import uk.ryanwong.gmap2ics.app.usecases.mocks.MockGetOutputFilenameUseCase
import uk.ryanwong.gmap2ics.app.usecases.mocks.MockVEventFromChildVisitUseCase
import uk.ryanwong.gmap2ics.app.usecases.mocks.MockVEventFromPlaceVisitUseCase
import uk.ryanwong.gmap2ics.data.repository.LocalFileRepository
import uk.ryanwong.gmap2ics.data.repository.TimelineRepository
import uk.ryanwong.gmap2ics.data.repository.mocks.MockLocalFileRepository
import uk.ryanwong.gmap2ics.data.repository.mocks.MockTimelineRepository
import uk.ryanwong.gmap2ics.ui.screens.MainScreenUIState
import uk.ryanwong.gmap2ics.ui.utils.mocks.MockResourceBundle

@OptIn(ExperimentalCoroutinesApi::class)
internal class MainScreenViewModelTest : FreeSpec() {

    private lateinit var mainScreenViewModel: MainScreenViewModel
    private lateinit var mockTimelineRepository: TimelineRepository
    private lateinit var mockLocalFileRepository: LocalFileRepository
    private lateinit var mockVEventFromPlaceVisitUseCase: VEventFromPlaceVisitUseCase
    private lateinit var mockVEventFromChildVisitUseCase: VEventFromChildVisitUseCase
    private lateinit var mockGetActivitySegmentVEventUseCase: GetActivitySegmentVEventUseCase
    private lateinit var mockGetOutputFilenameUseCase: GetOutputFilenameUseCase

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
        lastModified = "2011-11-11T11:22:22.222Z"
    )

    // These tests don't touch VEvent (yet), so we feed in a default mock result
    private fun setupViewModel(
        mockVEventFromPlaceVisitUseCaseResponse: VEvent = mockDefaultVEvent
    ) {
        mockTimelineRepository = MockTimelineRepository()
        mockLocalFileRepository = MockLocalFileRepository()
        mockVEventFromPlaceVisitUseCase =
            MockVEventFromPlaceVisitUseCase(mockUseCaseResponse = mockVEventFromPlaceVisitUseCaseResponse)
        mockVEventFromChildVisitUseCase = MockVEventFromChildVisitUseCase()
        mockGetActivitySegmentVEventUseCase = MockGetActivitySegmentVEventUseCase()
        mockGetOutputFilenameUseCase = MockGetOutputFilenameUseCase()

        mainScreenViewModel = MainScreenViewModel(
            configFile = MockConfig(),
            timelineRepository = mockTimelineRepository,
            localFileRepository = mockLocalFileRepository,
            getActivitySegmentVEventUseCase = mockGetActivitySegmentVEventUseCase,
            getOutputFilenameUseCase = mockGetOutputFilenameUseCase,
            vEventFromPlaceVisitUseCase = mockVEventFromPlaceVisitUseCase,
            vEventFromChildVisitUseCase = mockVEventFromChildVisitUseCase,
            resourceBundle = MockResourceBundle(),
            projectBasePath = mockProjectBasePath,
            dispatcher = UnconfinedTestDispatcher()
        )
    }

    init {
        "setExportPlaceVisit" - {
            "should update enablePlacesApiLookup correctly" {
                // 🔴 Given
                setupViewModel()
                val initialState = mainScreenViewModel.exportPlaceVisit.first()

                // 🟡 When
                mainScreenViewModel.setExportPlaceVisit(enabled = !initialState)

                // 🟢 Then
                mainScreenViewModel.exportPlaceVisit.first() shouldBe !initialState
            }
        }

        "setExportActivitySegment" - {
            "should update enablePlacesApiLookup correctly" {
                // 🔴 Given
                setupViewModel()
                val initialState = mainScreenViewModel.exportActivitySegment.first()

                // 🟡 When
                mainScreenViewModel.setExportActivitySegment(enabled = !initialState)

                // 🟢 Then
                mainScreenViewModel.exportActivitySegment.first() shouldBe !initialState
            }
        }

        "setEnablePlacesApiLookup" - {
            "should update enablePlacesApiLookup correctly" {
                // 🔴 Given
                setupViewModel()
                val initialState = mainScreenViewModel.enablePlacesApiLookup.first()

                // 🟡 When
                mainScreenViewModel.setEnablePlacesApiLookup(enabled = !initialState)

                // 🟢 Then
                mainScreenViewModel.enablePlacesApiLookup.first() shouldBe !initialState
            }
        }

        "setVerboseLogs" - {
            "should update _verboseLogs correctly" {
                // 🔴 Given
                setupViewModel()
                val initialState = mainScreenViewModel.verboseLogs.first()

                // 🟡 When
                mainScreenViewModel.setVerboseLogs(enabled = !initialState)

                // 🟢 Then
                mainScreenViewModel.verboseLogs.first() shouldBe !initialState
            }
        }

        "onChangeJsonPath" - {
            "When mainScreenUIState is Ready" - {
                "should set mainScreenUIState to ShowChangeJsonPathDialog" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.Ready

                    // 🟡 When
                    mainScreenViewModel.onChangeJsonPath()

                    // 🟢 Then
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.ChangeJsonPath
                }
            }

            "When mainScreenUIState is ShowChangeICalPathDialog" - {
                "should keep mainScreenUIState unchanged" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.onChangeICalPath()

                    // 🟡 When
                    mainScreenViewModel.onChangeJsonPath()

                    // 🟢 Then
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.ChangeICalPath
                }
            }

            "When mainScreenUIState is Error" - {
                "should keep mainScreenUIState unchanged" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))

                    // 🟡 When
                    mainScreenViewModel.onChangeJsonPath()

                    // 🟢 Then
                    mainScreenViewModel.mainScreenUIState.first().shouldBeTypeOf<MainScreenUIState.Error>()
                }
            }
        }

        "onChangeICalPath" - {
            "When mainScreenUIState is Ready" - {
                "should set mainScreenUIState to ShowChangeJsonPathDialog" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.Ready

                    // 🟡 When
                    mainScreenViewModel.onChangeICalPath()

                    // 🟢 Then
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.ChangeICalPath
                }
            }

            "When mainScreenUIState is ShowChangeICalPathDialog" - {
                "should keep mainScreenUIState unchanged" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.onChangeJsonPath()

                    // 🟡 When
                    mainScreenViewModel.onChangeICalPath()

                    // 🟢 Then
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.ChangeJsonPath
                }
            }

            "When mainScreenUIState is Error" - {
                "should keep mainScreenUIState unchanged" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))

                    // 🟡 When
                    mainScreenViewModel.onChangeICalPath()

                    // 🟢 Then
                    mainScreenViewModel.mainScreenUIState.first().shouldBeTypeOf<MainScreenUIState.Error>()
                }
            }
        }

        "updateJsonPath" - {
            "When JFileChooserResult is AbsolutePath" - {
                "should correctly trim the path" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.onChangeJsonPath()

                    // 🟡 When
                    val jFileChooserResult = JFileChooserResult.AbsolutePath(
                        absolutePath = mockProjectBasePath + "sample-folder1/sample-folder2"
                    )
                    mainScreenViewModel.updateJsonPath(jFileChooserResult = jFileChooserResult)

                    // 🟢 Then
                    mainScreenViewModel.jsonPath.first() shouldBe "sample-folder1/sample-folder2"
                }

                "should set MainScreenUIState = Ready" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.onChangeJsonPath()

                    // 🟡 When
                    val jFileChooserResult = JFileChooserResult.AbsolutePath(
                        absolutePath = mockProjectBasePath + "sample-folder1/sample-folder2"
                    )
                    mainScreenViewModel.updateJsonPath(jFileChooserResult = jFileChooserResult)

                    // 🟢 Then
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.Ready
                }
            }

            "When JFileChooserResult is Cancelled" - {
                "should set MainScreenUIState = Ready" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.onChangeJsonPath()

                    // 🟡 When
                    mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Cancelled)

                    // 🟢 Then
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.Ready
                }
            }


            "When JFileChooserResult is Error" - {
                "should set MainScreenUIState = Error with correct error message" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.onChangeJsonPath()

                    // 🟡 When
                    mainScreenViewModel.updateJsonPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))

                    // 🟢 Then
                    val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
                    (mainScreenUIState as MainScreenUIState.Error).errMsg shouldBe "mock-string-for-error.updating.json.path"
                }
            }
        }

        "updateICalPath" - {
            "When JFileChooserResult is AbsolutePath" - {
                "should correctly trim the path if it contains the base path" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.onChangeICalPath()

                    // 🟡 When
                    val jFileChooserResult = JFileChooserResult.AbsolutePath(
                        absolutePath = mockProjectBasePath + "sample-folder1/sample-folder2"
                    )
                    mainScreenViewModel.updateICalPath(jFileChooserResult = jFileChooserResult)

                    // 🟢 Then
                    mainScreenViewModel.iCalPath.first() shouldBe "sample-folder1/sample-folder2"
                }

                "should keep the correct path if AbsolutePath does not contains the base path" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.onChangeICalPath()

                    // 🟡 When
                    val jFileChooserResult = JFileChooserResult.AbsolutePath(
                        absolutePath = "/sample-folder3/sample-folder4"
                    )
                    mainScreenViewModel.updateICalPath(jFileChooserResult = jFileChooserResult)

                    // 🟢 Then
                    mainScreenViewModel.iCalPath.first() shouldBe "/sample-folder3/sample-folder4"
                }

                "should set MainScreenUIState = Ready" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.onChangeICalPath()

                    // 🟡 When
                    val jFileChooserResult = JFileChooserResult.AbsolutePath(
                        absolutePath = mockProjectBasePath + "sample-folder1/sample-folder2"
                    )
                    mainScreenViewModel.updateICalPath(jFileChooserResult = jFileChooserResult)

                    // 🟢 Then
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.Ready
                }
            }

            "When JFileChooserResult is Cancelled" - {
                "should set MainScreenUIState = Ready" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.onChangeICalPath()

                    // 🟡 When
                    mainScreenViewModel.updateICalPath(jFileChooserResult = JFileChooserResult.Cancelled)

                    // 🟢 Then
                    mainScreenViewModel.mainScreenUIState.first() shouldBe MainScreenUIState.Ready
                }
            }


            "When JFileChooserResult is Error" - {
                "should set MainScreenUIState = Error with correct error message" {
                    // 🔴 Given
                    setupViewModel()
                    mainScreenViewModel.onChangeICalPath()

                    // 🟡 When
                    mainScreenViewModel.updateICalPath(jFileChooserResult = JFileChooserResult.Error(errorCode = 521))

                    // 🟢 Then
                    val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
                    (mainScreenUIState as MainScreenUIState.Error).errMsg shouldBe "mock-string-for-error.updating.ical.path"
                }
            }
        }

        "notifyErrorMessageDisplayed" - {
            "Should set MainScreenUIState = Ready" {
                // 🔴 Given
                setupViewModel()
                mainScreenViewModel.onChangeICalPath() // Randomly alter the UI state

                // 🟡 When
                mainScreenViewModel.notifyErrorMessageDisplayed()

                // 🟢 Then
                val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
                mainScreenUIState shouldBe MainScreenUIState.Ready
            }
        }

        /***
         * Note: This is the core function of the App.
         * It has to be tested heavily.
         */
        "startExport" - {
            "Should set MainScreenUIState = Ready after running" {
                // 🔴 Given
                setupViewModel()
          //      mockLocalFileRepository.getFileListResponse = Result.success(listOf("some-file-1"))
        //        mockTimelineRepository.getTimeLineResponse = Result.success(mockTimeLineFromJsonString)

                // 🟡 When
                mainScreenViewModel.startExport()

                // 🟢 Then
                val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
                mainScreenUIState shouldBe MainScreenUIState.Ready
            }

            "Should set MainScreenUIState = Error if localFileRepository.getFileList returns error" {
                // 🔴 Given
                setupViewModel()
//                mockLocalFileRepository.getFileListResponse =
//                    Result.failure(exception = Exception("some-exception-message"))

                // 🟡 When
                mainScreenViewModel.startExport()

                // 🟢 Then
                val mainScreenUIState = mainScreenViewModel.mainScreenUIState.first()
                with(mainScreenUIState as MainScreenUIState.Error) {
                    errMsg shouldBe "☠️ Error getting json file list: some-exception-message"
                }
            }
        }
    }
}
