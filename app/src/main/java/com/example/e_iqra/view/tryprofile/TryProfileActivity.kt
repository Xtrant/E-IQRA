package com.example.e_iqra.view.tryprofile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.e_iqra.R
import com.example.e_iqra.data.user.UserRepository
import com.example.e_iqra.databinding.ActivityTryProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class TryProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTryProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userRepository: UserRepository
    private val name = ""
    private val email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTryProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        userRepository = UserRepository()

        getProfile()

        binding.editProfile.setOnClickListener {
            val name = binding.name.text
            val email = binding.email.text
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra("EXTRA_NAME", name)
            intent.putExtra("EXTRA_EMAIL", email)
            startActivity(intent)
        }
    }

    private fun getProfile() {
        val firebaseUser = auth.currentUser
        val uid = firebaseUser?.uid

        if (uid != null) {
            userRepository.readDataFirestore(uid) {
                if (it.isSuccessful) {
                    binding.name.text = it.result.data?.get("name").toString()
                    binding.email.text = it.result.data?.get("email").toString()
                } else {
                    Log.d("TryProfileActivity", "getProfile: ${it.exception}")
                }

            }
        }
    }
}