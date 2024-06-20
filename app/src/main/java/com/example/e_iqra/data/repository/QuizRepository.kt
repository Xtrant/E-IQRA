package com.example.e_iqra.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import com.example.e_iqra.data.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.ByteArrayOutputStream

class QuizRepository(private val context: Context) {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val userRepository: UserRepository by lazy { UserRepository() }

    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    suspend fun checkDrawingWithMLModel(bitmap: Bitmap): Boolean {
        var isCorrect = false
        try {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            val firebaseUser = auth.currentUser
            val token = firebaseUser?.getIdToken(false)?.await()?.token.toString()

            val requestImageFile = byteArray.toRequestBody("image/png".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image", "image.png", requestImageFile)

            val client = userRepository.uploadImage(token, multipartBody)

            val confidenceScore = client.confidence

            isCorrect = confidenceScore > "70.0"

        } catch (e: HttpException) {
            withContext(Dispatchers.Main) {
                // Handle HTTP exception
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                // Handle general exception
            }
        }
        return isCorrect
    }

    fun getQuestionSummary(
        totalQuestions: Int,
        questions: Array<String>,
        options: Array<Array<String>>,
        userAnswers: IntArray,
        correctAnswers: IntArray
    ): List<String> {
        val summaryList = mutableListOf<String>()
        for (i in 0 until totalQuestions) {
            val question = questions[i]
            val userAnswer = if (userAnswers[i] == -1) "No answer provided" else options[i][userAnswers[i]]
            val correctAnswer = if (correctAnswers[i] == -1) "Canvas question" else options[i][correctAnswers[i]]

            val summary = "Question ${i + 1}: $question\nYour answer: $userAnswer\nCorrect answer: $correctAnswer\n"
            summaryList.add(summary)
        }
        return summaryList
    }
}
