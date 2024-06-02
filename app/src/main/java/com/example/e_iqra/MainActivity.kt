package com.example.e_iqra

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.example.e_iqra.data.user.UserRepository
import com.example.e_iqra.databinding.ActivityMainBinding
import com.example.e_iqra.view.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userRepository: UserRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        userRepository = UserRepository()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                val credentialManager = CredentialManager.create(this@MainActivity)

                auth.signOut()
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
        }

        @SuppressLint("SetTextI18n")
        if (firebaseUser == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        } else {

            val currentUserId = firebaseUser.uid
            userRepository.readDataFirestore(currentUserId) {
                if (it.isSuccessful) {
                    val name = it.result.data?.get("name").toString()
                    binding.tvGreeting.text = "Hi $name"
                }

            }
            auth.currentUser?.getIdToken(true)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    val token = it.result.token
                    Log.d(TAG, "login berhasil cok: $token")

                } else {
                    Log.d(TAG, "loginFailure: ${it.exception} ")
                }
            }
        }
    }

    private fun uploadImage() {

    }

        companion object {
            private const val TAG = "MainActivity"
        }
}



