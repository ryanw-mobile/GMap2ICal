/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import uk.ryanwong.gmap2ics.ui.GregoryGreenTheme
import uk.ryanwong.gmap2ics.ui.screens.ContentDescriptions

@Composable
fun StatusBar(
    statusMessage: String,
    progress: Float?,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(height = 32.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = statusMessage,
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Left,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(weight = 1f, fill = true)
        )
        progress?.let { progress ->
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .width(120.dp)
                    .semantics {
                        contentDescription = ContentDescriptions.LINEAR_PROGRESS_INDICATOR
                    }
            )
        }
    }
}

@Preview
@Composable
private fun StatusBarPreview() {
    GregoryGreenTheme {
        StatusBar(
            statusMessage = "some-long-message ".repeat(10),
            progress = 82.5f
        )
    }
}
