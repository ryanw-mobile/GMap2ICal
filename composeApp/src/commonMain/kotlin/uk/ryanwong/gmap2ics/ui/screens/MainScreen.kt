/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */
@file:OptIn(ExperimentalResourceApi::class)

package uk.ryanwong.gmap2ics.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import gmap2ical.composeapp.generated.resources.Res
import gmap2ical.composeapp.generated.resources.ical_output_location
import gmap2ical.composeapp.generated.resources.json_source_location
import gmap2ical.composeapp.generated.resources.screen_title_main
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import uk.ryanwong.gmap2ics.ui.screens.components.CancelActionButton
import uk.ryanwong.gmap2ics.ui.screens.components.ErrorAlertDialog
import uk.ryanwong.gmap2ics.ui.screens.components.ExportActionButton
import uk.ryanwong.gmap2ics.ui.screens.components.ExportOptionsGroup
import uk.ryanwong.gmap2ics.ui.screens.components.ExtraOptionsGroup
import uk.ryanwong.gmap2ics.ui.screens.components.LogWindow
import uk.ryanwong.gmap2ics.ui.screens.components.LogWindowTab
import uk.ryanwong.gmap2ics.ui.screens.components.LogWindowTabRow
import uk.ryanwong.gmap2ics.ui.screens.components.LogWindowUIState
import uk.ryanwong.gmap2ics.ui.screens.components.SettingsPanel
import uk.ryanwong.gmap2ics.ui.screens.components.StatusBar
import uk.ryanwong.gmap2ics.ui.viewmodels.JFileChooserResult
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModel
import java.awt.Dimension
import javax.swing.JFileChooser
import javax.swing.UIManager

