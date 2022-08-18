/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import uk.ryanwong.gmap2ics.app.models.UILogEntry

@Composable
fun LogWindow(
    logEntries: List<UILogEntry>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        val lazyListState = rememberLazyListState()
        val scrollState = rememberScrollState()

        // Making text selectable helps crosscheck source files for debugging purpose
        SelectionContainer {
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                userScrollEnabled = true,
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .background(color = Color.White)
                    .border(width = 1.dp, color = Color.Gray)
                    .scrollable(
                        enabled = true,
                        orientation = Orientation.Vertical,
                        state = scrollState
                    )
            ) {
                itemsIndexed(items = logEntries) { _, uiLogEntry ->
                    Column {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = uiLogEntry.emoji,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(width = 48.dp)
                                    .padding(end = 8.dp)
                            )
                            Text(
                                text = uiLogEntry.message,
                                style = MaterialTheme.typography.body1,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            )
                        }
                        Spacer(
                            modifier = Modifier.fillMaxWidth()
                                .wrapContentHeight()
                                .height(height = 1.dp)
                                .background(color = Color.LightGray)
                        )
                    }
                }
            }
        }

        val scrollbarAdapter = rememberScrollbarAdapter(scrollState = lazyListState)
        if (logEntries.isNotEmpty()) {
            VerticalScrollbar(
                adapter = scrollbarAdapter,
                style = LocalScrollbarStyle.current.copy(
                    unhoverColor = Color.Gray,
                    hoverColor = MaterialTheme.colors.primary
                ),
                modifier = Modifier.align(Alignment.CenterEnd)
                    .padding(top = 1.dp, bottom = 1.dp, end = 17.dp)
                    .background(color = Color.LightGray)
            )
        }

        LaunchedEffect(key1 = logEntries) {
            if (logEntries.isNotEmpty()) {
                lazyListState.scrollToItem(index = logEntries.lastIndex)
            }
        }
    }
}

@Preview
@Composable
fun LogWindowPreview() {
    MaterialTheme {
        LogWindow(
            logEntries = listOf(
                UILogEntry(
                    emoji = "👨🏻‍🦲",
                    message = "some very very very very very very very very very very very very very very very very very very very very  long text"
                ),
                UILogEntry(
                    emoji = "👨🏻‍🦲",
                    message = "some very very very very very very very very very very very very very very very very very very very very  long text"
                )
            )
        )
    }
}
