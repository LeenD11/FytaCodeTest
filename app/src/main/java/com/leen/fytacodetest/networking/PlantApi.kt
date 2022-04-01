package com.leen.fytacodetest.networking

import com.leen.fytacodetest.networking.resultdataclasses.ApiResponse
import com.leen.fytacodetest.utils.Constants
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

@JvmSuppressWildcards
interface PlantApi {

    @Headers("accept: application/json")
    @POST("identify/all?include-related-images=true&no-reject=false&lang=en&api-key=" + Constants.PLANT_API_KEY)
    suspend fun identifyPlant(@Body images: RequestBody): Response<ApiResponse>

}