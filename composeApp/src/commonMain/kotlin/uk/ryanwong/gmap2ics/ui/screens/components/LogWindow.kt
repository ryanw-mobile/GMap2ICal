/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */
@file:OptIn(ExperimentalResourceApi::class)

package uk.ryanwong.gmap2ics.ui.screens.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import gmap2ical.composeapp.generated.resources.Res
import gmap2ical.composeapp.generated.resources.exported
import gmap2ical.composeapp.generated.resources.ignored
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import uk.ryanwong.gmap2ics.domain.models.UILogEntry
import uk.ryanwong.gmap2ics.ui.theme.gregorygreen.GregoryGreenTheme

@Composable
fun LogWindowTabRow(
    logWindowUIState: LogWindowUIState,
    modifier: Modifier = Modifier,
) {
    val buttonStateNormal = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
    )

    val buttonStateActive = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
    )

    val bubbleStateNormalBackground = Color.LightGray
    val bubbleStateNormalTextColor = Color.Black
    val bubbleStateActiveBackground = MaterialTheme.colors.primaryVariant
    val bubbleStateActiveTextColor = Color.White

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        TextButton(
            enabled = true,
            border = BorderStroke(width = 1.dp, color = Color.Gray),
            modifier = Modifier.wrapContentSize().padding(end = 8.dp),
            onClick = { logWindowUIState.onTabSelected(LogWindowTab.EXPORTED) },
            colors = if (logWindowUIState.selectedTab == LogWindowTab.EXPORTED) buttonStateActive else buttonStateNormal,
            contentPadding = PaddingValues(all = 0.dp),
            shape = RectangleShape,
        ) {
            Text(
                text = stringResource(Res.string.exported),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.wrapContentSize().padding(horizontal = 8.dp),
            )
            BubbleInteger(
                value = logWindowUIState.exportedCount,
                backgroundColor = if (logWindowUIState.selectedTab == LogWindowTab.EXPORTED) bubbleStateActiveBackground else bubbleStateNormalBackground,
                textColor = if (logWindowUIState.selectedTab == LogWindowTab.EXPORTED) bubbleStateActiveTextColor else bubbleStateNormalTextColor,
            )
        }

        TextButton(
            enabled = true,
            border = BorderStroke(width = 1.dp, color = Color.Gray),
            modifier = Modifier.wrapContentSize().padding(end = 8.dp),
            onClick = { logWindowUIState.onTabSelected(LogWindowTab.IGNORED) },
            colors = if (logWindowUIState.selectedTab == LogWindowTab.IGNORED) buttonStateActive else buttonStateNormal,
            contentPadding = PaddingValues(all = 0.dp),
            shape = RectangleShape,
        ) {
            Text(
                text = stringResource(Res.string.ignored),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.wrapContentSize().padding(horizontal = 8.dp),
            )
            BubbleInteger(
                value = logWindowUIState.ignoredCount,
                backgroundColor = if (logWindowUIState.selectedTab == LogWindowTab.IGNORED) bubbleStateActiveBackground else bubbleStateNormalBackground,
                textColor = if (logWindowUIState.selectedTab == LogWindowTab.IGNORED) bubbleStateActiveTextColor else bubbleStateNormalTextColor,
            )
        }
    }
}

@Composable
fun BubbleInteger(
    value: Int,
    textColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    if (value > 0) {
        Surface(
            shape = CircleShape,
            color = backgroundColor,
            modifier = Modifier.padding(end = 8.dp),
        ) {
            Text(
                text = value.toString(),
                color = textColor,
                style = MaterialTheme.typography.overline,
                modifier = modifier.wrapContentSize()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }
    }
}

@Preview
@Composable
private fun BubbleIntegerPreview() {
    GregoryGreenTheme {
        BubbleInteger(
            value = 100,
            backgroundColor = MaterialTheme.colors.primaryVariant,
            textColor = MaterialTheme.colors.onBackground,
        )
    }
}

@Composable
fun LogWindow(
    logEntries: List<UILogEntry>,
    lazyListState: LazyListState,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        // Making text selectable helps crosscheck source files for debugging purpose
        SelectionContainer {
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                userScrollEnabled = true,
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .background(color = MaterialTheme.colors.background)
                    .border(width = 1.dp, color = Color.Gray)
                    .scrollable(
                        enabled = true,
                        orientation = Orientation.Vertical,
                        state = scrollState,
                    ),
            ) {
                itemsIndexed(items = logEntries) { _, uiLogEntry ->
                    Column {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                        ) {
                            Text(
                                text = uiLogEntry.emoji,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(width = 48.dp)
                                    .padding(end = 8.dp),
                            )
                            Text(
                                text = uiLogEntry.message,
                                style = MaterialTheme.typography.body1,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colors.onBackground,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                            )
                        }
                        Spacer(
                            modifier = Modifier.fillMaxWidth()
                                .wrapContentHeight()
                                .height(height = 1.dp)
                                .background(color = Color.LightGray),
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
                    hoverColor = MaterialTheme.colors.primary,
                ),
                modifier = Modifier.align(Alignment.CenterEnd)
                    .padding(top = 1.dp, bottom = 1.dp, end = 17.dp)
                    .background(color = Color.LightGray),
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
private fun LogWindowPreview() {
    GregoryGreenTheme {
        LogWindow(
            logEntries = listOf(
                UILogEntry(
                    emoji = "👨🏻‍🦲",
                    message = "some very long text".repeat(5),
                ),
                UILogEntry(
                    emoji = "👨🏻‍🦲",
                    message = "some very long text".repeat(5),
                ),
            ),
            lazyListState = rememberLazyListState(),
            scrollState = rememberScrollState(),
        )
    }
}
