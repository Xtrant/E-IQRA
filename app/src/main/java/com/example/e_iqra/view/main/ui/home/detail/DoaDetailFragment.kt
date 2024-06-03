package com.example.e_iqra.view.main.ui.home.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_iqra.databinding.FragmentDoaDetailBinding

class DoaDetailFragment : Fragment() {

    private var _binding: FragmentDoaDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve data from Bundle
        val doa = arguments?.getString("doa")
        val ayat = arguments?.getString("ayat")
        val latin = arguments?.getString("latin")
        val artinya = arguments?.getString("artinya")

        // Display data in the views
        binding.textViewDoa.text = doa
        binding.textViewArab.text = ayat
        binding.textViewLatin.text = latin
        binding.textViewTerjemahan.text = artinya
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

