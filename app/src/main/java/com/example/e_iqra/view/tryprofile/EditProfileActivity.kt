package com.example.e_iqra.view.tryprofile

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.e_iqra.R
import com.example.e_iqra.data.user.User
import com.example.e_iqra.data.user.UserRepository
import com.example.e_iqra.databinding.ActivityEditProfileBinding
import com.example.e_iqra.view.main.MainActivity
import com.example.e_iqra.view.main.ui.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var userRepository: UserRepository
    private lateinit var auth: FirebaseAuth
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        userRepository = UserRepository()
        auth = Firebase.auth

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getDataProfile()

        binding.updtBtn.setOnClickListener {
            updateFirestore()
        }
    }

    private fun getDataProfile() {
        val editableNameUser = SpannableStringBuilder(intent.getStringExtra(EXTRA_NAME))
        val editableEmailUser = SpannableStringBuilder(intent.getStringExtra(EXTRA_EMAIL))

        binding.etName.text = editableNameUser
        binding.etEmail.text = editableEmailUser
    }

    private fun updateFirestore() {
        val newEmail = binding.etEmail.text.toString()
        val newName = binding.etName.text.toString()
        val uid = auth.currentUser?.uid
        val user = User(email = newEmail, name = newName)
        if (uid != null) {
            userRepository.addtoFirestoreGoogle(uid, user) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Data Pengguna Diperbarui", Toast.LENGTH_SHORT).show()
                    profileViewModel.fetchProfile()
                    navigateBackToNotificationsFragment()
                } else {
                    Toast.makeText(this, "Gagal Update Data User", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateBackToNotificationsFragment() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_EMAIL = "extra_email"
    }
}
