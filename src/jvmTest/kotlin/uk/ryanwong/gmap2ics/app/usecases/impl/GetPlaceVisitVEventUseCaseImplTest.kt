/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.usecases.impl

import com.esri.core.geometry.Polygon
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.UILogEntry
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.PlaceVisit
import uk.ryanwong.gmap2ics.app.usecases.GetPlaceVisitVEventUseCase
import uk.ryanwong.gmap2ics.app.usecases.mocks.MockVEventFromChildVisitUseCase
import uk.ryanwong.gmap2ics.app.usecases.mocks.MockVEventFromPlaceVisitUseCase
import us.dustinj.timezonemap.TimeZone

@OptIn(ExperimentalCoroutinesApi::class)
class GetPlaceVisitVEventUseCaseImplTest : FreeSpec() {

    private lateinit var getPlaceVisitVEventUseCase: GetPlaceVisitVEventUseCase
    private lateinit var mockVEventFromChildVisitUseCase: MockVEventFromChildVisitUseCase
    private lateinit var mockVEventFromPlaceVisitUseCase: MockVEventFromPlaceVisitUseCase

    private val someLatitudeE7 = 263383300
    private val someLongitudeE7 = 1278000000
    private val mockPlaceVisit = PlaceVisit(
        // meaningless values just to match the format
        durationEndTimestamp = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
        durationStartTimestamp = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
        lastEditedTimestamp = "2011-11-11T11:22:22.222Z",
        location = Location(
            placeId = "some-place-id",
            latitudeE7 = someLatitudeE7,
            longitudeE7 = someLongitudeE7
        ),
        childVisits = emptyList(),
        eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon())
    )

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
                    val placeVisit = mockPlaceVisit
                    val ignoredVisitedPlaceIds = listOf("some-place-id")

                    // When
                    val vEvent = getPlaceVisitVEventUseCase(
                        placeVisit = placeVisit,
                        ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
                        enablePlacesApiLookup = true,
                        verboseConsoleLog = true
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
                    val placeVisit = mockPlaceVisit
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
                        verboseConsoleLog = true
                    )
                    jobs.cancel()

                    // Then
                    ignoredEvents shouldBe listOf(
                        UILogEntry(
                            emoji = "🚫",
                            message = "11/11/2011 20:11:11: Place ID some-place-id"
                        )
                    )
                }
            }
        }

        "placeVisit.location.placeId is not in ignoredVisitedPlaceIds" - {
            "placeVisit has no child visits" - {
                "Should return correct VEvent list" {
                }

                "Should emit UILogEntry through exportedEvents" {
                }
            }

            "placeVisit has child visits" - {
                "child visit is in ignoredVisitedPlaceIds" - {

                    "Should skip processing this child visit" {
                    }
                }
            }
        }
    }
}
