package com.example.e_iqra.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.e_iqra.R
import com.example.e_iqra.data.user.User
import com.example.e_iqra.data.user.UserRepository
import com.example.e_iqra.databinding.ActivityRegisterBinding
import com.example.e_iqra.view.main.MainActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userRepository: UserRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userRepository = UserRepository()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        playAnimation()
        setMyButtonEnable()

        binding.etName.addTextChangedListener(MyTextWatcher())
        binding.etEmail.addTextChangedListener(MyTextWatcher())
        binding.etPass.addTextChangedListener(MyTextWatcher())

        binding.customBtn.setOnClickListener {
            register()
        }

        binding.tvClickable.setOnClickListener {
            startActivity(Intent(this, LoginActivity:: class.java))
        }

        binding.googleBtn.setOnClickListener {
            signInGoogle()
        }
    }

    private fun register() {
        val email = binding.etEmail.text.toString().trim()
        val name = binding.etName.text.toString().trim()
        val password = binding.etPass.text.toString().trim()

        val user = User(email = email, name = name)

        userRepository.addUser(
            auth, email, password, user) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully Create Account", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                Toast.makeText(this, "Error Create Account", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInGoogle() {
        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.your_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = this@RegisterActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {

        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun setMyButtonEnable() {
        val etEmail = binding.etEmail.text
        val etName = binding.etName.text
        val etPassword = binding.etPass.text
        binding.customBtn.isEnabled = etEmail != null && etEmail.toString().isNotEmpty() && etName != null && etName.toString().isNotEmpty() && etPassword != null && etPassword.toString().isNotEmpty() && etPassword.toString().length >= 8 && Patterns.EMAIL_ADDRESS.matcher(etEmail).matches()
    }

    private fun isEmailValid() {
        val valid = binding.etEmail.text?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() }
        if (!valid!!) {
            binding.etEmail.setError("Invalid Input Email", null)
        }
        else
            binding.etEmail.setError(null, null)
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivMain, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleEmail = ObjectAnimator.ofFloat(binding.tvTitleEmail, View.ALPHA, 1f).setDuration(200)
        val editTextEmail = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(200)
        val titleName = ObjectAnimator.ofFloat(binding.tvTitleName, View.ALPHA, 1f).setDuration(200)
        val editTextName = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, 1f).setDuration(200)
        val titlePass = ObjectAnimator.ofFloat(binding.tvTitlePassword, View.ALPHA, 1f).setDuration(200)
        val editTextPass = ObjectAnimator.ofFloat(binding.etPass, View.ALPHA, 1f).setDuration(200)
        val customButton = ObjectAnimator.ofFloat(binding.customBtn, View.ALPHA, 1f).setDuration(200)
        val sigInGoogleButton =
            ObjectAnimator.ofFloat(binding.googleBtn, View.ALPHA, 1f).setDuration(200)
        val instruction =
            ObjectAnimator.ofFloat(binding.tvInstruction, View.ALPHA, 1f).setDuration(200)
        val instructionLink =
            ObjectAnimator.ofFloat(binding.tvClickable, View.ALPHA, 1f).setDuration(200)

        val instructionTogether =  AnimatorSet().apply {
            playTogether(instruction, instructionLink)
        }

        AnimatorSet().apply {
            playSequentially(titleEmail, editTextEmail, titleName, editTextName, titlePass, editTextPass, customButton, sigInGoogleButton, instructionTogether)
            start()
        }
    }
    inner class MyTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setMyButtonEnable()
        }

        override fun afterTextChanged(s: Editable?) {
            isEmailValid()
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val uidGoogle = currentUser.uid
            val name = currentUser.displayName
            val email = currentUser.email

            if (name != null && email != null) {
                val user = User(email = email, name = name)
                userRepository.addtoFirestoreGoogle(uidGoogle, user) {
                    if (it.isSuccessful) {
                        Log.d(TAG, "Google Account Success added to Firestore")
                    } else {
                        Log.d(TAG, "Google Account Failed added to Firestore")
                    }
                }
            }

            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            finish()
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}