package com.example.e_iqra.view.main.ui.dashboard.canvas

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.e_iqra.R
import com.example.e_iqra.databinding.FragmentCanvasBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class CanvasFragment : Fragment() {

    private var _binding: FragmentCanvasBinding? = null
    private val binding get() = _binding!!
    private var isFullScreen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCanvasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val fabToggleSize = binding.fabToggleSize

        // Set initial icon
        updateFabIcon()

        fabToggleSize.setOnClickListener {
            toggleCanvasSize()
        }

        return root
    }

    private fun toggleCanvasSize() {

        if (isFullScreen) {
            // Exit full screen
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity?.window?.setDecorFitsSystemWindows(true)
                activity?.window?.insetsController?.show(WindowInsets.Type.systemBars())
            } else {
                @Suppress("DEPRECATION")
                activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                @Suppress("DEPRECATION")
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
            isFullScreen = false
        } else {
            // Enter full screen
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity?.window?.setDecorFitsSystemWindows(false)
                activity?.window?.insetsController?.hide(WindowInsets.Type.systemBars())
            } else {
                @Suppress("DEPRECATION")
                activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                @Suppress("DEPRECATION")
                activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
            isFullScreen = true
        }
        updateFabIcon()
    }

    private fun updateFabIcon() {
        val fabToggleSize = binding.fabToggleSize
        if (isFullScreen) {
            fabToggleSize.setImageResource(R.drawable.ic_fullscreen_exit_24)
        } else {
            fabToggleSize.setImageResource(R.drawable.ic_fullscreen_24)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
