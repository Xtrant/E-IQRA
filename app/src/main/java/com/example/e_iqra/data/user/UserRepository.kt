package com.example.e_iqra.data.user

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository {
    private val db = Firebase.firestore

    fun addUser(
        auth: FirebaseAuth,
        email: String,
        password: String,
        user: User,
        onCompleteListener: OnCompleteListener<DocumentReference>
    ) {
        auth.createUserWithEmailAndPassword(email, password)
        db.collection("users")
            .add(user)
            .addOnCompleteListener(onCompleteListener)
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
}