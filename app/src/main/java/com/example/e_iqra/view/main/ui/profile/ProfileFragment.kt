package com.example.e_iqra.view.main.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.e_iqra.databinding.FragmentProfileBinding
import com.example.e_iqra.view.LoginActivity
import com.example.e_iqra.view.tryprofile.EditProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var profileViewModel: ProfileViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = Firebase.auth

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        profileViewModel.fetchProfile()

        profileViewModel.name.observe(viewLifecycleOwner) {
            binding.name.text = it
        }

        profileViewModel.email.observe(viewLifecycleOwner) {
            binding.email.text = it
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editProfile.setOnClickListener {
            val name = binding.name.text
            val email = binding.email.text
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            intent.putExtra(EditProfileActivity.EXTRA_NAME, name)
            intent.putExtra(EditProfileActivity.EXTRA_EMAIL, email)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            logout()
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
