/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.ResourceBundle

@Composable
fun ExportActionButton(
    enabled: Boolean,
    onButtonClicked: (Boolean) -> Unit,
    resourceBundle: ResourceBundle,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(top = 16.dp),
    ) {
        val buttonBackground = if (!enabled) Color.LightGray else MaterialTheme.colors.error

        Button(
            enabled = enabled,
            shape = RoundedCornerShape(percent = 25),
            colors = ButtonDefaults.buttonColors(backgroundColor = buttonBackground),
            onClick = { onButtonClicked(!enabled) },
            modifier = Modifier.size(size = 64.dp),
        ) {
            Image(
                painter = painterResource(resourcePath = "/drawables/file_export_outline.xml"),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color.White),
                modifier = Modifier.fillMaxSize(),
            )
        }
        Text(
            text = resourceBundle.getString("convert"),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
                .width(IntrinsicSize.Max)
                .wrapContentHeight(),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onBackground,
        )
    }
}
