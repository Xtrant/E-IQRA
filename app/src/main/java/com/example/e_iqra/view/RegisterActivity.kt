package com.example.e_iqra.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.e_iqra.R
import com.example.e_iqra.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
            startActivity(Intent(this, LoginActivity::class.java))
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

        val titleEmail = ObjectAnimator.ofFloat(binding.tvTitleEmail, View.ALPHA, 1f).setDuration(100)
        val editTextEmail = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(100)
        val titleName = ObjectAnimator.ofFloat(binding.tvTitleName, View.ALPHA, 1f).setDuration(100)
        val editTextName = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, 1f).setDuration(100)
        val titlePass = ObjectAnimator.ofFloat(binding.tvTitlePassword, View.ALPHA, 1f).setDuration(100)
        val editTextPass = ObjectAnimator.ofFloat(binding.etPass, View.ALPHA, 1f).setDuration(100)
        val customButton = ObjectAnimator.ofFloat(binding.customBtn, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(titleEmail, editTextEmail, titleName, editTextName, titlePass, editTextPass, customButton)
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
}