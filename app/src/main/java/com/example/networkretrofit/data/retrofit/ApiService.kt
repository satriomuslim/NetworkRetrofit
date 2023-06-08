package com.example.networkretrofit.data.retrofit

import com.example.networkretrofit.data.response.NetworkResponse
import com.example.networkretrofit.data.response.PostReviewResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("detail/{id}")
    fun getNetwork(
        @Path("id") id: String
    ): Call<NetworkResponse>

    @FormUrlEncoded
    @Headers("Authorization: token 12345")
    @POST("review")
    fun postReview(
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("review") review: String
    ): Call<PostReviewResponse>

}