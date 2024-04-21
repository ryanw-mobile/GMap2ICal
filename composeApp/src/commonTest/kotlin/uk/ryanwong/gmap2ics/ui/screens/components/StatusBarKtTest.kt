/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
internal class StatusBarKtTest {
//
//    /***
//     * 1. Currently, Compose Desktop test runs on JUnit4 only.
//     * 2. Can't sort out how to apply @get:Rule on Kotest so back to JUnit4.
//     * 3. Compose Desktop assertion library is not completed yet - limited what can be tested.
//     */
//    @get:Rule
//    val compose = createComposeRule()
//
//    @Test
//    fun `Should not display LinearProgressIndicator if progress is null`() = runTest {
//        with(compose) {
//            val statusMessage = "some-status-message"
//            val progress = null
//
//            setContent {
//                GregoryGreenTheme {
//                    StatusBar(
//                        statusMessage = statusMessage,
//                        progress = progress,
//                    )
//                }
//            }
//            awaitIdle()
//
//            onNodeWithContentDescription(label = getString(Res.string.content_description_linear_progress_indicator))
//                .assertDoesNotExist()
//        }
//    }
//
//    @Test
//    fun `Should display LinearProgressIndicator if progress is 0`() = runTest {
//        with(compose) {
//            val statusMessage = "some-status-message"
//            val progress = 0f
//
//            setContent {
//                GregoryGreenTheme {
//                    StatusBar(
//                        statusMessage = statusMessage,
//                        progress = progress,
//                    )
//                }
//            }
//            awaitIdle()
//
//            onNodeWithContentDescription(label = getString(Res.string.content_description_linear_progress_indicator)).assertExists()
//        }
//    }
//
//    @Test
//    fun `Should display LinearProgressIndicator if progress is greater than 0`() = runTest {
//        with(compose) {
//            val statusMessage = "some-status-message"
//            val progress = 8.25f
//
//            setContent {
//                GregoryGreenTheme {
//                    StatusBar(
//                        statusMessage = statusMessage,
//                        progress = progress,
//                    )
//                }
//            }
//            awaitIdle()
//
//            onNodeWithContentDescription(label = getString(Res.string.content_description_linear_progress_indicator)).assertExists()
//        }
//    }
}
