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

    /***
     * 1. Currently, Compose Desktop test runs on JUnit4 only.
     * 2. Can't sort out how to apply @get:Rule on Kotest so back to JUnit4.
     * 3. Compose Desktop assertion library is not completed yet - limited what can be tested.
     */

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `Should not display LinearProgressIndicator if progress is null`() {
        runBlocking(Dispatchers.Main) {
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
    fun `Should display LinearProgressIndicator if progress is 0`() {
        runBlocking(Dispatchers.Main) {
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
    fun `Should display LinearProgressIndicator if progress is greater than 0`() {
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
