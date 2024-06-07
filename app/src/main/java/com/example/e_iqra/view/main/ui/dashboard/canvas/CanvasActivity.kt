package com.example.e_iqra.view.main.ui.dashboard.canvas

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.e_iqra.R
import com.example.e_iqra.databinding.ActivityCanvasBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CanvasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCanvasBinding
    private var isFullScreen = false
    private var bitmapFilePath: String? = null

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

        val fabToggleSize = binding.fabToggleSize
        val fabDone = binding.fabDone
        val fabRefresh = binding.fabRefresh

        updateFabIcon()

        fabToggleSize.setOnClickListener {
            toggleCanvasSize()
        }

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

    private fun toggleCanvasSize() {
        if (isFullScreen) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(true)
                window.insetsController?.show(WindowInsets.Type.systemBars())
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
            binding.toolbar.visibility = View.VISIBLE
            isFullScreen = false
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(false)
                window.insetsController?.hide(WindowInsets.Type.systemBars())
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
            binding.toolbar.visibility = View.GONE
            isFullScreen = true
        }
        updateFabIcon()
    }

    private fun updateFabIcon() {
        val fabToggleSize = binding.fabToggleSize
        if (isFullScreen) {
            fabToggleSize.setImageResource(R.drawable.ic_fullscreen_exit_24)
        } else {
            fabToggleSize.setImageResource(R.drawable.ic_fullscreen_24)
        }
    }

    private fun refreshCanvas() {
        binding.drawView.clearCanvas()
    }

    private fun saveImageAndShowResult() {
        val bitmap = binding.drawView.getBitmap()
        val imageFile = saveBitmapToFile(bitmap)
        bitmapFilePath = imageFile.absolutePath

        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("image_path", bitmapFilePath)
        startActivity(intent)
    }

    private fun saveBitmapToFile(bitmap: Bitmap): File {
        val imagePath = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "canvas_image.png")
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
