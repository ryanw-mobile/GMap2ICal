/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.ResourceBundle

@Composable
fun SettingsSection(
    jsonPath: String,
    iCalPath: String,
    exportPlaceVisit: Boolean,
    exportActivitySegment: Boolean,
    enablePlacesApiLookup: Boolean,
    verboseLogs: Boolean,
    onExportPlaceVisitChanged: (Boolean) -> Unit,
    onExportActivitySegmentChanged: (Boolean) -> Unit,
    onEnablePlaceApiLookupChanged: (Boolean) -> Unit,
    onVerboseLogsChanged: (Boolean) -> Unit,
    onChangeJsonPath: () -> Unit,
    onChangeICalPath: () -> Unit,
    modifier: Modifier = Modifier,
    resourceBundle: ResourceBundle
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            PathPickerItem(
                title = resourceBundle.getString("json.path"),
                currentPath = jsonPath,
                onClick = onChangeJsonPath,
                resourceBundle = resourceBundle,
                modifier = Modifier
                    .weight(0.5f, fill = true)
                    .height(intrinsicSize = IntrinsicSize.Max)
            )

            PathPickerItem(
                title = resourceBundle.getString("ical.path"),
                currentPath = iCalPath,
                onClick = onChangeICalPath,
                resourceBundle = resourceBundle,
                modifier = Modifier
                    .weight(0.5f, fill = true)
                    .height(intrinsicSize = IntrinsicSize.Max)
            )
        }

        Row {
            Column(modifier = Modifier.weight(0.5f, fill = true)) {
                CheckBoxItem(
                    text = resourceBundle.getString("export.places.visited"),
                    checked = exportPlaceVisit,
                    onCheckedChange = onExportPlaceVisitChanged
                )

                CheckBoxItem(
                    text = resourceBundle.getString("export.activity.segments"),
                    checked = exportActivitySegment,
                    onCheckedChange = onExportActivitySegmentChanged
                )
            }

            Column(modifier = Modifier.weight(0.5f, fill = true)) {
                CheckBoxItem(
                    text = resourceBundle.getString("enable.places.api.lookup"),
                    checked = enablePlacesApiLookup,
                    onCheckedChange = onEnablePlaceApiLookupChanged
                )

                CheckBoxItem(
                    text = resourceBundle.getString("verbose.log.mode"),
                    checked = verboseLogs,
                    onCheckedChange = onVerboseLogsChanged
                )
            }
        }
    }
}

@Preview
@Composable
fun ExportOptionsGroup() {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .border(width = 1.dp, color = Color.LightGray)
            .wrapContentSize()
    ) {
        Text(
            text = "Export Options",
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp)
                .background(color = Color.DarkGray)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        Row() {
            CheckBoxItem(
                text = "123",
                checked = true,
                onCheckedChange = { }
            )

            CheckBoxItem(
                text = "123",
                checked = true,
                onCheckedChange = { }
            )
        }
    }
}