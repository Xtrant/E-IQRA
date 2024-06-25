package com.example.e_iqra.view.main.ui.learning

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.e_iqra.databinding.FragmentLearnBinding
import com.example.e_iqra.view.main.ui.learning.canvas.CanvasActivity
import com.example.e_iqra.view.main.ui.quiz.QuizActivity

class LearnFragment : Fragment() {

    private var _binding: FragmentLearnBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearnBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
    }

    private fun setupAction() {
        binding.canvasButton.setOnClickListener {
            val intent = Intent(requireContext(), CanvasActivity::class.java)
            startActivity(intent)
        }

        binding.quizButton.setOnClickListener {
            val intent = Intent(requireContext(), QuizActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
