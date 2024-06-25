package com.example.e_iqra.view.main.ui.quiz

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_iqra.data.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(private val quizRepository: QuizRepository) : ViewModel() {

    fun getBitmapFromView(view: View): Bitmap {
        return quizRepository.getBitmapFromView(view)
    }

    fun checkDrawingWithMLModel(bitmap: Bitmap, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isCorrect = quizRepository.checkDrawingWithMLModel(bitmap)
            onResult(isCorrect)
        }
    }

    fun getQuestionSummary(
        totalQuestions: Int,
        questions: Array<String>,
        options: Array<Array<String>>,
        userAnswers: IntArray,
        correctAnswers: IntArray
    ): List<String> {
        return quizRepository.getQuestionSummary(totalQuestions, questions, options, userAnswers, correctAnswers)
    }
}
