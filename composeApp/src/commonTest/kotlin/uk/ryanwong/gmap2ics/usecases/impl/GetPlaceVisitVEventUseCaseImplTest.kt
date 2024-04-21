/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.impl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import uk.ryanwong.gmap2ics.domain.models.UILogEntry
import uk.ryanwong.gmap2ics.domain.usecases.GetPlaceVisitVEventUseCase
import uk.ryanwong.gmap2ics.usecases.GetPlaceVisitVEventUseCaseImpl
import uk.ryanwong.gmap2ics.usecases.fakes.FakeVEventFromChildVisitUseCase
import uk.ryanwong.gmap2ics.usecases.fakes.FakeVEventFromPlaceVisitUseCase

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetPlaceVisitVEventUseCaseImplTest : FreeSpec() {

    private lateinit var getPlaceVisitVEventUseCase: GetPlaceVisitVEventUseCase
    private lateinit var fakeVEventFromChildVisitUseCase: FakeVEventFromChildVisitUseCase
    private lateinit var fakeVEventFromPlaceVisitUseCase: FakeVEventFromPlaceVisitUseCase
    private lateinit var scope: TestScope
    private fun setupDispatcher() {
        val dispatcher = UnconfinedTestDispatcher()
        scope = TestScope(dispatcher)
    }

    private fun setupUseCase() {
        fakeVEventFromChildVisitUseCase = FakeVEventFromChildVisitUseCase()
        fakeVEventFromPlaceVisitUseCase = FakeVEventFromPlaceVisitUseCase()

        getPlaceVisitVEventUseCase = GetPlaceVisitVEventUseCaseImpl(
            vEventFromChildVisitUseCase = fakeVEventFromChildVisitUseCase,
            vEventFromPlaceVisitUseCase = fakeVEventFromPlaceVisitUseCase,
        )
    }

    init {
        "placeVisit.location.placeId is in ignoredVisitedPlaceIds" - {
            "Should return empty VEvent list" {
                setupDispatcher()
                scope.runTest {
                    // Given
                    setupUseCase()
                    val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisit
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
                    val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisit
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
                        val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisit
                        val ignoredVisitedPlaceIds = listOf("some-other-place-id")
                        fakeVEventFromPlaceVisitUseCase.useCaseResponse =
                            GetPlaceVisitVEventUseCaseImplTestData.vEvent

                        // When
                        val vEvent = getPlaceVisitVEventUseCase(
                            placeVisit = placeVisit,
                            ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                            enablePlacesApiLookup = true,
                            verboseConsoleLog = true,
                        )

                        // Then
                        vEvent shouldBe listOf(GetPlaceVisitVEventUseCaseImplTestData.vEvent)
                    }
                }

                "Should emit UILogEntry through exportedEvents" {
                    setupDispatcher()
                    scope.runTest {
                        // Given
                        setupUseCase()
                        val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisit
                        val ignoredVisitedPlaceIds = listOf("some-other-place-id")
                        fakeVEventFromPlaceVisitUseCase.useCaseResponse =
                            GetPlaceVisitVEventUseCaseImplTestData.vEvent
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
                            val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisitWithOneChildVisit
                            val ignoredVisitedPlaceIds = listOf("some-child-place-id")
                            fakeVEventFromPlaceVisitUseCase.useCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.vEvent
                            // Still have to mock this response to make sure it has not been called
                            fakeVEventFromChildVisitUseCase.useCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.vEventSomeChildPlaceId

                            // When
                            val vEvent = getPlaceVisitVEventUseCase(
                                placeVisit = placeVisit,
                                ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                                enablePlacesApiLookup = true,
                                verboseConsoleLog = true,
                            )

                            // Then
                            vEvent shouldBe listOf(GetPlaceVisitVEventUseCaseImplTestData.vEvent)
                        }
                    }

                    "Should only skip the ignored child visit if there are multiple child visits" {
                        setupDispatcher()
                        scope.runTest {
                            // Given
                            setupUseCase()
                            val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisitWithTwoChildVisit
                            val ignoredVisitedPlaceIds = listOf("some-another-child-place-id")
                            fakeVEventFromPlaceVisitUseCase.useCaseResponse = GetPlaceVisitVEventUseCaseImplTestData.vEvent
                            // Still have to fake this response to make sure it has not been called
                            fakeVEventFromChildVisitUseCase.useCaseResponse = GetPlaceVisitVEventUseCaseImplTestData.vEventSomeChildPlaceId

                            // When
                            val vEvent = getPlaceVisitVEventUseCase(
                                placeVisit = placeVisit,
                                ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                                enablePlacesApiLookup = true,
                                verboseConsoleLog = true,
                            )

                            // Then
                            vEvent shouldContainExactly listOf(
                                GetPlaceVisitVEventUseCaseImplTestData.vEvent,
                                GetPlaceVisitVEventUseCaseImplTestData.vEventSomeChildPlaceId,
                            )
                        }
                    }

                    "Should emit UILogEntry through ignoredEvents" {
                        setupDispatcher()
                        scope.runTest {
                            // Given
                            setupUseCase()
                            val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisitWithOneChildVisit
                            val ignoredVisitedPlaceIds = listOf("some-child-place-id")
                            fakeVEventFromPlaceVisitUseCase.useCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.vEvent
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
                            val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisitWithOneChildVisit
                            val ignoredVisitedPlaceIds = listOf("some-other-child-place-id")
                            fakeVEventFromPlaceVisitUseCase.useCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.vEvent
                            fakeVEventFromChildVisitUseCase.useCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.vEventSomeChildPlaceId

                            // When
                            val vEvent = getPlaceVisitVEventUseCase(
                                placeVisit = placeVisit,
                                ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                                enablePlacesApiLookup = true,
                                verboseConsoleLog = true,
                            )

                            // Then
                            vEvent shouldContainExactly listOf(
                                GetPlaceVisitVEventUseCaseImplTestData.vEvent,
                                GetPlaceVisitVEventUseCaseImplTestData.vEventSomeChildPlaceId,
                            )
                        }
                    }

                    "Should emit UILogEntry through exportedEvents" {
                        setupDispatcher()
                        scope.runTest {
                            // Given
                            setupUseCase()
                            val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisitWithOneChildVisit
                            val ignoredVisitedPlaceIds = listOf("some-other-child-place-id")
                            fakeVEventFromPlaceVisitUseCase.useCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.vEvent
                            fakeVEventFromChildVisitUseCase.useCaseResponse =
                                GetPlaceVisitVEventUseCaseImplTestData.vEventSomeChildPlaceId

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
