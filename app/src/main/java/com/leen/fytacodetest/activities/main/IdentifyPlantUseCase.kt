package com.leen.fytacodetest.activities.main

import android.content.Context
import android.net.Uri
import com.leen.fytacodetest.networking.PlantApi
import com.leen.fytacodetest.networking.resultdataclasses.Plant
import com.leen.fytacodetest.utils.Constants
import com.leen.fytacodetest.utils.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class IdentifyPlantUseCase(private val plantApi: PlantApi) {

    private lateinit var fileUtil: FileUtil

    sealed class Result {
        class Success(val plant: Plant) : Result()
        class Failure(val error: String) : Result()
    }


    suspend fun identifyPlant(images: RequestBody): Result {
        return withContext(Dispatchers.IO) {
            try {
                val response = plantApi.identifyPlant(images)
                if (response.isSuccessful && response.body() != null) {
                    return@withContext Result.Success(response.body()!!)
                } else {
                    return@withContext Result.Failure(response.errorBody()!!.charStream().readText())
                }
            } catch (exception: Exception) {
                throw exception
            }
        }
    }

    fun buildRequestBody(context: Context, uri: Uri) : RequestBody {
        fileUtil = FileUtil()
        val selectedImageUri = File(fileUtil.getFilePath(context, uri)).path

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "images", selectedImageUri,
                RequestBody.create(
                    MediaType.parse(Constants.IMAGE_FILE_TYPE),
                    File(selectedImageUri)
                ))
            .build()

        return requestBody
    }

}