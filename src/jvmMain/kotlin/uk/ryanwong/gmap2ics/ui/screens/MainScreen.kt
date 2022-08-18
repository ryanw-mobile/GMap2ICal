/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.app.models.JFileChooserResult
import uk.ryanwong.gmap2ics.ui.screens.components.ErrorAlertDialog
import uk.ryanwong.gmap2ics.ui.screens.components.LogWindow
import uk.ryanwong.gmap2ics.ui.screens.components.SettingsSection
import uk.ryanwong.gmap2ics.ui.screens.components.StatusBar
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModel
import java.util.Locale
import java.util.ResourceBundle.getBundle
import javax.swing.JFileChooser
import javax.swing.UIManager

@Composable
fun mainScreen(
    onCloseRequest: () -> Unit,
    mainScreenViewModel: MainScreenViewModel
) {
    val resourceBundle = getBundle("resources", Locale.ENGLISH)
    val windowState = rememberWindowState(width = 800.dp, height = 560.dp)

    Window(
        onCloseRequest = onCloseRequest,
        title = resourceBundle.getString("gmap2ical.google.maps.to.ical"),
        state = windowState
    ) {
        val coroutineScope = rememberCoroutineScope()
        val uiState by mainScreenViewModel.mainScreenUIState.collectAsState()
        val logEntries by mainScreenViewModel.logEntries.collectAsState()
        val statusMessage by mainScreenViewModel.statusMessage.collectAsState()
        val jsonPath by mainScreenViewModel.jsonPath.collectAsState()
        val iCalPath by mainScreenViewModel.iCalPath.collectAsState()
        val exportPlaceVisit by mainScreenViewModel.exportPlaceVisit.collectAsState()
        val exportActivitySegment by mainScreenViewModel.exportActivitySegment.collectAsState()
        val enablePlacesApiLookup by mainScreenViewModel.enablePlacesApiLookup.collectAsState()
        val verboseLogs by mainScreenViewModel.verboseLogs.collectAsState()
        var progress: Float? = remember { null }

        when (uiState) {
            is MainScreenUIState.ChangeJsonPath -> {
                coroutineScope.launch {
                    val jFileChooserResult = chooseDirectorySwing(
                        dialogTitle = resourceBundle.getString("json.source.location"),
                        currentDirectoryPath = jsonPath
                    )
                    mainScreenViewModel.updateJsonPath(jFileChooserResult = jFileChooserResult)
                }
            }

            is MainScreenUIState.ChangeICalPath -> {
                coroutineScope.launch {
                    val jFileChooserResult = chooseDirectorySwing(
                        dialogTitle = resourceBundle.getString("ical.output.location"),
                        currentDirectoryPath = iCalPath
                    )
                    mainScreenViewModel.updateICalPath(jFileChooserResult = jFileChooserResult)
                }
            }

            is MainScreenUIState.Error -> {
                ErrorAlertDialog(
                    text = (uiState as MainScreenUIState.Error).errMsg,
                    onDismissRequest = { mainScreenViewModel.notifyErrorMessageDisplayed() }
                )
            }

            is MainScreenUIState.Ready -> {
                progress = null
            }

            is MainScreenUIState.Processing -> {
                progress = (uiState as MainScreenUIState.Processing).progress
            }
        }

        MaterialTheme {
            Column(modifier = Modifier.fillMaxSize()) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 8.dp)
                        .padding(bottom = 16.dp)
                        .wrapContentHeight()
                ) {
                    SettingsSection(
                        jsonPath = jsonPath,
                        iCalPath = iCalPath,
                        exportPlaceVisit = exportPlaceVisit,
                        exportActivitySegment = exportActivitySegment,
                        enablePlacesApiLookup = enablePlacesApiLookup,
                        verboseLogs = verboseLogs,
                        onExportPlaceVisitChanged = { enabled -> mainScreenViewModel.setExportPlaceVisit(enabled) },
                        onExportActivitySegmentChanged = { enabled ->
                            mainScreenViewModel.setExportActivitySegment(enabled)
                        },
                        onEnablePlaceApiLookupChanged = { enabled ->
                            mainScreenViewModel.setEnablePlacesApiLookup(enabled)
                        },
                        onVerboseLogsChanged = { enabled -> mainScreenViewModel.setVerboseLogs(enabled) },
                        onChangeJsonPath = { mainScreenViewModel.onChangeJsonPath() },
                        onChangeICalPath = { mainScreenViewModel.onChangeICalPath() },
                        modifier = Modifier.weight(weight = 1.0f, fill = true),
                        resourceBundle = resourceBundle
                    )

                    Button(
                        enabled = (uiState == MainScreenUIState.Ready),
                        shape = CircleShape,
                        onClick = { mainScreenViewModel.startExport() },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = resourceBundle.getString("convert"),
                            modifier = Modifier.wrapContentSize()
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(height = 1.dp)
                        .background(color = Color.LightGray)
                )
                
                ButtonRow()
                LogWindow(
                    logEntries = logEntries,
                    modifier = Modifier
                        .weight(weight = 1.0f, fill = true)
                )

                StatusBar(
                    statusMessage = statusMessage,
                    progress = progress
                )
            }
        }
    }
}

@Preview
@Composable
fun ButtonRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        TextButton(
            enabled = true,
            border = BorderStroke(width = 1.dp, color = Color.Gray),
            modifier = Modifier.wrapContentSize().padding(end = 8.dp),
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(all = 0.dp),
            shape = RectangleShape
        ) {
            Text(
                text = "Exported (20)",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.wrapContentSize().padding(horizontal = 8.dp)
            )
        }
        TextButton(
            enabled = true,
            border = BorderStroke(width = 1.dp, color = Color.Gray),
            modifier = Modifier.wrapContentSize().padding(end = 8.dp),
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(all = 0.dp),
            shape = RectangleShape
        ) {
            Text(
                text = "Ignored (10)",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.wrapContentSize().padding(horizontal = 8.dp)
            )
        }
        TextButton(
            enabled = true,
            border = BorderStroke(width = 1.dp, color = Color.Gray),
            modifier = Modifier.wrapContentSize(),
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(all = 0.dp),
            shape = RectangleShape
        ) {
            Text(
                text = "Errors",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.wrapContentSize()
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

private suspend fun chooseDirectorySwing(dialogTitle: String, currentDirectoryPath: String): JFileChooserResult {
    return withContext(Dispatchers.Default) {
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