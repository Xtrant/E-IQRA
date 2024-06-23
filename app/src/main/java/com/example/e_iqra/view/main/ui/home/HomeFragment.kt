package com.example.e_iqra.view.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_iqra.R
import com.example.e_iqra.data.adapter.DoaAdapter
import com.example.e_iqra.data.adapter.SlideAdapter
import com.example.e_iqra.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var doaAdapter: DoaAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var slideAdapter: SlideAdapter

    private val initialListSize = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDoa.layoutManager = LinearLayoutManager(context)
        doaAdapter = DoaAdapter(requireContext())
        binding.rvDoa.adapter = doaAdapter

        binding.subtitle.text = getString(R.string.learn_doa)
        binding.seeAllButton.setOnClickListener {
            val intent = Intent(context, DoaListActivity::class.java)
            startActivity(intent)
        }
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        homeViewModel.doaList.observe(viewLifecycleOwner) { doaList ->
            doaList?.let {
                doaAdapter.submitList(it.take(initialListSize))
            }
        }

        homeViewModel.descriptions.observe(viewLifecycleOwner) { descriptions ->
            homeViewModel.buttonTexts.observe(viewLifecycleOwner) { buttonTexts ->
                homeViewModel.slideImages.observe(viewLifecycleOwner) { slideImages ->
                    if (descriptions != null && buttonTexts != null && slideImages != null) {
                        slideAdapter = SlideAdapter(descriptions, buttonTexts, slideImages) { position ->
                            homeViewModel.onSlideButtonClicked(position, requireContext())
                        }
                        binding.vpSlider.adapter = slideAdapter
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
