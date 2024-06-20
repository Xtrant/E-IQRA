package com.example.e_iqra.view.main.ui.home

import HomeViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_iqra.data.adapter.DoaAdapter
import com.example.e_iqra.databinding.ActivityDoaListBinding

class DoaListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoaListBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var doaAdapter: DoaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoaListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvDoa.layoutManager = LinearLayoutManager(this)
        doaAdapter = DoaAdapter(this)
        binding.rvDoa.adapter = doaAdapter
    }

    private fun observeViewModel() {
        homeViewModel.doaList.observe(this) { doaList ->
            doaList?.let {
                doaAdapter.submitList(it)
            }
        }
    }
}
