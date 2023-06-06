/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.theme.gregorygreen

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.jthemedetecor.OsThemeDetector
import javax.swing.SwingUtilities

@Composable
fun GregoryGreenTheme(content: @Composable () -> Unit) {
    val detector: OsThemeDetector = OsThemeDetector.getDetector()
    var isSystemInDarkTheme by remember { mutableStateOf(detector.isDark) }
    detector.registerListener { isDark: Boolean ->
        SwingUtilities.invokeLater {
            isSystemInDarkTheme = isDark
        }
    }

    val colors = if (isSystemInDarkTheme) {
        Colors(
            primary = Color(0xff65dbb2),
            onPrimary = Color(0xff003829),
            primaryVariant = Color(0xff00513c),
            secondary = Color(0xffb3ccc0),
            onSecondary = Color(0xff1e352c),
            secondaryVariant = Color(0xff354c42),
            background = Color(0xff191c1a),
            onBackground = Color(0xffe1e3e0),
            surface = Color(0xff191c1a),
            onSurface = Color(0xffe1e3e0),
            error = Color(0xffffb4ab),
            onError = Color(0xff690005),
            isLight = false,
        )
    } else {
        Colors(
            primary = Color(0xff006c51),
            onPrimary = Color(0xffffffff),
            primaryVariant = Color(0xff83f8ce),
            secondary = Color(0xff4c6359),
            onSecondary = Color(0xffffffff),
            secondaryVariant = Color(0xffcee9db),
            background = Color(0xfffbfdf9),
            onBackground = Color(0xff191c1a),
            surface = Color(0xfffbfdf9),
            onSurface = Color(0xff191c1a),
            error = Color(0xffba1a1a),
            onError = Color(0xffffffff),
            isLight = true,
        )
    }

    MaterialTheme(
        colors = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content,
    )
}
