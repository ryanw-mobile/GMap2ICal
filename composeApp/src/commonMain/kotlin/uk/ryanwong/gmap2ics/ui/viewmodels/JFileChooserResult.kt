/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.ui.viewmodels

sealed class JFileChooserResult {
    data class Error(val errorCode: Int) : JFileChooserResult()
    data class AbsolutePath(val absolutePath: String) : JFileChooserResult()
    object Cancelled : JFileChooserResult()
}
