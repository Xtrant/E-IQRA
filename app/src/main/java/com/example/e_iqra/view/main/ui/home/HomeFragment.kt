package com.example.e_iqra.view.main.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_iqra.data.adapter.DoaAdapter
import com.example.e_iqra.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var doaAdapter: DoaAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        binding.rvDoa.layoutManager = LinearLayoutManager(context)
        doaAdapter = DoaAdapter(mutableListOf())
        binding.rvDoa.adapter = doaAdapter

        // Set up ViewModel and observe data
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.doaList.observe(viewLifecycleOwner, Observer { doaList ->
            doaList?.let {
                Log.d("HomeFragment", "Received Doa List: $it")
                doaAdapter.setDoaList(it)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}