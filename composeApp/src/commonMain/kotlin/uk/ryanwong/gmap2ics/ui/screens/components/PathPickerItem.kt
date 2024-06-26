/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */
@file:OptIn(ExperimentalResourceApi::class)

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import gmap2ical.composeapp.generated.resources.Res
import gmap2ical.composeapp.generated.resources.change_folder
import gmap2ical.composeapp.generated.resources.folder_arrow_left
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import uk.ryanwong.gmap2ics.ui.theme.gregorygreen.GregoryGreenTheme

@Composable
fun PathPickerItem(
    title: String,
    icon: Painter,
    currentPath: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth(1f)
            .padding(top = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 8.dp),
        ) {
            Button(
                enabled = true,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                onClick = onClick,
                contentPadding = PaddingValues(all = 4.dp),
                modifier = Modifier.size(size = 24.dp),
            ) {
                Image(
                    painter = icon,
                    contentDescription = stringResource(Res.string.change_folder),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onPrimary),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground,
            )

            Text(
                text = currentPath,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Clip,
                maxLines = 2,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            )
        }
    }
}

@Preview
@Composable
fun PathPickerItemPreview() {
    GregoryGreenTheme {
        PathPickerItem(
            title = "some very long text ".repeat(10),
            currentPath = "some very long text ".repeat(10),
            icon = painterResource(Res.drawable.folder_arrow_left),
            onClick = {},
        )
    }
}
