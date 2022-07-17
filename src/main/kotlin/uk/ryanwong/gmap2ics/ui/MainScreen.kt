package uk.ryanwong.gmap2ics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import uk.ryanwong.gmap2ics.ui.models.MainScreenViewModel

@Composable
fun mainScreen(
    onCloseRequest: () -> Unit,
    mainScreenViewModel: MainScreenViewModel
) {
    Window(
        onCloseRequest = onCloseRequest,
        title = "GMap2iCal - Google Maps to iCal",
        state = rememberWindowState(width = 640.dp, height = 480.dp)
    ) {
        val statusMessage = mainScreenViewModel.statusMessage.collectAsState()

        MaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Google Maps to iCal converter",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                )

                Text(
                    text = statusMessage.value,
                    modifier = Modifier.fillMaxWidth()
                        .padding(all = 16.dp)
                        .background(color = Color.White)
                        .height(80.dp)
                        .scrollable(
                            enabled = true,
                            orientation = Orientation.Vertical,
                            state = rememberScrollState()
                        )
                )

                Row(
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Button(
                        onClick = { mainScreenViewModel.startConvertion() }
                    ) {
                        Text(text = "Convert")
                    }
                    Button(
                        onClick = onCloseRequest
                    ) {
                        Text(text = "Exit App")
                    }
                }
            }
        }
    }
}