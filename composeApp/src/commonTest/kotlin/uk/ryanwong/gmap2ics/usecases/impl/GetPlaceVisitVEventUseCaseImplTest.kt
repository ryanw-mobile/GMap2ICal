/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.usecases.impl

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import uk.ryanwong.gmap2ics.domain.models.UILogEntry
import uk.ryanwong.gmap2ics.domain.usecases.GetPlaceVisitVEventUseCaseImpl
import uk.ryanwong.gmap2ics.domain.usecases.interfaces.GetPlaceVisitVEventUseCase
import uk.ryanwong.gmap2ics.usecases.fakes.FakeVEventFromChildVisitUseCase
import uk.ryanwong.gmap2ics.usecases.fakes.FakeVEventFromPlaceVisitUseCase
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetPlaceVisitVEventUseCaseImplTest {

    private lateinit var getPlaceVisitVEventUseCase: GetPlaceVisitVEventUseCase
    private lateinit var fakeVEventFromChildVisitUseCase: FakeVEventFromChildVisitUseCase
    private lateinit var fakeVEventFromPlaceVisitUseCase: FakeVEventFromPlaceVisitUseCase
    private lateinit var testScope: TestScope

    @BeforeTest
    fun setup() {
        val dispatcher = UnconfinedTestDispatcher()
        testScope = TestScope(dispatcher)

        fakeVEventFromChildVisitUseCase = FakeVEventFromChildVisitUseCase()
        fakeVEventFromPlaceVisitUseCase = FakeVEventFromPlaceVisitUseCase()

        getPlaceVisitVEventUseCase = GetPlaceVisitVEventUseCaseImpl(
            vEventFromChildVisitUseCase = fakeVEventFromChildVisitUseCase,
            vEventFromPlaceVisitUseCase = fakeVEventFromPlaceVisitUseCase,
        )
    }

    // 🗂️ placeVisit.location.placeId is in ignoredVisitedPlaceIds
    @Test
    fun `returns empty VEvent list when placeVisit is in ignoredVisitedPlaceIds`() = testScope.runTest {
        val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisit
        val ignoredVisitedPlaceIds = listOf("some-place-id")

        val vEvent = getPlaceVisitVEventUseCase(
            placeVisit = placeVisit,
            ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
            enablePlacesApiLookup = true,
            verboseConsoleLog = true,
        )

        assertEquals(emptyList(), vEvent)
    }

    @Test
    fun `emits UILogEntry through ignoredEvents when placeVisit is in ignoredVisitedPlaceIds`() = testScope.runTest {
        val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisit
        val ignoredVisitedPlaceIds = listOf("some-place-id")
        val ignoredEvents = mutableListOf<UILogEntry>()
        val jobs = launch {
            getPlaceVisitVEventUseCase.ignoredEvents.collect {
                ignoredEvents.add(it)
            }
        }
        val expectedIgnoreEvents = listOf(
            UILogEntry(
                emoji = "🚫",
                message = "11/11/2011 20:11:11: Place ID some-place-id",
            ),
        )

        getPlaceVisitVEventUseCase(
            placeVisit = placeVisit,
            ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
            enablePlacesApiLookup = true,
            verboseConsoleLog = true,
        )
        jobs.cancel()

        assertEquals(expectedIgnoreEvents, ignoredEvents)
    }

    // 🗂️ placeVisit.location.placeId is not in ignoredVisitedPlaceIds
    @Test
    fun `returns correct VEvent list when placeVisit not in ignoredVisitedPlaceIds with no child visits`() = testScope.runTest {
        val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisit
        val ignoredVisitedPlaceIds = listOf("some-other-place-id")
        fakeVEventFromPlaceVisitUseCase.useCaseResponse = GetPlaceVisitVEventUseCaseImplTestData.vEvent

        val vEvent = getPlaceVisitVEventUseCase(
            placeVisit = placeVisit,
            ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
            enablePlacesApiLookup = true,
            verboseConsoleLog = true,
        )

        assertEquals(listOf(GetPlaceVisitVEventUseCaseImplTestData.vEvent), vEvent)
    }

    @Test
    fun `emits UILogEntry through exportedEvents when placeVisit not in ignoredVisitedPlaceIds with no child visits`() = testScope.runTest {
        val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisit
        val ignoredVisitedPlaceIds = listOf("some-other-place-id")
        fakeVEventFromPlaceVisitUseCase.useCaseResponse = GetPlaceVisitVEventUseCaseImplTestData.vEvent
        val exportedEvents = mutableListOf<UILogEntry>()
        val jobs = launch {
            getPlaceVisitVEventUseCase.exportedEvents.collect {
                exportedEvents.add(it)
            }
        }
        val expectedExportedEvents = listOf(
            UILogEntry(emoji = "🗓", message = "11/11/2011 20:11:11: 🏧 some-place-name"),
        )

        getPlaceVisitVEventUseCase(
            placeVisit = placeVisit,
            ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
            enablePlacesApiLookup = true,
            verboseConsoleLog = true,
        )
        jobs.cancel()

        assertEquals(expectedExportedEvents, exportedEvents)
    }

    // 🗂️ placeVisit has child visits
    // ➡️ child visit is in ignoredVisitedPlaceIds
    @Test
    fun `skips processing child visit when placeVisit not in ignoredVisitedPlaceIds with child visits and child visit is in ignoredVisitedPlaceIds`() = testScope.runTest {
        val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisitWithOneChildVisit
        val ignoredVisitedPlaceIds = listOf("some-child-place-id")
        fakeVEventFromPlaceVisitUseCase.useCaseResponse = GetPlaceVisitVEventUseCaseImplTestData.vEvent
        // Still have to mock this response to make sure it has not been called
        fakeVEventFromChildVisitUseCase.useCaseResponse = GetPlaceVisitVEventUseCaseImplTestData.vEventSomeChildPlaceId

        val vEvent = getPlaceVisitVEventUseCase(
            placeVisit = placeVisit,
            ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
            enablePlacesApiLookup = true,
            verboseConsoleLog = true,
        )

        assertEquals(listOf(GetPlaceVisitVEventUseCaseImplTestData.vEvent), vEvent)
    }

    @Test
    fun `only skips ignored child visit when placeVisit not in ignoredVisitedPlaceIds with child visits, child visit is in ignoredVisitedPlaceIds and multiple child visits`() = testScope.runTest {
        val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisitWithTwoChildVisit
        val ignoredVisitedPlaceIds = listOf("some-another-child-place-id")
        val expectedVEvent = listOf(
            GetPlaceVisitVEventUseCaseImplTestData.vEvent,
            GetPlaceVisitVEventUseCaseImplTestData.vEventSomeChildPlaceId,
        )
        fakeVEventFromPlaceVisitUseCase.useCaseResponse = GetPlaceVisitVEventUseCaseImplTestData.vEvent
        // Still have to fake this response to make sure it has not been called
        fakeVEventFromChildVisitUseCase.useCaseResponse = GetPlaceVisitVEventUseCaseImplTestData.vEventSomeChildPlaceId

        val vEvent = getPlaceVisitVEventUseCase(
            placeVisit = placeVisit,
            ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
            enablePlacesApiLookup = true,
            verboseConsoleLog = true,
        )

        assertContentEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `emits UILogEntry through ignoredEvents when placeVisit not in ignoredVisitedPlaceIds with child visits and child visit is in ignoredVisitedPlaceIds`() = testScope.runTest {
        val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisitWithOneChildVisit
        val ignoredVisitedPlaceIds = listOf("some-child-place-id")
        val expectedIgnoredEvents = listOf(
            UILogEntry(
                emoji = "🚫",
                message = "03/01/2022 14:19:00: Place ID some-child-place-id",
            ),
        )
        val ignoredEvents = mutableListOf<UILogEntry>()
        val jobs = launch {
            getPlaceVisitVEventUseCase.ignoredEvents.collect {
                ignoredEvents.add(it)
            }
        }
        fakeVEventFromPlaceVisitUseCase.useCaseResponse = GetPlaceVisitVEventUseCaseImplTestData.vEvent

        getPlaceVisitVEventUseCase(
            placeVisit = placeVisit,
            ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
            enablePlacesApiLookup = true,
            verboseConsoleLog = true,
        )
        jobs.cancel()

        assertEquals(expectedIgnoredEvents, ignoredEvents)
    }

    // ➡️ child visit is not in ignoredVisitedPlaceIds
    @Test
    fun `includes child visit in VEvent list when placeVisit not in ignoredVisitedPlaceIds with child visits and child visit not in ignoredVisitedPlaceIds`() = testScope.runTest {
        val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisitWithOneChildVisit
        val ignoredVisitedPlaceIds = listOf("some-other-child-place-id")
        fakeVEventFromPlaceVisitUseCase.useCaseResponse = GetPlaceVisitVEventUseCaseImplTestData.vEvent
        fakeVEventFromChildVisitUseCase.useCaseResponse = GetPlaceVisitVEventUseCaseImplTestData.vEventSomeChildPlaceId
        val expectedVEvent = listOf(
            GetPlaceVisitVEventUseCaseImplTestData.vEvent,
            GetPlaceVisitVEventUseCaseImplTestData.vEventSomeChildPlaceId,
        )

        val vEvent = getPlaceVisitVEventUseCase(
            placeVisit = placeVisit,
            ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
            enablePlacesApiLookup = true,
            verboseConsoleLog = true,
        )

        assertContentEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `emits UILogEntry through exportedEvents when placeVisit not in ignoredVisitedPlaceIds with child visits and child visit not in ignoredVisitedPlaceIds`() = testScope.runTest {
        val placeVisit = GetPlaceVisitVEventUseCaseImplTestData.placeVisitWithOneChildVisit
        val ignoredVisitedPlaceIds = listOf("some-other-child-place-id")
        fakeVEventFromPlaceVisitUseCase.useCaseResponse = GetPlaceVisitVEventUseCaseImplTestData.vEvent
        fakeVEventFromChildVisitUseCase.useCaseResponse = GetPlaceVisitVEventUseCaseImplTestData.vEventSomeChildPlaceId

        val expectedExportedEvents = listOf(
            UILogEntry(emoji = "🗓", message = "11/11/2011 20:11:11: 🏧 some-place-name"),
            UILogEntry(emoji = "🗓", message = "03/01/2022 14:19:00: 📍 some-child-name"),
        )
        val exportedEvents = mutableListOf<UILogEntry>()
        val jobs = launch {
            getPlaceVisitVEventUseCase.exportedEvents.collect {
                exportedEvents.add(it)
            }
        }

        getPlaceVisitVEventUseCase(
            placeVisit = placeVisit,
            ignoredVisitedPlaceIds = ignoredVisitedPlaceIds,
            enablePlacesApiLookup = true,
            verboseConsoleLog = true,
        )
        jobs.cancel()

        assertContentEquals(expectedExportedEvents, exportedEvents)
    }
}
