package com.example.helpme

import com.example.helpme.model.Fall
import com.example.helpme.model.FallData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @GET("ola")
    fun getFall () : Call<Fall>

    @POST("ola")
        fun sendData(@Body fallData: FallData):Call<Fall>
}