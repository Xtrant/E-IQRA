package com.example.e_iqra.data.user

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository {
    private val db = Firebase.firestore

    fun addUser(user: User, onCompleteListener: OnCompleteListener<DocumentReference>) {
        db.collection("users")
            .add(user)
            .addOnCompleteListener(onCompleteListener)
    }
}