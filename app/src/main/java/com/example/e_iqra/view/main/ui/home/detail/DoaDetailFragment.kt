package com.example.e_iqra.view.main.ui.home.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_iqra.databinding.FragmentDoaDetailBinding

class DoaDetailFragment : Fragment() {

    private var _binding: FragmentDoaDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EXTRA_DOA = "extra_doa"
        const val EXTRA_AYAT = "extra_ayat"
        const val EXTRA_LATIN = "extra_latin"
        const val EXTRA_ARTINYA = "extra_artinya"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = ""
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        arguments?.let {
            val doa = it.getString(EXTRA_DOA)
            val ayat = it.getString(EXTRA_AYAT)
            val latin = it.getString(EXTRA_LATIN)
            val artinya = it.getString(EXTRA_ARTINYA)

            binding.textViewDoa.text = doa
            binding.textViewArab.text = ayat
            binding.textViewLatin.text = latin
            binding.textViewTerjemahan.text = artinya
        }

        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
