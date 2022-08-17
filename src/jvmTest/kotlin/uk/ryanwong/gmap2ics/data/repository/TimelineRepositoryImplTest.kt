/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import uk.ryanwong.gmap2ics.app.utils.timezonemap.MockTimeZoneMap
import uk.ryanwong.gmap2ics.data.repository.TimelineRepositoryImplTestData.mockJsonString
import uk.ryanwong.gmap2ics.data.repository.TimelineRepositoryImplTestData.mockTimeLineFromJsonString
import uk.ryanwong.gmap2ics.data.source.local.MockLocalDataSource

internal class TimelineRepositoryImplTest : FreeSpec() {

    private lateinit var timelineRepository: TimelineRepositoryImpl
    private lateinit var localDataSource: MockLocalDataSource
    private lateinit var mockTimeZoneMap: MockTimeZoneMap

    private fun setupRepository() {
        localDataSource = MockLocalDataSource()
        mockTimeZoneMap = MockTimeZoneMap()

        timelineRepository = TimelineRepositoryImpl(
            localDataSource = localDataSource,
            timeZoneMap = mockTimeZoneMap,
            dispatcher = UnconfinedTestDispatcher()
        )
    }

    init {
        "getTimeLine" - {
            "Should return correct TimeLine object if localDataSource returns valid JSON String" {
                // 🔴 Given
                setupRepository()
                localDataSource.getJsonStringResponse = mockJsonString
                mockTimeZoneMap.mockZoneId = "Asia/Tokyo"

                // 🟡 When
                val timeLine = timelineRepository.getTimeLine(filePath = "/some-absolute-path/")

                // 🟢 Then
                timeLine.isSuccess shouldBe true
                timeLine.getOrNull() shouldBe mockTimeLineFromJsonString
            }
        }
    }
}