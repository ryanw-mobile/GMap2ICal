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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uk.ryanwong.gmap2ics.ui.theme.JapanesePurpleTheme
import java.text.MessageFormat
import java.util.Locale
import java.util.ResourceBundle
import java.util.ResourceBundle.getBundle

@Composable
fun SettingsPanel(
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
    resourceBundle: ResourceBundle,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .weight(weight = 0.5f, fill = true)
                .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                .height(intrinsicSize = IntrinsicSize.Max)
        ) {
            Text(
                text = resourceBundle.getString("file.locations"),
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                    .background(color = Color.DarkGray)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.caption
            )

            PathPickerItem(
                title = resourceBundle.getString("json.path"),
                currentPath = jsonPath,
                icon = painterResource(resourcePath = "/drawables/folder_arrow_right.xml"),
                onClick = onChangeJsonPath,
                resourceBundle = resourceBundle,
                modifier = Modifier.fillMaxWidth().weight(weight = 0.5f, fill = true)
            )

            PathPickerItem(
                title = resourceBundle.getString("ical.path"),
                currentPath = iCalPath,
                icon = painterResource(resourcePath = "/drawables/folder_arrow_left.xml"),
                onClick = onChangeICalPath,
                resourceBundle = resourceBundle,
                modifier = Modifier.fillMaxWidth().weight(weight = 0.5f, fill = true)
            )
        }

        ExportOptionsGroup(
            exportPlaceVisit = exportPlaceVisit,
            exportActivitySegment = exportActivitySegment,
            onExportActivitySegmentClicked = onExportActivitySegmentChanged,
            onExportPlaceVisitClicked = onExportPlaceVisitChanged,
            modifier = Modifier.wrapContentSize(),
            resourceBundle = resourceBundle
        )

        ExtraOptionsGroup(
            isPlaceApiEnabled = enablePlacesApiLookup,
            isVerboseLogEnabled = verboseLogs,
            onEnablePlaceApiLookupClicked = onEnablePlaceApiLookupChanged,
            onVerboseLogClicked = onVerboseLogsChanged,
            modifier = Modifier.wrapContentSize(),
            resourceBundle = resourceBundle
        )
    }
}

@Composable
fun ExportOptionsGroup(
    exportPlaceVisit: Boolean,
    exportActivitySegment: Boolean,
    onExportActivitySegmentClicked: (Boolean) -> Unit,
    onExportPlaceVisitClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    resourceBundle: ResourceBundle,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .border(width = 1.dp, color = Color.LightGray)
            .width(intrinsicSize = IntrinsicSize.Min)
    ) {
        Text(
            text = resourceBundle.getString("export.options"),
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                .background(color = Color.DarkGray)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.caption
        )

        Row(
            modifier = Modifier
                .width(intrinsicSize = IntrinsicSize.Max)
                .padding(16.dp)
        ) {
            BinaryOptionButton(
                isChecked = exportActivitySegment,
                text = MessageFormat.format(resourceBundle.getString("activity.segments")),
                icon = painterResource(resourcePath = "/drawables/road_variant.xml"),
                onButtonClicked = onExportActivitySegmentClicked,
                modifier = Modifier.padding(end = 16.dp)
            )
            BinaryOptionButton(
                isChecked = exportPlaceVisit,
                text = MessageFormat.format(resourceBundle.getString("place.visits")),
                icon = painterResource(resourcePath = "/drawables/map_marker_outline.xml"),
                onButtonClicked = onExportPlaceVisitClicked
            )
        }
    }
}

@Composable
fun ExtraOptionsGroup(
    isPlaceApiEnabled: Boolean,
    isVerboseLogEnabled: Boolean,
    onEnablePlaceApiLookupClicked: (Boolean) -> Unit,
    onVerboseLogClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    resourceBundle: ResourceBundle,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .border(width = 1.dp, color = Color.LightGray)
            .width(intrinsicSize = IntrinsicSize.Min)
    ) {
        Text(
            text = resourceBundle.getString("advanced.settings"),
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                .background(color = Color.DarkGray)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.caption
        )
        Row(
            modifier = Modifier
                .width(intrinsicSize = IntrinsicSize.Max)
                .padding(16.dp)
        ) {
            BinaryOptionButton(
                isChecked = isPlaceApiEnabled,
                text = MessageFormat.format(resourceBundle.getString("place.api.lookup")),
                icon = painterResource(resourcePath = "/drawables/database_marker_outline.xml"),
                onButtonClicked = onEnablePlaceApiLookupClicked,
                modifier = Modifier.padding(end = 16.dp)
            )
            BinaryOptionButton(
                isChecked = isVerboseLogEnabled,
                text = MessageFormat.format(resourceBundle.getString("verbose.console.log")),
                icon = painterResource(resourcePath = "/drawables/console.xml"),
                onButtonClicked = onVerboseLogClicked
            )
        }
    }
}

@Preview
@Composable
fun ExportOptionsGroupPreview() {
    JapanesePurpleTheme {
        ExportOptionsGroup(
            exportPlaceVisit = true,
            exportActivitySegment = false,
            onExportActivitySegmentClicked = {},
            onExportPlaceVisitClicked = {},
            resourceBundle = getBundle("resources", Locale.ENGLISH)
        )
    }
}
