package com.example.e_iqra.data.user

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository {
    private val db = Firebase.firestore

    fun addUser(
        auth: FirebaseAuth,
        email: String,
        password: String,
        user: User,
        onCompleteListener: OnCompleteListener<Void>
    ) { //TODO create listener for success and failed
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
//            .addOnSuccessListener {
//                auth.currentUser?.getIdToken(true)?.addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        val token = it.result.token
//                        Log.d(LoginActivity.TAG, "login: berhasil jancoek: $token")
//                        eyJhbGciOiJSUzI1NiIsImtpZCI6IjVkNjE3N2E5Mjg2ZDI1Njg0NTI2OWEzMTM2ZDNmNjY0MjZhNGQ2NDIiLCJ0eXAiOiJKV1QifQ
//                    } else {
//                        Log.d(LoginActivity.TAG, "loginFailure: ")
//                    }
//                }
//            }
//            .addOnFailureListener {
//                Log.d(LoginActivity.TAG, "loginFailure: ${it.message} ")
//            }
    }

    fun readDataFirestore(
        currentUserId: String,
        onCompleteListener: OnCompleteListener<DocumentSnapshot>

    ) {
        db.collection("users").document(currentUserId)
            .get()
            .addOnCompleteListener(onCompleteListener)
    }

    companion object {
        private val TAG = "UserRepository"
    }
}
