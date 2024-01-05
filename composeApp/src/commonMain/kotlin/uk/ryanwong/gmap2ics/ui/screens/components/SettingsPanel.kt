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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.ResourceBundle

@Composable
fun SettingsPanel(
    modifier: Modifier = Modifier,
    jsonPath: String,
    iCalPath: String,
    onChangeJsonPath: () -> Unit,
    onChangeICalPath: () -> Unit,
    resourceBundle: ResourceBundle,
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
                text = resourceBundle.getString("file.locations"),
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
                title = resourceBundle.getString("json.path"),
                currentPath = jsonPath,
                icon = painterResource(resourcePath = "/drawables/folder_arrow_right.xml"),
                onClick = onChangeJsonPath,
                resourceBundle = resourceBundle,
                modifier = Modifier.fillMaxWidth().weight(weight = 0.5f, fill = true),
            )

            PathPickerItem(
                title = resourceBundle.getString("ical.path"),
                currentPath = iCalPath,
                icon = painterResource(resourcePath = "/drawables/folder_arrow_left.xml"),
                onClick = onChangeICalPath,
                resourceBundle = resourceBundle,
                modifier = Modifier.fillMaxWidth().weight(weight = 0.5f, fill = true),
            )
        }

        exportOptionsGroup()

        extraOptionsGroup()
    }
}
