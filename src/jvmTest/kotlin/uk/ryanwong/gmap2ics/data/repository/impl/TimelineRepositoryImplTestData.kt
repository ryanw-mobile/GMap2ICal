/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository.impl

import com.esri.core.geometry.Polygon
import uk.ryanwong.gmap2ics.app.ActivityType
import uk.ryanwong.gmap2ics.app.models.RawTimestamp
import uk.ryanwong.gmap2ics.app.models.timeline.Location
import uk.ryanwong.gmap2ics.app.models.timeline.Timeline
import uk.ryanwong.gmap2ics.app.models.timeline.TimelineEntry
import uk.ryanwong.gmap2ics.app.models.timeline.activity.Activity
import uk.ryanwong.gmap2ics.app.models.timeline.activity.ActivitySegment
import uk.ryanwong.gmap2ics.app.models.timeline.activity.WaypointPath
import uk.ryanwong.gmap2ics.app.models.timeline.placevisit.PlaceVisit
import us.dustinj.timezonemap.TimeZone

object TimelineRepositoryImplTestData {
    val mockJsonString = """{
  "timelineObjects": [{
    "activitySegment": {
      "startLocation": {
        "latitudeE7": 223126356,
        "longitudeE7": 1139226425,
        "sourceInfo": {
          "deviceTag": -1996871501
        }
      },
      "endLocation": {
        "latitudeE7": 357719867,
        "longitudeE7": 1403928501,
        "placeId": "ChIJVze90XnzImARoRp3YqEpbtU",
        "address": "日本\n〒282-0004 千葉県成田市古込１−１",
        "name": "Narita International Airport",
        "sourceInfo": {
          "deviceTag": -1996871501
        },
        "locationConfidence": 73.31435,
        "calibratedProbability": 64.26103,
        "isCurrentLocation": true
      },
      "duration": {
        "startTimestamp": "2019-07-08T03:00:33.170Z",
        "endTimestamp": "2019-07-08T07:26:17.631Z"
      },
      "activityType": "FLYING",
      "confidence": "MEDIUM",
      "activities": [{
        "activityType": "FLYING",
        "probability": 82.33181164317799
      }, {
        "activityType": "IN_PASSENGER_VEHICLE",
        "probability": 15.525859753940392
      }, {
        "activityType": "WALKING",
        "probability": 1.3997407567198206
      }],
      "simplifiedRawPath": {
        "points": [{
          "latE7": 357966993,
          "lngE7": 1403814697,
          "accuracyMeters": 5,
          "timestamp": "2019-07-08T06:59:13Z"
        }, {
          "latE7": 357691998,
          "lngE7": 1403957983,
          "accuracyMeters": 40,
          "timestamp": "2019-07-08T07:07:05Z"
        }, {
          "latE7": 357685952,
          "lngE7": 1403918481,
          "accuracyMeters": 104,
          "timestamp": "2019-07-08T07:18:13.371Z"
        }],
        "source": "BACKFILLED",
        "distanceMeters": 4017115.776842302
      }
    }
  }, {
    "placeVisit": {
      "location": {
        "latitudeE7": 357719867,
        "longitudeE7": 1403928501,
        "placeId": "ChIJVze90XnzImARoRp3YqEpbtU",
        "address": "日本\n〒282-0004 千葉県成田市古込１−１",
        "name": "Narita International Airport",
        "sourceInfo": {
          "deviceTag": -1996871501
        },
        "locationConfidence": 73.31435,
        "calibratedProbability": 64.26103,
        "isCurrentLocation": true
      },
      "duration": {
        "startTimestamp": "2019-07-08T07:26:17.631Z",
        "endTimestamp": "2019-07-08T08:17:27.614Z"
      },
      "placeConfidence": "MEDIUM_CONFIDENCE",
      "centerLatE7": 357737356,
      "centerLngE7": 1403882792,
      "visitConfidence": 90,
      "otherCandidateLocations": [{
        "latitudeE7": 357731182,
        "longitudeE7": 1403871838,
        "placeId": "ChIJVSqON3bzImARUTDmWTxZGRc",
        "address": "日本\n〒286-0104 千葉県成田市古込\n字古込",
        "name": "Narita Airport Terminal 2･3 Station",
        "locationConfidence": 15.418667,
        "calibratedProbability": 13.514672,
        "isCurrentLocation": true
      }, {
        "latitudeE7": 357732700,
        "longitudeE7": 1403876000,
        "placeId": "ChIJOWLCT3bzImARSeoxn2vEu8Y",
        "address": "日本\n〒286-0104 千葉県成田市古込",
        "name": "Narita Airport Terminal 2",
        "locationConfidence": 6.886124,
        "calibratedProbability": 6.0357823,
        "isCurrentLocation": true
      }, {
        "latitudeE7": 357731753,
        "longitudeE7": 1403875250,
        "placeId": "ChIJu94GUHbzImARJu9KQb-t0Qg",
        "address": "日本\n〒286-0104 千葉県成田市古込",
        "name": "空港第2ビル駅",
        "locationConfidence": 4.2738724,
        "calibratedProbability": 3.7461076,
        "isCurrentLocation": true
      }],
      "editConfirmationStatus": "NOT_CONFIRMED",
      "simplifiedRawPath": {
        "points": [{
          "latE7": 357744284,
          "lngE7": 1403893053,
          "accuracyMeters": 16,
          "timestamp": "2019-07-08T07:28:20.066Z"
        }, {
          "latE7": 357744284,
          "lngE7": 1403893053,
          "accuracyMeters": 16,
          "timestamp": "2019-07-08T07:30:40.005Z"
        }, {
          "latE7": 357732656,
          "lngE7": 1403893124,
          "accuracyMeters": 29,
          "timestamp": "2019-07-08T07:55:19.254Z"
        }],
        "source": "BACKFILLED",
        "distanceMeters": 386.36359598287527
      },
      "locationConfidence": 62,
      "placeVisitType": "SINGLE_PLACE",
      "placeVisitImportance": "MAIN"
    }
  }, {
    "activitySegment": {
      "startLocation": {
        "latitudeE7": 357738677,
        "longitudeE7": 1403869186,
        "sourceInfo": {
          "deviceTag": -1996871501
        }
      },
      "endLocation": {
        "latitudeE7": 357844447,
        "longitudeE7": 1403599892,
        "sourceInfo": {
          "deviceTag": -1996871501
        }
      },
      "duration": {
        "startTimestamp": "2019-07-08T08:17:27.614Z",
        "endTimestamp": "2019-07-08T08:38:26.614Z"
      },
      "distance": 4663,
      "activityType": "IN_PASSENGER_VEHICLE",
      "confidence": "HIGH",
      "activities": [{
        "activityType": "IN_PASSENGER_VEHICLE",
        "probability": 93.9788799029986
      }, {
        "activityType": "IN_BUS",
        "probability": 3.4535111679906736
      }, {
        "activityType": "WALKING",
        "probability": 2.423142363564658
      }],
      "waypointPath": {
        "waypoints": [{
          "latE7": 357738342,
          "lngE7": 1403868408
        }, {
          "latE7": 357682495,
          "lngE7": 1403904418
        }, {
          "latE7": 357678260,
          "lngE7": 1403901977
        }],
        "source": "INFERRED",
        "roadSegment": [{
          "placeId": "ChIJKeTzWnbzImARRKsnk6f2Pu0"
        }, {
          "placeId": "ChIJVxrdQnbzImARGnKYpaFhR58"
        }, {
          "placeId": "ChIJ8f0MaXbzImAROsshg74NYkk"
        }],
        "distanceMeters": 5148.908939359117
      },
      "simplifiedRawPath": {
        "points": [{
          "latE7": 357671620,
          "lngE7": 1403914415,
          "accuracyMeters": 82,
          "timestamp": "2019-07-08T08:19:32.637Z"
        }, {
          "latE7": 357711351,
          "lngE7": 1403842427,
          "accuracyMeters": 92,
          "timestamp": "2019-07-08T08:25:22.021Z"
        }]
      },
      "parkingEvent": {
        "location": {
          "latitudeE7": 357840507,
          "longitudeE7": 1403588484,
          "accuracyMetres": 151
        },
        "method": "END_OF_ACTIVITY_SEGMENT",
        "locationSource": "UNKNOWN",
        "timestamp": "2019-07-08T08:35:34.344Z"
      }
    }
  }]
}"""

    val mockTimeLineFromJsonString = Timeline(
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
                ), placeVisit = null
            ),

            TimelineEntry(
                activitySegment = null, placeVisit = PlaceVisit(
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
                    childVisits = listOf(),
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
                ), placeVisit = null
            )
        )
    )
}