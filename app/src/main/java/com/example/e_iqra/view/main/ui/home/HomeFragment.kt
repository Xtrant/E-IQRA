package com.example.e_iqra.view.main.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_iqra.R
import com.example.e_iqra.data.adapter.DoaAdapter
import com.example.e_iqra.data.adapter.SlideAdapter
import com.example.e_iqra.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var doaAdapter: DoaAdapter
    private lateinit var searchAdapter: DoaAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var slideAdapter: SlideAdapter

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

        searchAdapter = DoaAdapter(requireContext())
        binding.searchView.findViewById<RecyclerView>(R.id.rv_search).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    Toast.makeText(requireContext(), searchView.text, Toast.LENGTH_SHORT).show()
                    false
                }

            searchView.editText.setOnFocusChangeListener { view, hasFocus ->
                val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
                if (hasFocus) {
                    bottomNavigationView?.visibility = View.GONE
                } else {
                    bottomNavigationView?.visibility = View.VISIBLE
                }
            }

            searchView.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    homeViewModel.searchDoa(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.doaList.observe(viewLifecycleOwner, Observer { doaList ->
            doaList?.let {
                doaAdapter.submitList(it)
            }
        })

        homeViewModel.filteredDoaList.observe(viewLifecycleOwner, Observer { doaList ->
            doaList?.let {
                if (it.isEmpty()) {
                    binding.searchView.findViewById<RecyclerView>(R.id.rv_search).visibility = View.GONE
                } else {
                    binding.searchView.findViewById<RecyclerView>(R.id.rv_search).visibility = View.VISIBLE
                    searchAdapter.submitList(it)
                }
            }
        })

        homeViewModel.slides.observe(viewLifecycleOwner, Observer { slides ->
            homeViewModel.descriptions.observe(viewLifecycleOwner, Observer { descriptions ->
                homeViewModel.buttonTexts.observe(viewLifecycleOwner, Observer { buttonTexts ->
                    if (slides != null && descriptions != null && buttonTexts != null) {
                        slideAdapter = SlideAdapter(slides, descriptions, buttonTexts) { position ->
                            homeViewModel.onSlideButtonClicked(position, requireContext())
                        }
                        binding.vpSlider.adapter = slideAdapter
                    }
                })
            })
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
