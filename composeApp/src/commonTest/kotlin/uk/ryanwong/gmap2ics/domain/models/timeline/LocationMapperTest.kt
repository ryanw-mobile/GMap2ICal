/*
 * Copyright (c) 2023-2025. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.domain.models.timeline

import junit.framework.TestCase.assertNull
import uk.ryanwong.gmap2ics.data.models.timeline.ActivityLocationDto
import uk.ryanwong.gmap2ics.data.models.timeline.LocationDto
import kotlin.test.Test
import kotlin.test.assertEquals

internal class LocationMapperTest {
    @Test
    fun `from Location Data Model should convert locationDataModel to Location Domain Model correctly`() {
        val locationDtoDataModel = LocationDto(
            address = "some-address",
            latitudeE7 = 343970563,
            longitudeE7 = 1324677422,
            locationConfidence = 0.987654,
            name = "some-name",
            placeId = "some-place-id",
        )
        val expectedLocationDomainModel = Location(
            placeId = "some-place-id",
            latitudeE7 = 343970563,
            longitudeE7 = 1324677422,
            name = "some-name",
            address = "some-address",
        )

        val locationDomainModel = locationDtoDataModel.toDomainModel()
        assertEquals(expectedLocationDomainModel, locationDomainModel)
    }

    @Test
    fun `from Location Data Model should return null if locationDataModel does not contain latitudeE7`() {
        val locationDtoDataModel = LocationDto(
            address = "some-address",
            latitudeE7 = null,
            longitudeE7 = 1324677422,
            locationConfidence = 0.987654,
            name = "some-name",
            placeId = "some-place-id",
        )

        val locationDomainModel = locationDtoDataModel.toDomainModel()
        assertNull(locationDomainModel)
    }

    @Test
    fun `from Location Data Model should return null if locationDataModel does not contain longitudeE7`() {
        val locationDtoDataModel = LocationDto(
            address = "some-address",
            latitudeE7 = 343970563,
            longitudeE7 = null,
            locationConfidence = 0.987654,
            name = "some-name",
            placeId = "some-place-id",
        )

        val locationDomainModel = locationDtoDataModel.toDomainModel()
        assertNull(locationDomainModel)
    }

    @Test
    fun `from ActivityLocation Data Model should convert activityLocationDataModel to Location Domain Model correctly`() {
        val activityLocationDtoDataModel = ActivityLocationDto(
            address = "some-address",
            latitudeE7 = 343970563,
            longitudeE7 = 1324677422,
            locationConfidence = 0.987654,
            name = "some-name",
            placeId = "some-place-id",
        )
        val expectedLocationDomainModel = Location(
            placeId = "some-place-id",
            latitudeE7 = 343970563,
            longitudeE7 = 1324677422,
            name = "some-name",
            address = "some-address",
        )

        val locationDomainModel = activityLocationDtoDataModel.toDomainModel()
        assertEquals(expectedLocationDomainModel, locationDomainModel)
    }

    @Test
    fun `from ActivityLocation Data Model should return null if activityLocationDataModel does not contain latitudeE7`() {
        val activityLocationDtoDataModel = ActivityLocationDto(
            address = "some-address",
            latitudeE7 = null,
            longitudeE7 = 1324677422,
            locationConfidence = 0.987654,
            name = "some-name",
            placeId = "some-place-id",
        )

        val locationDomainModel = activityLocationDtoDataModel.toDomainModel()
        assertNull(locationDomainModel)
    }

    @Test
    fun `from ActivityLocation Data Model should return null if activityLocationDataModel does not contain longitudeE7`() {
        val activityLocationDtoDataModel = ActivityLocationDto(
            address = "some-address",
            latitudeE7 = 343970563,
            longitudeE7 = null,
            locationConfidence = 0.987654,
            name = "some-name",
            placeId = "some-place-id",
        )

        val locationDomainModel = activityLocationDtoDataModel.toDomainModel()
        assertNull(locationDomainModel)
    }
}
