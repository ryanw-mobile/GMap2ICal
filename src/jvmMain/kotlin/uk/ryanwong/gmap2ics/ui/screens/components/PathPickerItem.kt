/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.util.Locale
import java.util.ResourceBundle

@Composable
fun PathPickerItem(
    title: String,
    currentPath: String,
    onClick: () -> Unit,
    resourceBundle: ResourceBundle,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth(1f)
            .padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 8.dp)
        ) {
            Image(
                painter = painterResource(resourcePath = "/drawables/folder.png"),
                contentDescription = resourceBundle.getString("change.folder"),
                modifier = Modifier
                    .clickable(onClick = onClick)
                    .padding(all = 4.dp)
                    .size(size = 24.dp)
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = currentPath,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Clip,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun PathPickerItemPreview() {
    MaterialTheme {
        PathPickerItem(
            title = "some very long text ".repeat(10),
            currentPath = "some very long text ".repeat(10),
            onClick = {},
            resourceBundle = ResourceBundle.getBundle("resources", Locale.ENGLISH)
        )
    }
}