package com.example.e_iqra.view.tryapipredict

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.e_iqra.R
import com.example.e_iqra.data.api.ApiConfig
import com.example.e_iqra.data.api.FileUploadResponse
import com.example.e_iqra.data.user.UserRepository
import com.example.e_iqra.databinding.ActivityTryApiPredictBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class TryApiPredict : AppCompatActivity() {
    private lateinit var binding: ActivityTryApiPredictBinding
    private var currentImageUri: Uri? = null
    private lateinit var userRepository: UserRepository
    private lateinit var auth: FirebaseAuth
    private var resultPredict: String? = null
    private var confidencePredict: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTryApiPredictBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userRepository = UserRepository()
        auth = Firebase.auth

        binding.galleryButton.setOnClickListener {
            openGallery()
        }

        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                val imageFile = uriToFile(it, this)
                analyzeImage(imageFile)
            }
                ?: Toast.makeText(this, "Gada Gambarnya Woi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage(imageFile: File) {
        val requestImageFile = imageFile.asRequestBody("image/jpg".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )

        // get token user
        val firebaseUser = auth.currentUser
        val token = firebaseUser?.getIdToken(false)?.result?.token.toString()

        //send request to api
        lifecycleScope.launch {
            try {
                val client = userRepository.uploadImage(token, multipartBody)
                resultPredict = client.result
                confidencePredict = client.confidence
                val intent = Intent(this@TryApiPredict, ResultActivity::class.java)
                intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
                intent.putExtra(ResultActivity.EXTRA_RESULT, resultPredict)
                intent.putExtra(ResultActivity.EXTRA_CONFIDENCE, confidencePredict)
                startActivity(intent)
            } catch (e: HttpException) {
                Log.d("Coba api", "analyzeImage: $e")
            }


        }



    }

    private fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }

}