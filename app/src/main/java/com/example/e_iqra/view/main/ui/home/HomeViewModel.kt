package com.example.e_iqra.view.main.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_iqra.data.api.ApiConfig
import com.example.e_iqra.data.api.DoaResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _doaList = MutableLiveData<List<DoaResponseItem>>()
    val doaList: LiveData<List<DoaResponseItem>> get() = _doaList

    init {
        fetchDoaList()
    }

    private fun fetchDoaList() {
        val call = ApiConfig.getDoaApiService().getDoaList()
        call.enqueue(object : Callback<List<DoaResponseItem>> {
            override fun onResponse(call: Call<List<DoaResponseItem>>, response: Response<List<DoaResponseItem>>) {
                if (response.isSuccessful) {
                    val doaList = response.body()
                    Log.d("HomeViewModel", "Received Doa List: $doaList")
                    _doaList.postValue(doaList!!)
                } else {
                    Log.e("HomeViewModel", "Failed to receive Doa List: ${response.errorBody()}")
                    _doaList.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<DoaResponseItem>>, t: Throwable) {
                Log.e("HomeViewModel", "Error fetching Doa List", t)
                _doaList.postValue(emptyList())
            }
        })
    }
}