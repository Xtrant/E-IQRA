package com.example.e_iqra.view.main.ui.dashboard.canvas

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.e_iqra.databinding.ActivityCanvasBinding
import com.example.e_iqra.view.ViewModelFactory

class CanvasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCanvasBinding
    private var bitmapFilePath: String? = null

    private val viewModel: CanvasViewModel by viewModels { ViewModelFactory(applicationContext) }

    companion object {
        private const val KEY_BITMAP_PATH = "bitmap_path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCanvasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val fabDone = binding.fabDone
        val fabRefresh = binding.fabRefresh

        fabDone.setOnClickListener {
            saveImageAndShowResult()
        }

        fabRefresh.setOnClickListener {
            refreshCanvas()
        }

        bitmapFilePath = savedInstanceState?.getString(KEY_BITMAP_PATH)
        bitmapFilePath?.let {
            val bitmap = BitmapFactory.decodeFile(it)
            binding.drawView.setBitmap(bitmap)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_BITMAP_PATH, bitmapFilePath)
    }

    private fun refreshCanvas() {
        binding.drawView.clearCanvas()
    }

    private fun saveImageAndShowResult() {
        val bitmap = binding.drawView.getBitmap()
        val imageFile = viewModel.saveBitmapToFile(bitmap)
        bitmapFilePath = imageFile.absolutePath

        val intent = Intent(this, AnalyzeActivity::class.java)
        intent.putExtra("image_path", bitmapFilePath)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
