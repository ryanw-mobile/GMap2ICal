package uk.ryanwong.gmap2ics.data

import uk.ryanwong.gmap2ics.domain.models.VEvent
import java.io.FileWriter

class ICalExporter(private val targetLocation : String) {

    fun exportICal(filename : String, vEvents : List<VEvent>) {
        println("💾 Exporting events in iCal format to $filename")

        // Preparing the file contents to write in one go
        val stringBuilder = StringBuilder()

        stringBuilder.run {
            append("BEGIN:VCALENDAR\n")
            append("VERSION:2.0\n")

            vEvents.forEach { vEvent ->
                append(vEvent.export())
            }

            append("END:VCALENDAR\n")
        }

        FileWriter( filename, false).use { fileWriter ->
            fileWriter.write(stringBuilder.toString())
        }
    }
}