package com.example.e_iqra.view.main.ui.home.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_iqra.databinding.ActivityDoaDetailBinding

class DoaDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoaDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val doa = intent.getStringExtra(EXTRA_DOA)
        val ayat = intent.getStringExtra(EXTRA_AYAT)
        val latin = intent.getStringExtra(EXTRA_LATIN)
        val artinya = intent.getStringExtra(EXTRA_ARTINYA)

        binding.textViewDoa.text = doa
        binding.textViewArab.text = ayat
        binding.textViewLatin.text = latin
        binding.textViewTerjemahan.text = artinya

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        const val EXTRA_DOA = "extra_doa"
        const val EXTRA_AYAT = "extra_ayat"
        const val EXTRA_LATIN = "extra_latin"
        const val EXTRA_ARTINYA = "extra_artinya"
    }
}
