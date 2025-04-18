/*
 * Copyright (c) 2022-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models

import uk.ryanwong.gmap2ics.domain.models.VEventTestData.activityEndSegmentPlaceDetails
import uk.ryanwong.gmap2ics.domain.models.VEventTestData.activityFirstSegmentPlaceDetails
import uk.ryanwong.gmap2ics.domain.models.VEventTestData.activityLastSegmentPlaceDetails
import uk.ryanwong.gmap2ics.domain.models.VEventTestData.activitySegment
import uk.ryanwong.gmap2ics.domain.models.VEventTestData.activityStartSegmentPlaceDetails
import uk.ryanwong.gmap2ics.domain.models.VEventTestData.childVisit
import uk.ryanwong.gmap2ics.domain.models.VEventTestData.childVisitPlaceDetails
import uk.ryanwong.gmap2ics.domain.models.VEventTestData.placeVisit
import uk.ryanwong.gmap2ics.domain.models.VEventTestData.placeVisitPlaceDetails
import uk.ryanwong.gmap2ics.domain.models.timeline.LatLng
import uk.ryanwong.gmap2ics.domain.models.timeline.Location
import kotlin.test.Test
import kotlin.test.assertEquals

internal class VEventTest {
    /**
     * Test Plan -
     * 1. from() with happy flow - from ActivitySegment, PlaceVisit and ChildVisit
     * 2. export() with happy flow - test data need to include string replacement to make sure being substituted correctly
     * 3. getLocalizedTimeStamp() - test the fixed pattern for happy flow, and negative cases
     */

    /**
     * While for some strings we can safely replace them with "some-strings", for those we expect some special formatting,
     * like timestamp, or url, I would take a balance by making them meaningless enough but keeping the format.
     */

    @Test
    fun `returns VEvent with correct data when ActivitySegment has PlaceDetails`() {
        val activitySegment = activitySegment
        val shouldShowMiles = false
        val firstPlaceDetails = activityFirstSegmentPlaceDetails
        val lastPlaceDetails = activityLastSegmentPlaceDetails
        val startPlaceDetails = activityStartSegmentPlaceDetails
        val endPlaceDetails = activityEndSegmentPlaceDetails
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-end-location-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "🚐 7.9km (some-start-segment-name ➡ some-end-segment-name)",
            location = "some-last-segment-formatted-address",
            geo = LatLng(latitude = 26.33933, longitude = 127.85),
            description = "Start Location: some-start-segment-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-start-location-place-id\\n\\nEnd Location: some-end-segment-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-end-location-place-id\\n\\nFirst segment: some-first-segment-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-first-segment-place-id\\n\\nLast segment: some-last-segment-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-last-segment-place-id\\n\\n",
            url = "https://www.google.com/maps/place/?q=place_id:some-end-location-place-id",
            lastModified = "2011-11-11T11:22:22.222Z",
        )

        val vEvent = VEvent.from(
            activitySegment = activitySegment,
            shouldShowMiles = shouldShowMiles,
            firstPlaceDetails = firstPlaceDetails,
            lastPlaceDetails = lastPlaceDetails,
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns VEvent with correct data when ActivitySegment has null endLocation placeId`() {
        val activitySegment = activitySegment.copy(
            endLocation = Location(
                address = null,
                latitudeE7 = 263393300,
                longitudeE7 = 1278500000,
                name = null,
                placeId = null,
            ),
        )
        val shouldShowMiles = false
        val firstPlaceDetails = activityFirstSegmentPlaceDetails
        val lastPlaceDetails = activityLastSegmentPlaceDetails
        val startPlaceDetails = activityStartSegmentPlaceDetails
        val endPlaceDetails = activityEndSegmentPlaceDetails
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = null,
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "🚐 7.9km (some-start-segment-name ➡ some-end-segment-name)",
            location = "some-last-segment-formatted-address",
            geo = LatLng(latitude = 26.33933, longitude = 127.85),
            description = "Start Location: some-start-segment-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-start-location-place-id\\n\\nEnd Location: some-end-segment-formatted-address\\n\\n\\nFirst segment: some-first-segment-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-first-segment-place-id\\n\\nLast segment: some-last-segment-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-last-segment-place-id\\n\\n",
            url = "https://maps.google.com?q=26.33933,127.85",
            lastModified = "2011-11-11T11:22:22.222Z",
        )

        val vEvent = VEvent.from(
            activitySegment = activitySegment,
            shouldShowMiles = shouldShowMiles,
            firstPlaceDetails = firstPlaceDetails,
            lastPlaceDetails = lastPlaceDetails,
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns VEvent with miles when shouldShowMiles is true`() {
        val activitySegment = activitySegment
        val shouldShowMiles = true
        val firstPlaceDetails = activityFirstSegmentPlaceDetails
        val lastPlaceDetails = activityLastSegmentPlaceDetails
        val startPlaceDetails = activityStartSegmentPlaceDetails
        val endPlaceDetails = activityEndSegmentPlaceDetails
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-end-location-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "🚐 4.9mi (some-start-segment-name ➡ some-end-segment-name)",
            location = "some-last-segment-formatted-address",
            geo = LatLng(latitude = 26.33933, longitude = 127.85),
            description = "Start Location: some-start-segment-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-start-location-place-id\\n\\nEnd Location: some-end-segment-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-end-location-place-id\\n\\nFirst segment: some-first-segment-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-first-segment-place-id\\n\\nLast segment: some-last-segment-formatted-address\\nhttps://www.google.com/maps/place/?q=place_id:some-last-segment-place-id\\n\\n",
            url = "https://www.google.com/maps/place/?q=place_id:some-end-location-place-id",
            lastModified = "2011-11-11T11:22:22.222Z",
        )

        val vEvent = VEvent.from(
            activitySegment = activitySegment,
            shouldShowMiles = shouldShowMiles,
            firstPlaceDetails = firstPlaceDetails,
            lastPlaceDetails = lastPlaceDetails,
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns VEvent with correct data when ActivitySegment has no PlaceDetails`() {
        val activitySegment = activitySegment
        val shouldShowMiles = false
        val firstPlaceDetails = null
        val lastPlaceDetails = null
        val startPlaceDetails = null
        val endPlaceDetails = null
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-end-location-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "🚐 7.9km ",
            location = "26.33933,127.85",
            geo = LatLng(latitude = 26.33933, longitude = 127.85),
            description = "Start Location: 26.33833,127.8\\nhttps://maps.google.com?q=26.33833,127.8\\n\\nEnd Location: 26.33933,127.85\\nhttps://maps.google.com?q=26.33933,127.85\\n\\n",
            url = "https://www.google.com/maps/place/?q=place_id:some-end-location-place-id",
            lastModified = "2011-11-11T11:22:22.222Z",
        )

        val vEvent = VEvent.from(
            activitySegment = activitySegment,
            shouldShowMiles = shouldShowMiles,
            firstPlaceDetails = firstPlaceDetails,
            lastPlaceDetails = lastPlaceDetails,
            startPlaceDetails = startPlaceDetails,
            endPlaceDetails = endPlaceDetails,
        )
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns VEvent with correct data when PlaceVisit has PlaceDetails`() {
        val placeVisit = placeVisit
        val placeDetails = placeVisitPlaceDetails
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-place-visit-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "🏞 some-place-details-name",
            location = "some-place-details-formatted-address",
            geo = LatLng(latitude = 26.33833, longitude = 127.8),
            description = "Place ID:\\nsome-place-visit-place-id\\n\\nGoogle Maps URL:\\nhttps://maps.google.com/?cid=1021876599690425051",
            url = "https://maps.google.com/?cid=1021876599690425051",
            lastModified = "2011-11-11T11:22:22.222Z",
        )

        val vEvent = VEvent.from(placeVisit = placeVisit, placeDetails = placeDetails)
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns VEvent with correct data when PlaceVisit has no PlaceDetails`() {
        val placeVisit = placeVisit
        val placeDetails = null
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-place-visit-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "📍 some-name",
            location = "some-address",
            geo = LatLng(latitude = 26.33833, longitude = 127.8),
            description = "Place ID:\\nsome-place-visit-place-id\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:some-place-visit-place-id",
            url = "https://www.google.com/maps/place/?q=place_id:some-place-visit-place-id",
            lastModified = "2011-11-11T11:22:22.222Z",
        )

        val vEvent = VEvent.from(placeVisit = placeVisit, placeDetails = placeDetails)
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns VEvent with correct data when ChildVisit has PlaceDetails`() {
        val childVisit = childVisit
        val placeDetails = childVisitPlaceDetails
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-child-visit-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "🏞 some-place-details-name",
            location = "some-place-details-formatted-address",
            geo = LatLng(latitude = 26.33833, longitude = 127.8),
            description = "Place ID:\\nsome-child-visit-place-id\\n\\nGoogle Maps URL:\\nhttps://maps.google.com/?cid=1021876599690425051",
            url = "https://maps.google.com/?cid=1021876599690425051",
            lastModified = "2011-11-11T11:22:22.222Z",
        )

        val vEvent = VEvent.from(childVisit = childVisit, placeDetails = placeDetails)
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns VEvent with correct data when ChildVisit has no PlaceDetails`() {
        val childVisit = childVisit
        val placeDetails = null
        val expectedVEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-child-visit-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T11:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T11:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "📍 some-name",
            location = "some-address",
            geo = LatLng(latitude = 26.33833, longitude = 127.8),
            description = "Place ID:\\nsome-child-visit-place-id\\n\\nGoogle Maps URL:\\nhttps://www.google.com/maps/place/?q=place_id:some-child-visit-place-id",
            url = "https://www.google.com/maps/place/?q=place_id:some-child-visit-place-id",
            lastModified = "2011-11-11T11:22:22.222Z",
        )

        val vEvent = VEvent.from(childVisit = childVisit, placeDetails = placeDetails)
        assertEquals(expectedVEvent, vEvent)
    }

    @Test
    fun `returns correct iCal string when export`() {
        val vEvent = VEvent(
            uid = "2011-11-11T11:22:22.222Z",
            placeId = "some-child-visit-place-id",
            dtStamp = "2011-11-11T11:22:22.222Z",
            organizer = null,
            dtStart = RawTimestamp(timestamp = "2011-11-11T20:11:11.111Z", timezoneId = "Asia/Tokyo"),
            dtEnd = RawTimestamp(timestamp = "2011-11-11T20:22:22.222Z", timezoneId = "Asia/Tokyo"),
            summary = "🏞 some-place-details-name",
            location = "some-place-details-formatted-address",
            geo = LatLng(latitude = 26.33833, longitude = 127.8),
            description = "Place ID:\\nsome-child-visit-place-id\\n\\nGoogle Maps URL:\\nhttps://maps.google.com/?cid=1021876599690425051",
            url = "https://maps.google.com/?cid=1021876599690425051",
            lastModified = "2011-11-11T11:22:22.222Z",
        )
        val expectedICalString = "BEGIN:VEVENT\n" +
            "TRANSP:OPAQUE\n" +
            "DTSTART;TZID=Asia/Tokyo:20111112T051111\n" +
            "DTEND;TZID=Asia/Tokyo:20111112T052222\n" +
            "X-APPLE-STRUCTURED-LOCATION;VALUE=URI;X-APPLE-RADIUS=147;\n" +
            "X-TITLE=\"some-place-details-formatted-address\":geo:26.33833,127.8\n" +
            "UID:2011-11-11T11:22:22.222Z\n" +
            "DTSTAMP:2011-11-11T11:22:22.222Z\n" +
            "LOCATION:some-place-details-formatted-address\n" +
            "SUMMARY:\uD83C\uDFDE some-place-details-name\n" +
            "DESCRIPTION:Place ID:\\nsome-child-visit-place-id\\n\\nGoogle Maps URL:\\nhttps://maps.google.com/?cid=1021876599690425051\n" +
            "URL;VALUE=URI:https://maps.google.com/?cid=1021876599690425051\n" +
            "STATUS:CONFIRMED\n" +
            "SEQUENCE:1\n" +
            "LAST-MODIFIED:2011-11-11T11:22:22.222Z\n" +
            "CREATED:2011-11-11T11:22:22.222Z\n" +
            "X-APPLE-TRAVEL-ADVISORY-BEHAVIOR:AUTOMATIC\n" +
            "END:VEVENT\n"

        val iCalString = vEvent.export()
        assertEquals(expectedICalString, iCalString)
    }
}