@Composable
fun mainScreen(
    onCloseRequest: () -> Unit,
    mainScreenViewModel: MainScreenViewModel,
) {
    val windowState = rememberWindowState(width = 800.dp, height = 560.dp)

    Window(
        onCloseRequest = onCloseRequest,
        title = stringResource(Res.string.screen_title_main),
        state = windowState,
    ) {
        val coroutineScope = rememberCoroutineScope()
        val uiState by mainScreenViewModel.mainScreenUIState.collectAsState()
        val exportedLogs by mainScreenViewModel.exportedLogs.collectAsState()
        val ignoredLogs by mainScreenViewModel.ignoredLogs.collectAsState()
        val statusMessage by mainScreenViewModel.statusMessage.collectAsState()
        val jsonPath by mainScreenViewModel.jsonPath.collectAsState()
        val iCalPath by mainScreenViewModel.iCalPath.collectAsState()
        val exportPlaceVisit by mainScreenViewModel.exportPlaceVisit.collectAsState()
        val exportActivitySegment by mainScreenViewModel.exportActivitySegment.collectAsState()
        val enablePlacesApiLookup by mainScreenViewModel.enablePlacesApiLookup.collectAsState()
        val verboseLogs by mainScreenViewModel.verboseLogs.collectAsState()
        val progress: MutableState<Float?> = remember { mutableStateOf(null) }
        val selectLogWindowTab = remember { mutableStateOf(LogWindowTab.EXPORTED) }

        window.minimumSize = Dimension(640, 480)

        when (uiState) {
            is MainScreenUIState.ChangeJsonPath -> {
                coroutineScope.launch {
                    val jFileChooserResult = chooseDirectorySwing(
                        dialogTitle = getString(Res.string.json_source_location),
                        currentDirectoryPath = jsonPath,
                    )
                    mainScreenViewModel.updateJsonPath(jFileChooserResult = jFileChooserResult)
                }
            }

            is MainScreenUIState.ChangeICalPath -> {
                coroutineScope.launch {
                    val jFileChooserResult = chooseDirectorySwing(
                        dialogTitle = getString(Res.string.ical_output_location),
                        currentDirectoryPath = iCalPath,
                    )
                    mainScreenViewModel.updateICalPath(jFileChooserResult = jFileChooserResult)
                }
            }

            is MainScreenUIState.Error -> {
                ErrorAlertDialog(
                    text = (uiState as MainScreenUIState.Error).errMsg,
                    onDismissRequest = { mainScreenViewModel.notifyErrorMessageDisplayed() },
                )
            }

            is MainScreenUIState.Ready -> {
                progress.value = null
            }

            is MainScreenUIState.Processing -> {
                progress.value = (uiState as MainScreenUIState.Processing).progress
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
                .background(color = MaterialTheme.colors.background),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(bottom = 16.dp)
                    .wrapContentHeight(),
            ) {
                SettingsPanel(
                    jsonPath = jsonPath,
                    iCalPath = iCalPath,
                    onChangeJsonPath = { mainScreenViewModel.onChangeJsonPath() },
                    onChangeICalPath = { mainScreenViewModel.onChangeICalPath() },
                    modifier = Modifier.weight(weight = 1.0f, fill = true),
                    exportOptionsGroup = {
                        ExportOptionsGroup(
                            exportPlaceVisit = exportPlaceVisit,
                            exportActivitySegment = exportActivitySegment,
                            onExportActivitySegmentClicked = { enabled ->
                                mainScreenViewModel.setExportActivitySegment(enabled)
                            },
                            onExportPlaceVisitClicked = { enabled -> mainScreenViewModel.setExportPlaceVisit(enabled) },
                            modifier = Modifier.wrapContentSize(),
                        )
                    },
                    extraOptionsGroup = {
                        ExtraOptionsGroup(
                            isPlaceApiEnabled = enablePlacesApiLookup,
                            isVerboseLogEnabled = verboseLogs,
                            onEnablePlaceApiLookupClicked = { enabled ->
                                mainScreenViewModel.setEnablePlacesApiLookup(enabled)
                            },
                            onVerboseLogClicked = { enabled -> mainScreenViewModel.setVerboseLogs(enabled) },
                            modifier = Modifier.wrapContentSize(),
                        )
                    },
                )

                if (uiState is MainScreenUIState.Processing) {
                    CancelActionButton(
                        onButtonClicked = { mainScreenViewModel.cancelExport() },
                        modifier = Modifier.padding(end = 16.dp),
                    )
                } else {
                    val shouldExportButtonEnabled = (uiState == MainScreenUIState.Ready) &&
                        (exportActivitySegment || exportPlaceVisit)
                    ExportActionButton(
                        enabled = shouldExportButtonEnabled,
                        onButtonClicked = { mainScreenViewModel.startExport() },
                        modifier = Modifier.padding(end = 16.dp),
                    )
                }
            }

            Spacer(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(height = 1.dp)
                    .background(color = Color.LightGray),
            )

            LogWindowTabRow(
                logWindowUIState = LogWindowUIState(
                    exportedCount = exportedLogs.size,
                    ignoredCount = ignoredLogs.size,
                    selectedTab = selectLogWindowTab.value,
                    onTabSelected = { selectedTab -> selectLogWindowTab.value = selectedTab },
                ),
            )

            val exportedLazyListState = rememberLazyListState()
            val exportedScrollState = rememberScrollState()
            val ignoredLazyListState = rememberLazyListState()
            val ignoredScrollState = rememberScrollState()
            when (selectLogWindowTab.value) {
                LogWindowTab.EXPORTED ->
                    LogWindow(
                        logEntries = exportedLogs,
                        lazyListState = exportedLazyListState,
                        scrollState = exportedScrollState,
                        modifier = Modifier
                            .weight(weight = 1.0f, fill = true),
                    )

                LogWindowTab.IGNORED ->
                    LogWindow(
                        logEntries = ignoredLogs,
                        lazyListState = ignoredLazyListState,
                        scrollState = ignoredScrollState,
                        modifier = Modifier
                            .weight(weight = 1.0f, fill = true),
                    )
            }

            StatusBar(
                statusMessage = statusMessage,
                progress = progress.value,
            )
        }
    }
}

private suspend fun chooseDirectorySwing(dialogTitle: String, currentDirectoryPath: String): JFileChooserResult {
    return withContext(Dispatchers.Main) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

        val chooser = JFileChooser(currentDirectoryPath).apply {
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            isVisible = true
            this.dialogTitle = dialogTitle
        }

        return@withContext when (val returnCode = chooser.showOpenDialog(null)) {
            JFileChooser.APPROVE_OPTION -> JFileChooserResult.AbsolutePath(absolutePath = chooser.selectedFile.absolutePath)
            JFileChooser.CANCEL_OPTION -> JFileChooserResult.Cancelled
            else -> JFileChooserResult.Error(errorCode = returnCode)
        }
    }
}
