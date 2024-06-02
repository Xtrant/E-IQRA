package com.example.e_iqra.data.user

import android.util.Log
import com.example.e_iqra.data.api.ApiConfig
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import okhttp3.MultipartBody

class UserRepository {
    private val db = Firebase.firestore

    fun addUser(
        auth: FirebaseAuth,
        email: String,
        password: String,
        user: User,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userUid = it.user?.uid
                if (userUid != null) {
                    db.collection("users").document(userUid)
                        .set(user)
                        .addOnCompleteListener(onCompleteListener)
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "addUser: ${it.message}")
            }
    }

    fun loginUser(
        auth: FirebaseAuth,
        email: String,
        password: String,
        onCompleteListener: OnCompleteListener<AuthResult>
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(onCompleteListener)
    }

    fun readDataFirestore(
        currentUserId: String,
        onCompleteListener: OnCompleteListener<DocumentSnapshot>

    ) {
        db.collection("users").document(currentUserId)
            .get()
            .addOnCompleteListener(onCompleteListener)
    }

    suspend fun uploadImage(token: String, file: MultipartBody.Part ) {
        val apiConfig = ApiConfig().getApiService(token).uploadImage(file)
    }

    companion object {
        private const val TAG = "UserRepository"
    }
}
