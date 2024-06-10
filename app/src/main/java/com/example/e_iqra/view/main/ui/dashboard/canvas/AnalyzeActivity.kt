package com.example.e_iqra.view.main.ui.dashboard.canvas

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.e_iqra.databinding.ActivityAnalyzeBinding
import java.io.File

class AnalyzeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalyzeBinding

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
                binding.imageViewResult.setImageBitmap(bitmap)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Navigate back when the home button is pressed
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}



