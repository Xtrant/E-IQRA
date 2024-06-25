package com.example.e_iqra.view.main.ui.learning.canvas

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_iqra.data.repository.CanvasRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class CanvasViewModel(private val repository: CanvasRepository) : ViewModel() {

    fun saveBitmapToFile(bitmap: Bitmap): File {
        return repository.saveBitmapToFile(bitmap)
    }

    fun analyzeImage(bitmap: Bitmap, callback: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.analyzeImage(bitmap)
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }
}
