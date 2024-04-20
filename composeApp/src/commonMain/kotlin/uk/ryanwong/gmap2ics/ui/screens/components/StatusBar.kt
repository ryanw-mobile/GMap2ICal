/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
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
import gmap2ical.composeapp.generated.resources.Res
import gmap2ical.composeapp.generated.resources.content_description_linear_progress_indicator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import uk.ryanwong.gmap2ics.ui.theme.gregorygreen.GregoryGreenTheme

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StatusBar(
    statusMessage: String,
    progress: Float?,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(height = 32.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = statusMessage,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Left,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(weight = 1f, fill = true),
        )
        progress?.let { progress ->
            val semanticsDescription = stringResource(Res.string.content_description_linear_progress_indicator)
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .width(120.dp)
                    .semantics {
                        contentDescription = semanticsDescription
                    },
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
            progress = 82.5f,
        )
    }
}
