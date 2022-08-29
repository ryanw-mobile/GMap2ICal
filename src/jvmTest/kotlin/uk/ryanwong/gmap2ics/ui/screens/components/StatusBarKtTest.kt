/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.ryanwong.gmap2ics.ui.GregoryGreenTheme
import uk.ryanwong.gmap2ics.ui.screens.ContentDescriptions

@OptIn(ExperimentalCoroutinesApi::class)
internal class StatusBarKtTest {

    /***
     * 1. Currently, Compose Desktop test runs on JUnit4 only.
     * 2. Can't sort out how to apply @get:Rule on Kotest so back to JUnit4.
     * 3. Compose Desktop assertion library is not completed yet - limited what can be tested.
     */
    private lateinit var scope: TestScope

    @get:Rule
    val compose = createComposeRule()

    @Before
    fun setupDispatcher() {
        val dispatcher = StandardTestDispatcher()
        scope = TestScope(dispatcher)
    }

    @Test
    fun `Should not display LinearProgressIndicator if progress is null`() {
        scope.runTest {
            // 🔴 Given
            val statusMessage = "some-status-message"
            val progress = null

            // 🟡 When
            compose.setContent {
                GregoryGreenTheme {
                    StatusBar(
                        statusMessage = statusMessage,
                        progress = progress
                    )
                }
            }
            compose.awaitIdle()

            // 🟢 Then
            compose.onNodeWithContentDescription(label = ContentDescriptions.LINEAR_PROGRESS_INDICATOR).assertDoesNotExist()
        }
    }

    @Test
    fun `Should display LinearProgressIndicator if progress is 0`() {
        scope.runTest {
            // 🔴 Given
            val statusMessage = "some-status-message"
            val progress = 0f

            // 🟡 When
            compose.setContent {
                GregoryGreenTheme {
                    StatusBar(
                        statusMessage = statusMessage,
                        progress = progress
                    )
                }
            }
            compose.awaitIdle()

            // 🟢 Then
            compose.onNodeWithContentDescription(label = ContentDescriptions.LINEAR_PROGRESS_INDICATOR).assertExists()
        }
    }

    @Test
    fun `Should display LinearProgressIndicator if progress is greater than 0`() {
        scope.runTest {
            // 🔴 Given
            val statusMessage = "some-status-message"
            val progress = 8.25f

            // 🟡 When
            compose.setContent {
                GregoryGreenTheme {
                    StatusBar(
                        statusMessage = statusMessage,
                        progress = progress
                    )
                }
            }
            compose.awaitIdle()

            // 🟢 Then
            compose.onNodeWithContentDescription(label = ContentDescriptions.LINEAR_PROGRESS_INDICATOR).assertExists()
        }
    }
}
