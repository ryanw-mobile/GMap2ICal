/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.utils

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkObject
import java.util.Locale
import java.util.ResourceBundle

internal class DefaultResourceBundleTest : FreeSpec() {

    lateinit var defaultResourceBundle: DefaultResourceBundle

    private fun setupResourceBundle() {
        val resourceBundle: ResourceBundle = ResourceBundle.getBundle(
            "resources",
            Locale.ENGLISH
        )
        mockkObject(resourceBundle)
        every { resourceBundle.getString(any()) } returns "some-resource-string"

        defaultResourceBundle = DefaultResourceBundle(resourceBundle = resourceBundle)
    }

    init {
        "getString() should call ResourceBundle.getString()" {
            // 🔴 Given
            setupResourceBundle()

            // 🟡 When
            val returnString = defaultResourceBundle.getString(key = "some-key")

            // 🟢 Then
            returnString shouldBe "some-resource-string"
        }
    }
}