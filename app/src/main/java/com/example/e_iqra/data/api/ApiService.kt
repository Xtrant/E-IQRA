package com.example.e_iqra.data.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/v1/predict")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part

    ): FileUploadResponse

    @GET("api")
    fun getDoaList(
    ): Call<List<DoaResponseItem>>
}