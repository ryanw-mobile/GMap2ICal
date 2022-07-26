package uk.ryanwong.gmap2ics.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ryanwong.gmap2ics.ui.components.CheckBoxItem
import uk.ryanwong.gmap2ics.ui.models.MainScreenViewModel
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

    Window(
        onCloseRequest = onCloseRequest,
        title = resourceBundle.getString("gmap2ical.google.maps.to.ical"),
        state = rememberWindowState(width = 800.dp, height = 560.dp)
    ) {
        val uiState by mainScreenViewModel.mainScreenUIState.collectAsState()
        val statusMessage by mainScreenViewModel.statusMessage.collectAsState()
        val jsonPath by mainScreenViewModel.jsonPath.collectAsState()
        val iCalPath by mainScreenViewModel.iCalPath.collectAsState()
        val exportPlaceVisit by mainScreenViewModel.exportPlaceVisit.collectAsState()
        val exportActivitySegment by mainScreenViewModel.exportActivitySegment.collectAsState()
        val enablePlacesApiLookup by mainScreenViewModel.enablePlacesApiLookup.collectAsState()

        LaunchedEffect(uiState) {
            when (uiState) {
                is MainScreenUIState.ShowChangeJsonPathDialog -> {
                    val newPath = chooseDirectorySwing(currentDirectoryPath = jsonPath)
                    mainScreenViewModel.updateJsonPath(path = newPath)
                }
                is MainScreenUIState.ShowChangeICalPathDialog -> {
                    val newPath = chooseDirectorySwing(currentDirectoryPath = iCalPath)
                    mainScreenViewModel.updateICalPath(path = newPath)
                }
                is MainScreenUIState.Error -> {
                    // TODO: Show error message
                }
                else -> {}
            }
        }

        MaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = resourceBundle.getString("google.maps.to.ical.converter"),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    SettingsColumn(
                        jsonPath = jsonPath,
                        iCalPath = iCalPath,
                        exportPlaceVisit = exportPlaceVisit,
                        exportActivitySegment = exportActivitySegment,
                        enablePlacesApiLookup = enablePlacesApiLookup,
                        onExportPlaceVisitChanged = { enabled -> mainScreenViewModel.setExportPlaceVisit(enabled) },
                        onExportActivitySegmentChanged = { enabled ->
                            mainScreenViewModel.setExportActivitySegment(enabled)
                        },
                        onEnabldPlaceApiLookupChanged = { enabled ->
                            mainScreenViewModel.setEnablePlacesApiLookup(enabled)
                        },
                        onChangeJsonPath = { mainScreenViewModel.onChangeJsonPath() },
                        onChangeICalPath = { mainScreenViewModel.onChangeICalPath() }
                    )

                    StatusColumn(
                        statusMessage = statusMessage
                    )
                }

                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Button(
                        onClick = { mainScreenViewModel.startConvertion() }
                    ) {
                        Text(text = resourceBundle.getString("convert"))
                    }
                    Button(
                        onClick = onCloseRequest
                    ) {
                        Text(text = resourceBundle.getString("exit.app"))
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsColumn(
    jsonPath: String,
    iCalPath: String,
    exportPlaceVisit: Boolean,
    exportActivitySegment: Boolean,
    enablePlacesApiLookup: Boolean,
    onExportPlaceVisitChanged: (Boolean) -> Unit,
    onExportActivitySegmentChanged: (Boolean) -> Unit,
    onEnabldPlaceApiLookupChanged: (Boolean) -> Unit,
    onChangeJsonPath: () -> Unit,
    onChangeICalPath: () -> Unit
) {
    val resourceBundle = getBundle("resources", Locale.ENGLISH)

    val spacerModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .height(height = 1.dp)
        .background(color = Color.Gray)

    Column(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(1f)
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 8.dp)
            ) {
                Image(
                    painter = painterResource(resourcePath = "/drawables/folder.png"),
                    contentDescription = resourceBundle.getString("change.folder"),
                    modifier = Modifier
                        .clickable(onClick = onChangeJsonPath)
                        .padding(all = 4.dp)
                        .size(size = 24.dp)
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = resourceBundle.getString("json.path"),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Text(
                    text = jsonPath,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 8.dp)
            ) {
                Image(
                    painter = painterResource(resourcePath = "/drawables/folder.png"),
                    contentDescription = resourceBundle.getString("change.folder"),
                    modifier = Modifier
                        .clickable(onClick = onChangeICalPath)
                        .padding(all = 4.dp)
                        .size(size = 24.dp)
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = resourceBundle.getString("ical.path"),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Text(
                    text = iCalPath,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }
        }

        Spacer(modifier = spacerModifier)

        CheckBoxItem(
            text = resourceBundle.getString("export.places.visited"),
            checked = exportPlaceVisit,
            onCheckedChange = onExportPlaceVisitChanged
        )

        CheckBoxItem(
            text = resourceBundle.getString("export.activity.segments"),
            checked = enablePlacesApiLookup,
            onCheckedChange = onEnabldPlaceApiLookupChanged
        )

        Spacer(modifier = spacerModifier)

        CheckBoxItem(
            text = resourceBundle.getString("enable.places.api.lookup"),
            checked = exportActivitySegment,
            onCheckedChange = onExportActivitySegmentChanged
        )
    }
}

@Composable
private fun StatusColumn(
    statusMessage: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = statusMessage,
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .padding(horizontal = 32.dp)
                .background(color = Color.White)
                .scrollable(
                    enabled = true,
                    orientation = Orientation.Vertical,
                    state = rememberScrollState()
                )
        )
    }
}

private suspend fun chooseDirectorySwing(currentDirectoryPath: String): String? = withContext(Dispatchers.IO) {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    val chooser = JFileChooser(currentDirectoryPath).apply {
        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        isVisible = true
    }

    return@withContext when (val code = chooser.showOpenDialog(null)) {
        JFileChooser.APPROVE_OPTION -> chooser.selectedFile.absolutePath
        JFileChooser.CANCEL_OPTION -> null
        JFileChooser.ERROR_OPTION -> {
            //  error("An error occurred while executing JFileChooser::showOpenDialog")
            null
        }
        else -> {
            //error("Unknown return code '${code}' from JFileChooser::showOpenDialog")
            null
        }
    }
}