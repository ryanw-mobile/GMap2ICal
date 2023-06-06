/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases.impl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import uk.ryanwong.gmap2ics.app.models.UILogEntry
import uk.ryanwong.gmap2ics.app.usecases.GetPlaceVisitVEventUseCase
import uk.ryanwong.gmap2ics.app.usecases.mocks.MockVEventFromChildVisitUseCase
import uk.ryanwong.gmap2ics.app.usecases.mocks.MockVEventFromPlaceVisitUseCase

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetPlaceVisitVEventUseCaseImplTest : FreeSpec() {

    private lateinit var getPlaceVisitVEventUseCase: GetPlaceVisitVEventUseCase
    private lateinit var mockVEventFromChildVisitUseCase: MockVEventFromChildVisitUseCase
    private lateinit var mockVEventFromPlaceVisitUseCase: MockVEventFromPlaceVisitUseCase
    private lateinit var scope: TestScope
    private fun setupDispatcher() {
        val dispatcher = UnconfinedTestDispatcher()
        scope = TestScope(dispatcher)
    }

    private fun setupUseCase() {
        mockVEventFromChildVisitUseCase = MockVEventFromChildVisitUseCase()
        mockVEventFromPlaceVisitUseCase = MockVEventFromPlaceVisitUseCase()

        getPlaceVisitVEventUseCase = GetPlaceVisitVEventUseCaseImpl(
            vEventFromChildVisitUseCase = mockVEventFromChildVisitUseCase,
            vEventFromPlaceVisitUseCase = mockVEventFromPlaceVisitUseCase,
        )
    }

    init {
        "placeVisit.location.placeId is in ignoredVisitedPlaceIds" - {
            "Should return empty VEvent list" {
                setupDispatcher()
                scope.runTest {
                    // Given
                    setupUseCase()
                    val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.mockPlaceVisit
                    val ignoredVisitedPlaceIds = listOf("some-place-id")

                    // When
                    val vEvent = getPlaceVisitVEventUseCase(
                        placeVisit = placeVisit,
                        ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                        enablePlacesApiLookup = true,
                        verboseConsoleLog = true,
                    )

                    // Then
                    vEvent shouldBe emptyList()
                }
            }

            "Should emit UILogEntry through ignoredEvents" {
                setupDispatcher()
                scope.runTest {
                    // Given
                    setupUseCase()
                    val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.mockPlaceVisit
                    val ignoredVisitedPlaceIds = listOf("some-place-id")
                    val ignoredEvents = mutableListOf<UILogEntry>()
                    val jobs = launch {
                        getPlaceVisitVEventUseCase.ignoredEvents.collect {
                            ignoredEvents.add(it)
                        }
                    }

                    // When
                    getPlaceVisitVEventUseCase(
                        placeVisit = placeVisit,
                        ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                        enablePlacesApiLookup = true,
                        verboseConsoleLog = true,
                    )
                    jobs.cancel()

                    // Then
                    ignoredEvents shouldBe listOf(
                        UILogEntry(
                            emoji = "🚫",
                            message = "11/11/2011 20:11:11: Place ID some-place-id",
                        ),
                    )
                }
            }
        }

        "placeVisit.location.placeId is not in ignoredVisitedPlaceIds" - {
            "placeVisit has no child visits" - {
                "Should return correct VEvent list" {
                    setupDispatcher()
                    scope.runTest {
                        // Given
                        setupUseCase()
                        val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.mockPlaceVisit
                        val ignoredVisitedPlaceIds = listOf("some-other-place-id")
                        mockVEventFromPlaceVisitUseCase.mockUseCaseResponse =
                            GetPlaceVisitVEventUseCaseImplTestData.mockVEvent

                        // When
                        val vEvent = getPlaceVisitVEventUseCase(
                            placeVisit = placeVisit,
                            ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                            enablePlacesApiLookup = true,
                            verboseConsoleLog = true,
                        )

                        // Then
                        vEvent shouldBe listOf(GetPlaceVisitVEventUseCaseImplTestData.mockVEvent)
                    }
                }

                "Should emit UILogEntry through exportedEvents" {
                    setupDispatcher()
                    scope.runTest {
                        // Given
                        setupUseCase()
                        val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.mockPlaceVisit
                        val ignoredVisitedPlaceIds = listOf("some-other-place-id")
                        mockVEventFromPlaceVisitUseCase.mockUseCaseResponse =
                            GetPlaceVisitVEventUseCaseImplTestData.mockVEvent
                        val exportedEvents = mutableListOf<UILogEntry>()
                        val jobs = launch {
                            getPlaceVisitVEventUseCase.exportedEvents.collect {
                                exportedEvents.add(it)
                            }
                        }

                        // When
                        getPlaceVisitVEventUseCase(
                            placeVisit = placeVisit,
                            ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                            enablePlacesApiLookup = true,
                            verboseConsoleLog = true,
                        )
                        jobs.cancel()

                        // Then
                        exportedEvents shouldBe listOf(
                            UILogEntry(emoji = "🗓", message = "11/11/2011 20:11:11: 🏧 some-place-name"),
                        )
                    }
                }
            }

            "placeVisit has child visits" - {
                "child visit is in ignoredVisitedPlaceIds" - {

                    "Should skip processing this child visit" {
                        setupDispatcher()
                        scope.runTest {
                            // Given
                            setupUseCase()
                            val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.mockPlaceVisitWithOneChildVisit
                            val ignoredVisitedPlaceIds = listOf("some-child-place-id")
                            mockVEventFromPlaceVisitUseCase.mockUseCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.mockVEvent
                            // Still have to mock this response to make sure it has not been called
                            mockVEventFromChildVisitUseCase.mockUseCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.mockVEventSomeChildPlaceId

                            // When
                            val vEvent = getPlaceVisitVEventUseCase(
                                placeVisit = placeVisit,
                                ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                                enablePlacesApiLookup = true,
                                verboseConsoleLog = true,
                            )

                            // Then
                            vEvent shouldBe listOf(GetPlaceVisitVEventUseCaseImplTestData.mockVEvent)
                        }
                    }

                    "Should only skip the ignored child visit if there are multiple child visits" {
                        setupDispatcher()
                        scope.runTest {
                            // Given
                            setupUseCase()
                            val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.mockPlaceVisitWithTwoChildVisit
                            val ignoredVisitedPlaceIds = listOf("some-another-child-place-id")
                            mockVEventFromPlaceVisitUseCase.mockUseCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.mockVEvent
                            // Still have to mock this response to make sure it has not been called
                            mockVEventFromChildVisitUseCase.mockUseCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.mockVEventSomeChildPlaceId

                            // When
                            val vEvent = getPlaceVisitVEventUseCase(
                                placeVisit = placeVisit,
                                ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                                enablePlacesApiLookup = true,
                                verboseConsoleLog = true,
                            )

                            // Then
                            vEvent shouldContainExactly listOf(
                                GetPlaceVisitVEventUseCaseImplTestData.mockVEvent,
                                GetPlaceVisitVEventUseCaseImplTestData.mockVEventSomeChildPlaceId,
                            )
                        }
                    }

                    "Should emit UILogEntry through ignoredEvents" {
                        setupDispatcher()
                        scope.runTest {
                            // Given
                            setupUseCase()
                            val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.mockPlaceVisitWithOneChildVisit
                            val ignoredVisitedPlaceIds = listOf("some-child-place-id")
                            mockVEventFromPlaceVisitUseCase.mockUseCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.mockVEvent
                            val ignoredEvents = mutableListOf<UILogEntry>()
                            val jobs = launch {
                                getPlaceVisitVEventUseCase.ignoredEvents.collect {
                                    ignoredEvents.add(it)
                                }
                            }

                            // When
                            getPlaceVisitVEventUseCase(
                                placeVisit = placeVisit,
                                ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                                enablePlacesApiLookup = true,
                                verboseConsoleLog = true,
                            )
                            jobs.cancel()

                            // Then
                            ignoredEvents shouldBe listOf(
                                UILogEntry(
                                    emoji = "🚫",
                                    message = "03/01/2022 14:19:00: Place ID some-child-place-id",
                                ),
                            )
                        }
                    }
                }

                "child visit is not in ignoredVisitedPlaceIds" - {
                    "Should include the child visit in the list of VEvent" - {
                        setupDispatcher()
                        scope.runTest {
                            // Given
                            setupUseCase()
                            val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.mockPlaceVisitWithOneChildVisit
                            val ignoredVisitedPlaceIds = listOf("some-other-child-place-id")
                            mockVEventFromPlaceVisitUseCase.mockUseCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.mockVEvent
                            mockVEventFromChildVisitUseCase.mockUseCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.mockVEventSomeChildPlaceId

                            // When
                            val vEvent = getPlaceVisitVEventUseCase(
                                placeVisit = placeVisit,
                                ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                                enablePlacesApiLookup = true,
                                verboseConsoleLog = true,
                            )

                            // Then
                            vEvent shouldContainExactly listOf(
                                GetPlaceVisitVEventUseCaseImplTestData.mockVEvent,
                                GetPlaceVisitVEventUseCaseImplTestData.mockVEventSomeChildPlaceId,
                            )
                        }
                    }

                    "Should emit UILogEntry through exportedEvents" {
                        setupDispatcher()
                        scope.runTest {
                            // Given
                            setupUseCase()
                            val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.mockPlaceVisitWithOneChildVisit
                            val ignoredVisitedPlaceIds = listOf("some-other-child-place-id")
                            mockVEventFromPlaceVisitUseCase.mockUseCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.mockVEvent
                            mockVEventFromChildVisitUseCase.mockUseCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.mockVEventSomeChildPlaceId

                            val exportedEvents = mutableListOf<UILogEntry>()
                            val jobs = launch {
                                getPlaceVisitVEventUseCase.exportedEvents.collect {
                                    exportedEvents.add(it)
                                }
                            }

                            // When
                            getPlaceVisitVEventUseCase(
                                placeVisit = placeVisit,
                                ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                                enablePlacesApiLookup = true,
                                verboseConsoleLog = true,
                            )
                            jobs.cancel()

                            // Then
                            exportedEvents shouldContainExactly listOf(
                                UILogEntry(emoji = "🗓", message = "11/11/2011 20:11:11: 🏧 some-place-name"),
                                UILogEntry(emoji = "🗓", message = "03/01/2022 14:19:00: 📍 some-child-name"),
                            )
                        }
                    }
                }
            }
        }
    }
}
