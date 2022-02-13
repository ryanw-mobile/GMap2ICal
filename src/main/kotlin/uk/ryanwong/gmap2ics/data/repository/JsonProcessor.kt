package uk.ryanwong.gmap2ics.data.repository

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import uk.ryanwong.gmap2ics.data.models.TimelineObjects
import java.io.File

class JsonProcessor {

    private val objectMapper = jacksonObjectMapper().apply {
        registerKotlinModule()
        registerModule(JavaTimeModule())
    }

    fun parseTimeLine(filePath: String) : TimelineObjects? {
        val jsonString: String
        try {
            jsonString = File(filePath).readText(Charsets.UTF_8)
        } catch (npe: NullPointerException) {
            return null
        }

        return objectMapper.readValue(content = jsonString)
    }
}

