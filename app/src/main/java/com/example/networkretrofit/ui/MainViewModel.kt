package com.example.networkretrofit.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.networkretrofit.data.response.CustomerReviewsItem
import com.example.networkretrofit.data.response.NetworkResponse
import com.example.networkretrofit.data.response.PostReviewResponse
import com.example.networkretrofit.data.response.Restaurant
import com.example.networkretrofit.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.networkretrofit.utils.Event

class MainViewModel : ViewModel() {

    private val _restaurant = MutableLiveData<Restaurant>()
    val restaurant: LiveData<Restaurant> = _restaurant

    private val _listReview = MutableLiveData<List<CustomerReviewsItem>>()
    val listReviews: LiveData<List<CustomerReviewsItem>> = _listReview

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    companion object {
        private const val TAG = "MainViewModel"
        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }

    init {
        findRestaurant()
    }

    private fun findRestaurant() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getNetwork(RESTAURANT_ID)
        client.enqueue(object : Callback<NetworkResponse> {
            override fun onResponse(
                call: Call<NetworkResponse>,
                response: Response<NetworkResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _restaurant.value = response.body()?.restaurant
                    _listReview.value = response.body()?.restaurant?.customerReviews
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")

                }
            }

            override fun onFailure(call: Call<NetworkResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

   fun postReview(review: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Johannes", review)
        client.enqueue(object : Callback<PostReviewResponse> {
            override fun onResponse(
                call: Call<PostReviewResponse>,
                response: Response<PostReviewResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listReview.value = response.body()?.customerReviews
                    _snackbarText.value = Event(response.body()?.message.toString())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG,"onFailure: ${t.message.toString()}")
            }

        })
    }

}