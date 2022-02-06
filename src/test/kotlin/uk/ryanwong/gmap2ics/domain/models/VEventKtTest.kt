package uk.ryanwong.gmap2ics.domain.models

import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsNull
import org.junit.Assert.assertThat
import org.junit.Test

class VEventKtTest {

    @Test
    fun dateWithPaddingZero_returnsCorrectOutput() {
        val googleTimeStamp = "2019-04-28T06:51:24.246Z"
        val expectedTimeStamp = "20190428T065124"

        val output = googleTimeStamp.googleToICalTimeStamp()
        assertThat(output, IsEqual(expectedTimeStamp))
    }

    @Test
    fun dateWithoutPaddingZero_returnsCorrectOutput() {
        val googleTimeStamp = "2019-4-8T6:1:1.246Z"
        val expectedTimeStamp = "20190408T060101"

        val output = googleTimeStamp.googleToICalTimeStamp()
        assertThat(output, IsEqual(expectedTimeStamp))
    }

    @Test
    fun dateWithoutMills_returnsCorrectOutput() {
        val googleTimeStamp = "2019-04-28T06:51:24Z"
        val expectedTimeStamp = "20190428T065124"

        val output = googleTimeStamp.googleToICalTimeStamp()
        assertThat(output, IsEqual(expectedTimeStamp))
    }

    @Test
    fun emptyDateString_returnsNull() {
        val googleTimeStamp = ""

        val output = googleTimeStamp.googleToICalTimeStamp()
        assertThat(output, IsNull())
    }
}