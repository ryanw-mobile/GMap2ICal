/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

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
import java.text.MessageFormat
import java.util.ResourceBundle

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
