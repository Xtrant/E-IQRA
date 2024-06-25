package com.example.e_iqra.view.main.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_iqra.data.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val auth: FirebaseAuth = Firebase.auth
    private val userRepository: UserRepository = UserRepository()

    fun fetchProfile() {
        val firebaseUser = auth.currentUser
        val uid = firebaseUser?.uid

        if (uid != null) {
            userRepository.readDataFirestore(uid) {
                if (it.isSuccessful) {
                    _name.value = it.result.data?.get("name").toString()
                    _email.value = it.result.data?.get("email").toString()
                } else {
                    // Handle the error here if needed
                }
            }
        }
    }
}
