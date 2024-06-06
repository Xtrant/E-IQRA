package com.example.e_iqra.view.main.ui.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.e_iqra.databinding.FragmentNotificationsBinding
import com.example.e_iqra.view.LoginActivity
import com.example.e_iqra.view.tryprofile.TryProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class   NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private lateinit var auth : FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = Firebase.auth

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener {
            logout()
        }

        binding.toProfileBtn.setOnClickListener {
            startActivity(Intent(context, TryProfileActivity::class.java))
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            val context = requireContext()
            val credentialManager = CredentialManager.create(context)

            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())

            val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()

            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}