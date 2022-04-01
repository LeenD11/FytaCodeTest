package com.leen.fytacodetest.activities.main

import android.content.Context
import android.net.Uri
import com.leen.fytacodetest.networking.PlantApi
import com.leen.fytacodetest.networking.resultdataclasses.ApiResponse
import com.leen.fytacodetest.utils.Constants
import com.leen.fytacodetest.utils.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File

class IdentifyPlantUseCase(private val plantApi: PlantApi) {

    private lateinit var fileUtil: FileUtil
    private lateinit var jsonObject: JSONObject

    //initialize Result class to refer to in api request
    sealed class Result {
        class Success(val apiResponse: ApiResponse) : Result()
        class Failure(val error: String) : Result()
    }

    suspend fun identifyPlant(images: RequestBody): Result {
        //make api call using coroutines
        return withContext(Dispatchers.IO) {
            try {
                val response = plantApi.identifyPlant(images)
                //return response body if result is success
                if (response.isSuccessful && response.body() != null) {
                    return@withContext Result.Success(response.body()!!)
                } else {
                    //return error body message when error is found in request
                    jsonObject = JSONObject(response.errorBody()!!.string())
                    val userMessage: String = jsonObject.getString("message")
                    return@withContext Result.Failure(userMessage)
                }
            } catch (exception: Exception) {
                throw exception
            }
        }
    }

    //this function builds the multipart body for the api request
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