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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uk.ryanwong.gmap2ics.ui.theme.gregorygreen.GregoryGreenTheme
import java.text.MessageFormat
import java.util.Locale
import java.util.ResourceBundle

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
            .width(intrinsicSize = IntrinsicSize.Min),
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
            style = MaterialTheme.typography.caption,
        )

        Row(
            modifier = Modifier
                .width(intrinsicSize = IntrinsicSize.Max)
                .padding(16.dp),
        ) {
            BinaryOptionButton(
                isChecked = exportActivitySegment,
                text = MessageFormat.format(resourceBundle.getString("activity.segments")),
                icon = painterResource(resourcePath = "/drawables/road_variant.xml"),
                onButtonClicked = onExportActivitySegmentClicked,
                modifier = Modifier.padding(end = 16.dp),
            )
            BinaryOptionButton(
                isChecked = exportPlaceVisit,
                text = MessageFormat.format(resourceBundle.getString("place.visits")),
                icon = painterResource(resourcePath = "/drawables/map_marker_outline.xml"),
                onButtonClicked = onExportPlaceVisitClicked,
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
            onExportPlaceVisitClicked = {},
            resourceBundle = ResourceBundle.getBundle("resources", Locale.ENGLISH),
        )
    }
}
