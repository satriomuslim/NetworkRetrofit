package com.example.networkretrofit.data.retrofit

import com.example.networkretrofit.data.response.NetworkResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("detail/{id}")
    fun getNetwork(
        @Path("id") id: String
    ): Call<NetworkResponse>
}