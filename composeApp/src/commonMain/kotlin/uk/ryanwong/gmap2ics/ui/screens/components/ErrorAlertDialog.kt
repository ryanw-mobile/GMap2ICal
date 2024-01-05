/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ErrorAlertDialog(
    text: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = { Text(text = text) },
        buttons = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
            ) {
                Button(
                    onClick = onDismissRequest,
                    shape = CircleShape,
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                ) {
                    Text(text = "OK")
                }
            }
        },
        modifier = modifier.defaultMinSize(minWidth = 320.dp),
    )
}
