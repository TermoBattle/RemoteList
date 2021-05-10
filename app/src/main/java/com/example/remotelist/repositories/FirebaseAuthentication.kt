package com.example.remotelist.repositories

import com.example.remotelist.library.firebase.*
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object FirebaseAuthentication {

    suspend fun createUser(email: String, password: String, login: String) {
        val authResult = Firebase
            .auth
            .createUserWithEmailAndPassword(email, password)
            .await() ?: throw RegisterFailedException(message = "register result is null")

        val userId = authResult
            .user
            ?.uid ?: throw RegisterFailedException(message = "[user] is null")

        users.document().apply {
            set(
                hashMapOf(
                    LOGIN to login,
                    FRIENDS to emptyList<String>(),
                    USER_ID to userId
                )
            )
            shoppingList
        }
    }

    fun signOut() {
        Firebase.auth.signOut()
    }

    suspend fun signIn(email: String, password: String): AuthResult = Firebase
        .auth
        .signInWithEmailAndPassword(
            email,
            password
        )
        .await() ?: throw SignInFailedException()


}