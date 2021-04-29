package com.example.remotelist.mvvm.model.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// -- Authentication -- ///////////////////////////////////////////////////
inline fun createUser(email: String, password: String): Task<AuthResult> =
    Firebase.auth.createUserWithEmailAndPassword(email, password)

inline fun signInUser(email: String, password: String): Task<AuthResult> =
    Firebase.auth.signInWithEmailAndPassword(email, password)

