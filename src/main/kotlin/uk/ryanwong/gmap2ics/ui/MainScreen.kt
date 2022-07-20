package uk.ryanwong.gmap2ics.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import uk.ryanwong.gmap2ics.ui.models.MainScreenViewModel
import java.util.Locale
import java.util.ResourceBundle.getBundle

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
        val statusMessage by mainScreenViewModel.statusMessage.collectAsState()
        val jsonPath by mainScreenViewModel.jsonPath.collectAsState()
        val iCalPath by mainScreenViewModel.iCalPath.collectAsState()
        val exportPlaceVisit by mainScreenViewModel.exportPlaceVisit.collectAsState()
        val exportActivitySegment by mainScreenViewModel.exportActivitySegment.collectAsState()
        val enablePlacesApiLookup by mainScreenViewModel.enablePlacesApiLookup.collectAsState()

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
    onEnabldPlaceApiLookupChanged: (Boolean) -> Unit
) {
    val resourceBundle = getBundle("resources", Locale.ENGLISH)

    Column(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(horizontal = 16.dp)
    ) {
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

        Text(
            text = resourceBundle.getString("ical.path"),
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 16.dp)
        )

        Text(
            text = iCalPath,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { role = Role.Checkbox }
        ) {
            Checkbox(
                checked = exportPlaceVisit,
                onCheckedChange = onExportPlaceVisitChanged
            )
            Text(
                text = resourceBundle.getString("export.places.visited"),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { role = Role.Checkbox }
        ) {
            Checkbox(
                checked = exportActivitySegment,
                onCheckedChange = onExportActivitySegmentChanged
            )
            Text(
                text = resourceBundle.getString("enable.places.api.lookup"),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { role = Role.Checkbox }
        ) {
            Checkbox(
                checked = enablePlacesApiLookup,
                onCheckedChange = onEnabldPlaceApiLookupChanged
            )
            Text(
                text = resourceBundle.getString("export.activity.segments"),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
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