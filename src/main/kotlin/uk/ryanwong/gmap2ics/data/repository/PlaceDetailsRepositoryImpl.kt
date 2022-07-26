package uk.ryanwong.gmap2ics.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import uk.ryanwong.gmap2ics.configs.Config
import uk.ryanwong.gmap2ics.data.api.PlacesDetailApi
import uk.ryanwong.gmap2ics.domain.models.PlaceDetails

class PlaceDetailsRepositoryImpl(private val configFile: Config) : PlaceDetailsRepository {
    private val client = HttpClient(CIO)
    private val placeDetailsService = PlacesDetailApi.retrofitService
    private val placesCache = mutableMapOf<String, PlaceDetails>()

    override suspend fun getPlaceDetails(placeId: String, placeTimeZoneId: String?): PlaceDetails? {
        if (placesCache.contains(key = placeId)) {
            return placesCache.getValue(placeId)
        }

        // Do API lookup and cache the results
        // If user does not supply an API Key means we always return null
        configFile.placesApiKey?.let { apiKey ->
            val language: String? = configFile.apiLanguageOverride.getOrDefault(
                key = placeTimeZoneId,
                defaultValue = configFile.apiLanguageOverride.get(key = "default")
            )

            try {
                val placeDetailsDataObject = language?.let {
                    placeDetailsService.getPlaceDetails(placeId = placeId, key = apiKey, language = it)
                } ?: placeDetailsService.getPlaceDetails(placeId = placeId, key = apiKey)

                if (!placeDetailsDataObject.isSuccessful) {
                    println("⛔️ Error getting API results: ${placeDetailsDataObject.message()}")
                    return null
                }

                return placeDetailsDataObject.body()?.result?.let { result ->
                    PlaceDetails.from(placeDetailsResult = result)
                        .also { placeDetailsDomainObject ->
                            placesCache[placeId] = placeDetailsDomainObject
                        }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return null
    }
}