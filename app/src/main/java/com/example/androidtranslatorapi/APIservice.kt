package com.example.androidtranslatorapi

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIservice{
    @GET("/0.2/languages")
    suspend fun getLanguages() : Response<List<Language>>

    @Headers("Authorization: Bearer a7f02388d21558c603ab9108bd3a542a")
    @FormUrlEncoded
    @POST("/0.2/detect")
    suspend fun getTextLanguage(@Field("q") text:String):Response<DetectionResponse>
}