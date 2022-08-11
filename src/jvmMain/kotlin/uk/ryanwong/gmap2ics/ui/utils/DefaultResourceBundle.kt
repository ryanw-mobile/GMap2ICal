/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.utils

import java.util.Locale
import java.util.ResourceBundle

class DefaultResourceBundle(
    private val resourceBundle: ResourceBundle = ResourceBundle.getBundle(
        "resources",
        Locale.ENGLISH
    )
) : ResourceBundleWrapper {
    override fun getString(key: String): String {
        return resourceBundle.getString(key)
    }
}