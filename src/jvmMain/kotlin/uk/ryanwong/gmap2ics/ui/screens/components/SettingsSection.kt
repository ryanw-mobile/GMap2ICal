/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uk.ryanwong.gmap2ics.ui.GregoryGreenTheme
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
    Row(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.wrapContentSize().weight(0.5f, true)) {
            PathPickerItem(
                title = resourceBundle.getString("json.path"),
                currentPath = jsonPath,
                onClick = onChangeJsonPath,
                resourceBundle = resourceBundle,
                modifier = Modifier.wrapContentSize()
            )

            PathPickerItem(
                title = resourceBundle.getString("ical.path"),
                currentPath = iCalPath,
                onClick = onChangeICalPath,
                resourceBundle = resourceBundle,
                modifier = Modifier.wrapContentSize()
            )
        }

        ExportOptionsGroup(
            exportPlaceVisit = exportPlaceVisit,
            exportActivitySegment = exportActivitySegment,
            onExportActivitySegmentClicked = onExportActivitySegmentChanged,
            onExportPlaceVisitClicked = onExportPlaceVisitChanged,
            modifier = Modifier.wrapContentSize()
        )

        ExtraOptionsGroup(
            isPlaceApiEnabled = enablePlacesApiLookup,
            isVerboseLogEnabled = verboseLogs,
            onEnablePlaceApiLookupClicked = onEnablePlaceApiLookupChanged,
            onVerboseLogClicked = onVerboseLogsChanged,
            modifier = Modifier.wrapContentSize()
        )
    }
}

@Composable
fun ExportOptionsGroup(
    exportPlaceVisit: Boolean,
    exportActivitySegment: Boolean,
    onExportActivitySegmentClicked: (Boolean) -> Unit,
    onExportPlaceVisitClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .border(width = 1.dp, color = Color.LightGray)
            .width(intrinsicSize = IntrinsicSize.Min)
    ) {
        Text(
            text = "Export Options",
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                .background(color = Color.DarkGray)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.caption
        )
        Row(
            modifier = Modifier.width(intrinsicSize = IntrinsicSize.Max)
                .padding(16.dp)
        ) {
            BinaryOptionButton(
                isChecked = exportActivitySegment,
                text = "Activity\nSegments",
                icon = painterResource(resourcePath = "/drawables/road_variant.xml"),
                onButtonClicked = onExportActivitySegmentClicked,
                modifier = Modifier.padding(end = 16.dp)
            )
            BinaryOptionButton(
                isChecked = exportPlaceVisit,
                text = "Place\nVisits",
                icon = painterResource(resourcePath = "/drawables/road_variant.xml"),
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .border(width = 1.dp, color = Color.LightGray)
            .width(intrinsicSize = IntrinsicSize.Min)
    ) {
        Text(
            text = "Advanced Settings",
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                .background(color = Color.DarkGray)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.caption
        )
        Row(
            modifier = Modifier.width(intrinsicSize = IntrinsicSize.Max)
                .padding(16.dp)
        ) {
            BinaryOptionButton(
                isChecked = isPlaceApiEnabled,
                text = "Place Api\nLookup",
                icon = painterResource(resourcePath = "/drawables/road_variant.xml"),
                onButtonClicked = onEnablePlaceApiLookupClicked,
                modifier = Modifier.padding(end = 16.dp)
            )
            BinaryOptionButton(
                isChecked = isVerboseLogEnabled,
                text = "Verbose\nConsole Log",
                icon = painterResource(resourcePath = "/drawables/road_variant.xml"),
                onButtonClicked = onVerboseLogClicked
            )
        }
    }
}

@Preview
@Composable
fun ExportOptionsGroupPreview() {
    GregoryGreenTheme {
        ExportOptionsGroup(
            exportPlaceVisit = true,
            exportActivitySegment = false,
            onExportActivitySegmentClicked = {},
            onExportPlaceVisitClicked = {}
        )
    }
}

@Composable
fun BinaryOptionButton(
    text: String,
    icon: Painter,
    isChecked: Boolean,
    onButtonClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Button(
            enabled = true,
            shape = CircleShape,
            border = if (!isChecked) BorderStroke(width = 2.dp, color = MaterialTheme.colors.primary) else null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (!isChecked) Color.LightGray else MaterialTheme.colors.primary
            ),
            onClick = { onButtonClicked(!isChecked) },
            modifier = Modifier.size(size = 64.dp)
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = if (!isChecked) MaterialTheme.colors.primary else Color.White),
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
                .width(IntrinsicSize.Max)
                .wrapContentHeight(),
            style = MaterialTheme.typography.caption
        )
    }
}

@Preview
@Composable
private fun previewBinaryOptionButtonOff() {
    GregoryGreenTheme {
        BinaryOptionButton(
            isChecked = false,
            text = "Activity\nSegment",
            icon = painterResource(resourcePath = "/drawables/road_variant.xml"),
            onButtonClicked = {}
        )
    }
}

@Preview
@Composable
private fun previewBinaryOptionButtonOn() {
    GregoryGreenTheme {
        BinaryOptionButton(
            isChecked = true,
            text = "Activity\nSegment",
            icon = painterResource(resourcePath = "/drawables/road_variant.xml"),
            onButtonClicked = {}
        )
    }
}