/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import gmap2ical.composeapp.generated.resources.Res
import gmap2ical.composeapp.generated.resources.content_description_linear_progress_indicator
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.ryanwong.gmap2ics.ui.theme.gregorygreen.GregoryGreenTheme

@OptIn(ExperimentalResourceApi::class)
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
            with(compose) {
                val statusMessage = "some-status-message"
                val progress = null

                setContent {
                    GregoryGreenTheme {
                        StatusBar(
                            statusMessage = statusMessage,
                            progress = progress,
                        )
                    }
                }
                awaitIdle()

                onNodeWithContentDescription(label = getString(Res.string.content_description_linear_progress_indicator))
                    .assertDoesNotExist()
            }
        }
    }

    @Test
    fun `Should display LinearProgressIndicator if progress is 0`() {
        scope.runTest {
            with(compose) {
                val statusMessage = "some-status-message"
                val progress = 0f

                setContent {
                    GregoryGreenTheme {
                        StatusBar(
                            statusMessage = statusMessage,
                            progress = progress,
                        )
                    }
                }
                awaitIdle()

                onNodeWithContentDescription(label = getString(Res.string.content_description_linear_progress_indicator)).assertExists()
            }
        }
    }

    @Test
    fun `Should display LinearProgressIndicator if progress is greater than 0`() {
        scope.runTest {
            with(compose) {
                val statusMessage = "some-status-message"
                val progress = 8.25f

                setContent {
                    GregoryGreenTheme {
                        StatusBar(
                            statusMessage = statusMessage,
                            progress = progress,
                        )
                    }
                }
                awaitIdle()

                onNodeWithContentDescription(label = getString(Res.string.content_description_linear_progress_indicator)).assertExists()
            }
        }
    }
}
