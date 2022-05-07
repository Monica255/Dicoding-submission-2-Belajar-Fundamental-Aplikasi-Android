package com.example.aplikasigit2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _detailuser = MutableLiveData<DetailUser>()
    val detailuser: LiveData<DetailUser> = _detailuser

    private val _detailfolloweruser = MutableLiveData<List<User>>()
    val detailfolloweruser: LiveData<List<User>> = _detailfolloweruser

    private val _detailfollowinguser = MutableLiveData<List<User>>()
    val detailfollowinguser: LiveData<List<User>> = _detailfollowinguser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingFollower = MutableLiveData<Boolean>()
    val isLoadingFollower: LiveData<Boolean> = _isLoadingFollower

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing


    var userlogin: String = ""
        set(value) {
            field = value
            fetchDetailtUser()
            fetchDetailtUserFollower()
            fetchDetailtUserFollowing()
        }

    private fun fetchDetailtUser() {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getDetailUser(userlogin, token = TOKEN)
        api.enqueue(object : retrofit2.Callback<DetailUser> {
            override fun onResponse(call: Call<DetailUser>, response: Response<DetailUser>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _detailuser.value = responseBody!!

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun fetchDetailtUserFollower() {
        _isLoadingFollower.value = true
        val api = ApiConfig.getApiService().getAllUserFollower(userlogin, token = TOKEN)
        api.enqueue(object : retrofit2.Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoadingFollower.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _detailfolloweruser.value = responseBody!!
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoadingFollower.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun fetchDetailtUserFollowing() {
        _isLoadingFollowing.value = true
        val api = ApiConfig.getApiService().getAllUserFollowing(userlogin, token = TOKEN)
        api.enqueue(object : retrofit2.Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoadingFollowing.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _detailfollowinguser.value = responseBody!!
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoadingFollowing.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }


    companion object {
        private const val TAG = "DetailViewModel"
        private const val TOKEN = "token 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"
    }

}