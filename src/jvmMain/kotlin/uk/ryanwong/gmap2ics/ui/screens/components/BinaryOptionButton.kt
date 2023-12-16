/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uk.ryanwong.gmap2ics.ui.theme.gregorygreen.GregoryGreenTheme

@Composable
fun BinaryOptionButton(
    text: String,
    icon: Painter,
    isChecked: Boolean,
    onButtonClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.semantics { role = Role.Checkbox },
    ) {
        val buttonBorder = if (!isChecked) BorderStroke(width = 2.dp, color = MaterialTheme.colors.primary) else null
        val buttonBackground = if (!isChecked) Color.LightGray else MaterialTheme.colors.primary
        val buttonTint = if (!isChecked) MaterialTheme.colors.primary else Color.White

        Button(
            enabled = true,
            shape = CircleShape,
            border = buttonBorder,
            colors = ButtonDefaults.buttonColors(backgroundColor = buttonBackground),
            onClick = { onButtonClicked(!isChecked) },
            modifier = Modifier.size(size = 64.dp),
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = buttonTint),
                modifier = Modifier.fillMaxSize(),
            )
        }
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .width(IntrinsicSize.Max)
                .wrapContentHeight(),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onBackground,
        )
    }
}

@Preview
@Composable
private fun previewBinaryOptionButtonOff() {
    GregoryGreenTheme {
        BinaryOptionButton(
            isChecked = false,
            text = "Activity\nSegment",
            icon = painterResource(resourcePath = "/drawables/road_variant.xml"),
            onButtonClicked = {},
        )
    }
}

@Preview
@Composable
private fun previewBinaryOptionButtonOn() {
    GregoryGreenTheme {
        BinaryOptionButton(
            isChecked = true,
            text = "Activity\nSegment",
            icon = painterResource(resourcePath = "/drawables/road_variant.xml"),
            onButtonClicked = {},
        )
    }
}
