/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import gmap2ical.composeapp.generated.resources.Res
import gmap2ical.composeapp.generated.resources.file_locations
import gmap2ical.composeapp.generated.resources.folder_arrow_left
import gmap2ical.composeapp.generated.resources.folder_arrow_right
import gmap2ical.composeapp.generated.resources.ical_path
import gmap2ical.composeapp.generated.resources.json_path
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SettingsPanel(
    modifier: Modifier = Modifier,
    jsonPath: String,
    iCalPath: String,
    onChangeJsonPath: () -> Unit,
    onChangeICalPath: () -> Unit,
    exportOptionsGroup: @Composable () -> Unit,
    extraOptionsGroup: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .wrapContentHeight(),
    ) {
        Column(
            modifier = Modifier
                .weight(weight = 0.5f, fill = true)
                .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                .height(intrinsicSize = IntrinsicSize.Max),
        ) {
            Text(
                text = stringResource(Res.string.file_locations),
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

            PathPickerItem(
                title = stringResource(Res.string.json_path),
                currentPath = jsonPath,
                icon = painterResource(Res.drawable.folder_arrow_right),
                onClick = onChangeJsonPath,
                modifier = Modifier.fillMaxWidth().weight(weight = 0.5f, fill = true),
            )

            PathPickerItem(
                title = stringResource(Res.string.ical_path),
                currentPath = iCalPath,
                icon = painterResource(Res.drawable.folder_arrow_left),
                onClick = onChangeICalPath,
                modifier = Modifier.fillMaxWidth().weight(weight = 0.5f, fill = true),
            )
        }

        exportOptionsGroup()

        extraOptionsGroup()
    }
}
