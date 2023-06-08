package com.example.networkretrofit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.networkretrofit.data.response.CustomerReviewsItem
import com.example.networkretrofit.data.response.NetworkResponse
import com.example.networkretrofit.data.response.Restaurant
import com.example.networkretrofit.data.retrofit.ApiConfig
import com.example.networkretrofit.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Tag

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        findRestaurant()

    }

    private fun findRestaurant() {
        showLoading(true)

        val client = ApiConfig.getApiService().getNetwork(RESTAURANT_ID)
        client.enqueue(object : Callback<NetworkResponse> {
            override fun onResponse(
                call: Call<NetworkResponse>,
                response: Response<NetworkResponse>,
            ) {
               showLoading(false)
               if (response.isSuccessful) {
                   val responseBody = response.body()
                        if (responseBody != null) {
                            setNetworkData(responseBody.restaurant)
                            setReviewData(responseBody.restaurant.customerReviews)
                        }
                   } else {
                       Log.e(TAG, "onFailure: ${response.message()}")

                   }
               }


            override fun onFailure(call: Call<NetworkResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        
    }

    private fun setNetworkData(restaurant: Restaurant) {
        binding.tvTitle.text = restaurant.name
        binding.tvDescription.text = restaurant.description

        Glide.with(this@MainActivity)
            .load("https://restaurant-api.dicoding.dev/images/large/${restaurant.pictureId}")
            .into(binding.ivPicture)
    }

    private fun setReviewData(customerReviews: List<CustomerReviewsItem>) {
        val adapter = ReviewAdapter()
        adapter.submitList(customerReviews)

        binding.rvReview.adapter = adapter
        binding.edReview.setText("")

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }


    }
}