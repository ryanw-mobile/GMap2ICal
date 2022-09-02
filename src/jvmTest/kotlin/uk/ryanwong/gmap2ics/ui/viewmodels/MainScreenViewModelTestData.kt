/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.ui.viewmodels

import com.esri.core.geometry.Polygon
import uk.ryanwong.gmap2ics.app.models.ActivityType
import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.models.timeline.Timeline
import uk.ryanwong.gmap2ics.app.models.timeline.TimelineEntry
import uk.ryanwong.gmap2ics.app.models.timeline.activity.Activity
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.app.models.timeline.activity.WaypointPath
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.ChildVisit
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.PlaceVisit
import us.dustinj.timezonemap.TimeZone

object MainScreenViewModelTestData {

    val mockTimeLineWithActivityVisitAndChildVisit = Timeline(
        timelineEntries = listOf(
            TimelineEntry(
                activitySegment =
                ActivitySegment(
                    activities = listOf(
                        Activity(activityType = ActivityType.FLYING, rawActivityType = "FLYING"),
                        Activity(
                            activityType = ActivityType.IN_PASSENGER_VEHICLE,
                            rawActivityType = "IN_PASSENGER_VEHICLE"
                        ),
                        Activity(activityType = ActivityType.WALKING, rawActivityType = "WALKING")
                    ),
                    activityType = ActivityType.FLYING,
                    rawActivityType = "FLYING",
                    distance = 0,
                    durationEndTimestamp = RawTimestamp(
                        timestamp = "2019-07-08T07:26:17.631Z",
                        timezoneId = "Asia/Tokyo"
                    ),
                    durationStartTimestamp = RawTimestamp(
                        timestamp = "2019-07-08T03:00:33.170Z",
                        timezoneId = "Asia/Tokyo"
                    ),
                    endLocation =
                    Location(
                        placeId = "ChIJVze90XnzImARoRp3YqEpbtU",
                        latitudeE7 = 357719867,
                        longitudeE7 = 1403928501,
                        name = "Narita International Airport",
                        address = "日本\n〒282-0004 千葉県成田市古込１−１"
                    ),
                    startLocation = Location(
                        placeId = null,
                        latitudeE7 = 223126356,
                        longitudeE7 = 1139226425,
                        name = null,
                        address = null
                    ),
                    waypointPath = null,
                    lastEditedTimestamp = "2019-07-08T07:26:17.631Z",
                    eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon())
                ),
                placeVisit = null
            ),

            TimelineEntry(
                activitySegment = null,
                placeVisit = PlaceVisit(
                    durationEndTimestamp = RawTimestamp(
                        timestamp = "2019-07-08T08:17:27.614Z",
                        timezoneId = "Asia/Tokyo"
                    ),
                    durationStartTimestamp = RawTimestamp(
                        timestamp = "2019-07-08T07:26:17.631Z",
                        timezoneId = "Asia/Tokyo"
                    ),
                    lastEditedTimestamp = "2019-07-08T08:17:27.614Z",
                    location = Location(
                        placeId = "ChIJVze90XnzImARoRp3YqEpbtU",
                        latitudeE7 = 357719867, longitudeE7 = 1403928501,
                        name = "Narita International Airport",
                        address = "日本\n〒282-0004 千葉県成田市古込１−１"
                    ),
                    childVisits = listOf(
                        ChildVisit(
                            durationEndTimestamp = RawTimestamp(
                                timestamp = "2019-07-08T07:30:00Z",
                                timezoneId = "Asia/Tokyo"
                            ),
                            durationStartTimestamp = RawTimestamp(
                                timestamp = "2019-07-08T07:40:00Z",
                                timezoneId = "Asia/Tokyo"
                            ),
                            lastEditedTimestamp = "2022-02-20T01:17:06.535Z",
                            location = Location(
                                placeId = "some-child-place-id",
                                latitudeE7 = 357720867,
                                longitudeE7 = 1403930501,
                                name = "some-child-name", address = "some-child-address"
                            ),
                            eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon())
                        )
                    ),
                    eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon())
                )
            ),
            TimelineEntry(
                activitySegment = ActivitySegment(
                    activities = listOf(
                        Activity(
                            activityType = ActivityType.IN_PASSENGER_VEHICLE,
                            rawActivityType = "IN_PASSENGER_VEHICLE"
                        ),
                        Activity(activityType = ActivityType.IN_BUS, rawActivityType = "IN_BUS"),
                        Activity(activityType = ActivityType.WALKING, rawActivityType = "WALKING")
                    ),
                    activityType = ActivityType.IN_PASSENGER_VEHICLE,
                    rawActivityType = "IN_PASSENGER_VEHICLE",
                    distance = 4663,
                    durationEndTimestamp = RawTimestamp(
                        timestamp = "2019-07-08T08:38:26.614Z",
                        timezoneId = "Asia/Tokyo"
                    ),
                    durationStartTimestamp = RawTimestamp(
                        timestamp = "2019-07-08T08:17:27.614Z",
                        timezoneId = "Asia/Tokyo"
                    ),
                    endLocation = Location(
                        placeId = null,
                        latitudeE7 = 357844447,
                        longitudeE7 = 1403599892,
                        name = null,
                        address = null
                    ),
                    startLocation = Location(
                        placeId = null,
                        latitudeE7 = 357738677,
                        longitudeE7 = 1403869186,
                        name = null,
                        address = null
                    ),
                    waypointPath = WaypointPath(
                        distanceMeters = 5148.908939359117,
                        roadSegmentPlaceIds = listOf(
                            "ChIJKeTzWnbzImARRKsnk6f2Pu0",
                            "ChIJVxrdQnbzImARGnKYpaFhR58",
                            "ChIJ8f0MaXbzImAROsshg74NYkk"
                        )
                    ),
                    lastEditedTimestamp = "2019-07-08T08:38:26.614Z",
                    eventTimeZone = TimeZone(zoneId = "Asia/Tokyo", region = Polygon())
                ),
                placeVisit = null
            )
        )
    )
}
