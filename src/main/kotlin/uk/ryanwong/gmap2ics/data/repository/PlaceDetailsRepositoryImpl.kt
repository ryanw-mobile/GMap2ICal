package uk.ryanwong.gmap2ics.data.repository

import uk.ryanwong.gmap2ics.app.models.PlaceDetails
import uk.ryanwong.gmap2ics.configs.Config
import uk.ryanwong.gmap2ics.data.except
import uk.ryanwong.gmap2ics.data.source.googleapi.GoogleApiDataSource
import uk.ryanwong.gmap2ics.data.source.googleapi.retrofit.RetrofitGoogleApiDataSource
import kotlin.coroutines.cancellation.CancellationException

class PlaceDetailsRepositoryImpl(
    private val configFile: Config,
    private val networkDataSource: GoogleApiDataSource = RetrofitGoogleApiDataSource()
) : PlaceDetailsRepository {

    private val placesCache = mutableMapOf<String, PlaceDetails>()

    override suspend fun getPlaceDetails(placeId: String, placeTimeZoneId: String?): Result<PlaceDetails> {
        if (placesCache.contains(key = placeId)) {
            return Result.success(placesCache.getValue(placeId))
        }

        // Do API lookup and cache the results
        // If user does not supply an API Key means we always return null
        return configFile.placesApiKey?.let { apiKey ->
            val language: String? = configFile.apiLanguageOverride.getOrDefault(
                key = placeTimeZoneId,
                defaultValue = configFile.apiLanguageOverride.get(key = "default")
            )

            Result.runCatching {
                val placeDetailsDataObject = language?.let {
                    networkDataSource.getPlaceDetails(placeId = placeId, key = apiKey, language = it)
                } ?: networkDataSource.getPlaceDetails(placeId = placeId, key = apiKey)

                if (!placeDetailsDataObject.isSuccessful) {
                    throw Exception("⛔️ Error getting API results: ${placeDetailsDataObject.message()}")
                }

                placeDetailsDataObject.body()?.result?.let { result ->
                    PlaceDetails.from(placeDetailsResult = result)
                        .also { placeDetailsDomainObject ->
                            placesCache[placeId] = placeDetailsDomainObject
                        }
                } ?: throw Exception("not found")
            }.except<CancellationException, _>()
        } ?: Result.failure(Exception("not found"))
    }
}