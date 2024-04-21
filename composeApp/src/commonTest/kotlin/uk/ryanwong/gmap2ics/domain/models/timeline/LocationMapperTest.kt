/*
 * Copyright (c) 2023-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class LocationMapperTest : FreeSpec() {
    init {
        "from Location Data Model" - {
            "should convert locationDataModel to Location Domain Model correctly" - {
                val locationDataModel = uk.ryanwong.gmap2ics.data.models.timeline.Location(
                    address = "some-address",
                    latitudeE7 = 343970563,
                    longitudeE7 = 1324677422,
                    locationConfidence = 0.987654,
                    name = "some-name",
                    placeId = "some-place-id",
                )

                val locationDomainModel = locationDataModel.toDomainModel()

                locationDomainModel shouldBe Location(
                    placeId = "some-place-id",
                    latitudeE7 = 343970563,
                    longitudeE7 = 1324677422,
                    name = "some-name",
                    address = "some-address",
                )
            }

            "should return null if locationDataModel does not contain latitudeE7" {
                val locationDataModel = uk.ryanwong.gmap2ics.data.models.timeline.Location(
                    address = "some-address",
                    latitudeE7 = null,
                    longitudeE7 = 1324677422,
                    locationConfidence = 0.987654,
                    name = "some-name",
                    placeId = "some-place-id",
                )

                val locationDomainModel = locationDataModel.toDomainModel()

                locationDomainModel shouldBe null
            }

            "should return null if locationDataModel does not contain longitudeE7" {
                val locationDataModel = uk.ryanwong.gmap2ics.data.models.timeline.Location(
                    address = "some-address",
                    latitudeE7 = 343970563,
                    longitudeE7 = null,
                    locationConfidence = 0.987654,
                    name = "some-name",
                    placeId = "some-place-id",
                )

                val locationDomainModel = locationDataModel.toDomainModel()

                locationDomainModel shouldBe null
            }
        }

        "from ActivityLocation Data Model" - {
            "should convert activityLocationDataModel to Location Domain Model correctly" - {
                val activityLocationDataModel = uk.ryanwong.gmap2ics.data.models.timeline.ActivityLocation(
                    address = "some-address",
                    latitudeE7 = 343970563,
                    longitudeE7 = 1324677422,
                    locationConfidence = 0.987654,
                    name = "some-name",
                    placeId = "some-place-id",
                )

                val locationDomainModel = activityLocationDataModel.toDomainModel()

                locationDomainModel shouldBe Location(
                    placeId = "some-place-id",
                    latitudeE7 = 343970563,
                    longitudeE7 = 1324677422,
                    name = "some-name",
                    address = "some-address",
                )
            }

            "should return null if activityLocationDataModel does not contain latitudeE7" {
                val activityLocationDataModel = uk.ryanwong.gmap2ics.data.models.timeline.ActivityLocation(
                    address = "some-address",
                    latitudeE7 = null,
                    longitudeE7 = 1324677422,
                    locationConfidence = 0.987654,
                    name = "some-name",
                    placeId = "some-place-id",
                )

                val locationDomainModel = activityLocationDataModel.toDomainModel()

                locationDomainModel shouldBe null
            }

            "should return null if activityLocationDataModel does not contain longitudeE7" {
                val activityLocationDataModel = uk.ryanwong.gmap2ics.data.models.timeline.ActivityLocation(
                    address = "some-address",
                    latitudeE7 = 343970563,
                    longitudeE7 = null,
                    locationConfidence = 0.987654,
                    name = "some-name",
                    placeId = "some-place-id",
                )

                val locationDomainModel = activityLocationDataModel.toDomainModel()

                locationDomainModel shouldBe null
            }
        }
    }
}
