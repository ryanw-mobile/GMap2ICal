/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.viewmodels

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.flow.first
import uk.ryanwong.gmap2ics.app.configs.MockConfig
import uk.ryanwong.gmap2ics.app.models.JFileChooserResult
import uk.ryanwong.gmap2ics.app.models.VEvent
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.data.repository.MockLocalFileRepository
import uk.ryanwong.gmap2ics.data.repository.MockTimelineRepository
import uk.ryanwong.gmap2ics.ui.screens.MainScreenUIState
import uk.ryanwong.gmap2ics.ui.usecases.MockVEventFromActivitySegmentUseCase
import uk.ryanwong.gmap2ics.ui.usecases.MockVEventFromChildVisitUseCase
import uk.ryanwong.gmap2ics.ui.usecases.MockVEventFromPlaceVisitUseCase
import uk.ryanwong.gmap2ics.ui.utils.MockResourceBundle

internal class MainScreenViewModelTest : FreeSpec() {

    lateinit var mainScreenViewModel: MainScreenViewModel
    lateinit var mockTimelineRepository: MockTimelineRepository
    lateinit var mockLocalFileRepository: MockLocalFileRepository
    lateinit var mockVEventFromActivitySegmentUseCase: MockVEventFromActivitySegmentUseCase
    lateinit var mockVEventFromPlaceVisitUseCase: MockVEventFromPlaceVisitUseCase
    lateinit var mockVEventFromChildVisitUseCase: MockVEventFromChildVisitUseCase

    private val mockProjectBasePath = "/default-base-path/default-sub-folder/"

    private val mockDefaultVEvent = VEvent(
        uid = "2011-11-11T11:22:22.222Z",
        placeId = "location-id-to-be-kept",
        dtStamp = "2011-11-11T11:22:22.222Z",
        organizer = null,
        dtStart = "20111111T201111",
        dtEnd = "20111111T202222",
        dtTimeZone = "Asia/Tokyo",
        summary = "📍 some-summary",
        location = "",
        geo = LatLng(latitude = 26.33833, longitude = 127.8),
        description = "Place ID:\\nlocation-id-to-be-kept\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:location-id-to-be-kept",
        url = "https://www.google.com/maps/place/?q=place_id:location-id-to-be-kept",
        lastModified = "2011-11-11T11:22:22.222Z"
    )

    // These tests don't touch VEvent (yet), so we feed in a default mock result
    private fun setupViewModel(
        mockVEventFromPlaceVisitUseCaseResponse: VEvent = mockDefaultVEvent,
        mockVEventFromActivitySegmentUseCaseResponse: VEvent = mockDefaultVEvent
    ) {
        mockTimelineRepository = MockTimelineRepository()
        mockLocalFileRepository = MockLocalFileRepository()
        mockVEventFromActivitySegmentUseCase =
            MockVEventFromActivitySegmentUseCase(mockUseCaseResponse = mockVEventFromActivitySegmentUseCaseResponse)
        mockVEventFromPlaceVisitUseCase =
            MockVEventFromPlaceVisitUseCase(mockUseCaseResponse = mockVEventFromPlaceVisitUseCaseResponse)
        mockVEventFromChildVisitUseCase = MockVEventFromChildVisitUseCase()

        mainScreenViewModel = MainScreenViewModel(
            configFile = MockConfig(),
            timelineRepository = mockTimelineRepository,
            localFileRepository = mockLocalFileRepository,
            vEventFromActivitySegmentUseCase = mockVEventFromActivitySegmentUseCase,
            vEventFromPlaceVisitUseCase = mockVEventFromPlaceVisitUseCase,
            vEventFromChildVisitUseCase = mockVEventFromChildVisitUseCase,
            resourceBundle = MockResourceBundle(),
            projectBasePath = mockProjectBasePath
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
    }
}
