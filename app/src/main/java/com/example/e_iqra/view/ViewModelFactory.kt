package com.example.e_iqra.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_iqra.data.repository.CanvasRepository
import com.example.e_iqra.data.repository.QuizRepository
import com.example.e_iqra.view.main.ui.dashboard.canvas.CanvasViewModel
import com.example.e_iqra.view.main.ui.quiz.QuizViewModel

class ViewModelFactory(private val context: Context): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CanvasViewModel::class.java) -> {
                CanvasViewModel(repository = CanvasRepository(context)) as T
            }
            modelClass.isAssignableFrom(QuizViewModel::class.java) -> {
                QuizViewModel(quizRepository = QuizRepository(context)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}