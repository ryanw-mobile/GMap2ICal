package uk.ryanwong.gmap2ics.ui.utils

import java.util.Locale
import java.util.ResourceBundle

class DefaultResourceBundle(
    private val resourceBundle: ResourceBundle = java.util.ResourceBundle.getBundle(
        "resources",
        Locale.ENGLISH
    )
) : ResourceBundleWrapper {
    override fun getString(key: String): String {
        return resourceBundle.getString(key)
    }
}