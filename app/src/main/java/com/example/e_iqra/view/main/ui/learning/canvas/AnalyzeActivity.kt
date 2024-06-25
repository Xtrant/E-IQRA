package com.example.e_iqra.view.main.ui.learning.canvas

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.e_iqra.databinding.ActivityAnalyzeBinding
import com.example.e_iqra.view.ViewModelFactory
import java.io.File

class AnalyzeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalyzeBinding
    private val canvasViewModel: CanvasViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            canvasViewModel.analyzeImage(bitmap) { result ->
                binding.tvPredictResult.text = result
            }
        }
    }

    private fun scaleBitmapToImageView(bitmap: Bitmap, imageView: ImageView): Bitmap {
        val imageViewWidth = imageView.width
        val imageViewHeight = imageView.height
        return Bitmap.createScaledBitmap(bitmap, imageViewWidth, imageViewHeight, true)
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
