package com.example.e_iqra.view.main.ui.home

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_iqra.R
import com.example.e_iqra.data.api.ApiConfig
import com.example.e_iqra.data.api.DoaResponseItem
import com.example.e_iqra.view.main.ui.learning.canvas.CanvasActivity
import com.example.e_iqra.view.main.ui.quiz.QuizActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _doaList = MutableLiveData<List<DoaResponseItem>>()
    val doaList: LiveData<List<DoaResponseItem>> get() = _doaList

    private val _slideImages = MutableLiveData<List<Int>>()
    val slideImages: LiveData<List<Int>> get() = _slideImages

    private val _descriptions = MutableLiveData<List<String>>()
    val descriptions: LiveData<List<String>> get() = _descriptions

    private val _buttonTexts = MutableLiveData<List<String>>()
    val buttonTexts: LiveData<List<String>> get() = _buttonTexts

    init {
        fetchDoaList()
        updateSlides()
    }

    private fun fetchDoaList() {
        ApiConfig.getDoaApiService().getDoaList().enqueue(object : Callback<List<DoaResponseItem>> {
            override fun onResponse(call: Call<List<DoaResponseItem>>, response: Response<List<DoaResponseItem>>) {
                if (response.isSuccessful) {
                    _doaList.postValue(response.body() ?: emptyList())
                } else {
                    _doaList.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<DoaResponseItem>>, t: Throwable) {
                _doaList.postValue(emptyList())
            }
        })
    }

    private fun updateSlides() {
        _descriptions.postValue(getDescriptions())
        _buttonTexts.postValue(getButtonTexts())
        _slideImages.postValue(getSlideImages())
    }

    private fun getSlideImages(): List<Int> {
        return listOf(
            R.drawable.drawing,
            R.drawable.quiz
        )
    }

    fun onSlideButtonClicked(position: Int, context: Context) {
        val intent = when (position) {
            0 -> Intent(context, CanvasActivity::class.java)
            1 -> Intent(context, QuizActivity::class.java)
            else -> null
        }
        intent?.let { context.startActivity(it) }
    }

    private fun getDescriptions(): List<String> {
        return listOf(
            "Pelajari secara langsung tentang huruf Hijaiyah",
            "Evaluasi pengetahuan anda"
        )
    }

    private fun getButtonTexts(): List<String> {
        return listOf(
            "Coba Sekarang",
            "Coba Sekarang"
        )
    }
}