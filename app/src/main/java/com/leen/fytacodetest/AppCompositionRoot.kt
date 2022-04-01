package com.leen.fytacodetest

import androidx.annotation.UiThread
import com.leen.fytacodetest.activities.main.IdentifyPlantUseCase
import com.leen.fytacodetest.networking.PlantApi
import com.leen.fytacodetest.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@UiThread
class AppCompositionRoot  {

    //init retrofit by lazy so it will not be initialized everytime we make an api call
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    private val plantApi: PlantApi by lazy {
        retrofit.create(PlantApi::class.java)
    }


    val identifyPlantUseCase get() = IdentifyPlantUseCase(plantApi)

}