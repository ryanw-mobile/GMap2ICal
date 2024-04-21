/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */
@file:OptIn(ExperimentalResourceApi::class)

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import gmap2ical.composeapp.generated.resources.Res
import gmap2ical.composeapp.generated.resources.activity_segments
import gmap2ical.composeapp.generated.resources.export_options
import gmap2ical.composeapp.generated.resources.map_marker_outline
import gmap2ical.composeapp.generated.resources.place_visits
import gmap2ical.composeapp.generated.resources.road_variant
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import uk.ryanwong.gmap2ics.ui.theme.gregorygreen.GregoryGreenTheme
import java.text.MessageFormat

@Composable
fun ExportOptionsGroup(
    exportPlaceVisit: Boolean,
    exportActivitySegment: Boolean,
    onExportActivitySegmentClicked: (Boolean) -> Unit,
    onExportPlaceVisitClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .border(width = 1.dp, color = Color.LightGray)
            .width(intrinsicSize = IntrinsicSize.Min),
    ) {
        Text(
            text = stringResource(Res.string.export_options),
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
                text = MessageFormat.format(stringResource(Res.string.activity_segments)),
                icon = painterResource(Res.drawable.road_variant),
                onButtonClicked = onExportActivitySegmentClicked,
                modifier = Modifier.padding(end = 16.dp),
            )
            BinaryOptionButton(
                isChecked = exportPlaceVisit,
                text = MessageFormat.format(stringResource(Res.string.place_visits)),
                icon = painterResource(Res.drawable.map_marker_outline),
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
        )
    }
}
