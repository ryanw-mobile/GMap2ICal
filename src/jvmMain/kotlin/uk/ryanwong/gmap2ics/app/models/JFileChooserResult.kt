/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.app.models

sealed class JFileChooserResult {
    data class Error(val errorCode: Int) : JFileChooserResult()
    data class AbsolutePath(val absolutePath: String) : JFileChooserResult()
    object Cancelled : JFileChooserResult()
}
