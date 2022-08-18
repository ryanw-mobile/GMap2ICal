/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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