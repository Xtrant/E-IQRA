package com.example.e_iqra.data.user

import android.util.Log
import com.example.e_iqra.data.api.ApiConfig
import com.example.e_iqra.data.api.FileUploadResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
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
                addtoFirestore(it, user, onCompleteListener )

            }
            .addOnFailureListener {
                Log.d(TAG, "addUser: ${it.message}")
            }
    }

    fun addtoFirestore(
        authResult: AuthResult,
        user: User,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val userUid = authResult.user?.uid
        if (userUid != null) {
            db.collection("users").document(userUid)
                .set(user)
                .addOnCompleteListener(onCompleteListener)
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

    fun addtoFirestoreGoogle(uidGoggle: String, user: User, onCompleteListener: OnCompleteListener<Void>) {
        db.collection("users").document(uidGoggle)
            .set(user)
            .addOnCompleteListener(onCompleteListener)
    }

    suspend fun uploadImage(token: String, file: MultipartBody.Part) : FileUploadResponse {
        val apiConfig = ApiConfig().getApiService(token).uploadImage(file)


        return apiConfig

    }


    companion object {
        private const val TAG = "UserRepository"
    }
}
