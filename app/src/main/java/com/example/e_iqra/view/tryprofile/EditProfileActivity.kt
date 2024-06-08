package com.example.e_iqra.view.tryprofile

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.e_iqra.R
import com.example.e_iqra.data.user.User
import com.example.e_iqra.data.user.UserRepository
import com.example.e_iqra.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var userRepository: UserRepository
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        userRepository = UserRepository()
        auth = Firebase.auth
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getDataProfile()

        binding.updtBtn.setOnClickListener {
            updateFirestore()
            startActivity(Intent(this, TryProfileActivity::class.java))
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
                    Toast.makeText(this, "Success Update Data User", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal Update Data User", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_EMAIL = "extra_email"
    }
}