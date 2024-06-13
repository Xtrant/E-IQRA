package com.example.e_iqra.view.main.ui.dashboard.canvas

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_iqra.data.user.UserRepository
import com.example.e_iqra.databinding.ActivityAnalyzeBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.File

class AnalyzeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalyzeBinding
    private lateinit var userRepository: UserRepository
    private lateinit var auth: FirebaseAuth
    private var resultPredict: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepository()
        auth = FirebaseAuth.getInstance()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val imagePath = intent.getStringExtra("image_path")
        if (imagePath != null) {
            val imgFile = File(imagePath)
            if (imgFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                binding.imageViewResult.viewTreeObserver.addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            binding.imageViewResult.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            val scaledBitmap = scaleBitmapToImageView(bitmap, binding.imageViewResult)
                            binding.imageViewResult.setImageBitmap(scaledBitmap)
                        }
                    }
                )
            }
        }

        binding.btnAnalyze.setOnClickListener {
            val bitmap = (binding.imageViewResult.drawable as BitmapDrawable).bitmap
            analyzeImage(bitmap)
        }

    }

    private fun scaleBitmapToImageView(bitmap: Bitmap, imageView: ImageView): Bitmap {
        val imageViewWidth = imageView.width
        val imageViewHeight = imageView.height
        return Bitmap.createScaledBitmap(bitmap, imageViewWidth, imageViewHeight, true)
    }

    private fun analyzeImage(bitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        val requestImageFile = byteArray.toRequestBody("image/jpg".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", "image.jpg", requestImageFile)

        // get token user
        val firebaseUser = auth.currentUser
        val token = firebaseUser?.getIdToken(false)?.result?.token.toString()

        //send request to api
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = userRepository.uploadImage(token, multipartBody)
                resultPredict = client.result

                runOnUiThread {
                    // Menampilkan hasil prediksi pada UI AnalyzeActivity
                    binding.tvPredictResult.text = "$resultPredict"
                }
            } catch (e: HttpException) {
                runOnUiThread {
                    Toast.makeText(this@AnalyzeActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}