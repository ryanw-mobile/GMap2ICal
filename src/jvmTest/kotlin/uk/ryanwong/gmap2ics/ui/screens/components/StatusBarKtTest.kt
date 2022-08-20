/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class StatusBarKtTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `should_not_display_LinearProgressIndicator_if_progress_is_null`() {
        runBlocking(Dispatchers.Unconfined) {
            // 🔴 Given
            val statusMessage = "some-status-message"
            val progress = null

            // 🟡 When
            compose.setContent {
                StatusBar(
                    statusMessage = statusMessage,
                    progress = progress
                )
            }
            compose.awaitIdle()

            // 🟢 Then
            compose.onNodeWithTag(testTag = "LinearProgressIndicator").assertDoesNotExist()
        }
    }

    @Test
    fun `should_display_LinearProgressIndicator_if_progress_is_0`() {
        runBlocking(Dispatchers.Unconfined) {
            // 🔴 Given
            val statusMessage = "some-status-message"
            val progress = 0f

            // 🟡 When
            compose.setContent {
                StatusBar(
                    statusMessage = statusMessage,
                    progress = progress
                )
            }
            compose.awaitIdle()

            // 🟢 Then
            compose.onNodeWithTag(testTag = "LinearProgressIndicator").assertExists()
        }
    }

    @Test
    fun `should_display_LinearProgressIndicator_if_progress_greater_than_0`() {
        runBlocking(Dispatchers.Unconfined) {
            // 🔴 Given
            val statusMessage = "some-status-message"
            val progress = 8.25f

            // 🟡 When
            compose.setContent {
                StatusBar(
                    statusMessage = statusMessage,
                    progress = progress
                )
            }
            compose.awaitIdle()

            // 🟢 Then
            compose.onNodeWithTag(testTag = "LinearProgressIndicator").assertExists()
        }
    }
}
