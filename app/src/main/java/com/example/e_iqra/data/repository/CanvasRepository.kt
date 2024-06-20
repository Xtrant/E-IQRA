package com.example.e_iqra.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
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
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CanvasRepository(private val context: Context) {

    private val userRepository: UserRepository by lazy { UserRepository() }
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun saveBitmapToFile(bitmap: Bitmap): File {
        val imagePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "canvas_image.png")
        try {
            val outputStream = FileOutputStream(imagePath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return imagePath
    }

    suspend fun analyzeImage(bitmap: Bitmap): String {
        var resultPredict = ""
        try {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()
            val requestImageFile = byteArray.toRequestBody("image/jpg".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image", "image.jpg", requestImageFile)

            val firebaseUser = auth.currentUser
            val token = firebaseUser?.getIdToken(false)?.await()?.token.toString()

            val client = userRepository.uploadImage(token, multipartBody)
            resultPredict = client.result
        } catch (e: HttpException) {
            withContext(Dispatchers.Main) {

            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {

            }
        }
        return resultPredict
    }
}
