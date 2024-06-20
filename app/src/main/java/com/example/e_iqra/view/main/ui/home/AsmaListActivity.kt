package com.example.e_iqra.view.main.ui.home

import HomeViewModel
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_iqra.R
import com.example.e_iqra.data.adapter.AsmaAdapter
import com.example.e_iqra.databinding.ActivityAsmaListBinding

class AsmaListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAsmaListBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var asmaAdapter: AsmaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAsmaListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvAsma.layoutManager = LinearLayoutManager(this)
        asmaAdapter = AsmaAdapter(this)
        binding.rvAsma.adapter = asmaAdapter
    }

    private fun observeViewModel() {
        homeViewModel.asmaList.observe(this) { asmaList ->
            asmaList?.let {
                asmaAdapter.submitList(it)
            }
        }
    }

    companion object {
        const val EXTRA_ARAB = "extra_arab"
        const val EXTRA_LATIN = "extra_latin"
        const val EXTRA_INDO = "extra_indo"
    }
}
