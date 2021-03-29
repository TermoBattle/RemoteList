package com.example.remotelist.model.repository

import com.example.remotelist.model.LOGIN
import com.example.remotelist.model.USERS
import com.example.remotelist.model.USER_ID
import com.example.remotelist.model.data.UserState
import com.example.remotelist.utils.firebase.NoUserException
import com.example.remotelist.utils.firebase.UserNotFoundException
import com.example.remotelist.utils.firebase.getSnapshot
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class UserRepository @Inject constructor() {

    private val _currentUserLogin = MutableStateFlow("")
    val currentUserLogin: StateFlow<String> = _currentUserLogin.asStateFlow()

    init {
        GlobalScope.launch {
            Firebase
                .auth
                .addAuthStateListener { it: FirebaseAuth ->
                    it.currentUser?.let{
                        launch {
                            _currentUserLogin.emit(
                                value = getLogin(id = it.uid)
                            )
                        }
                    } ?: throw NoUserException()
                }
        }
    }

    private suspend fun getLogin(id:String):String = Firebase
        .firestore
        .collection(USERS)
        .whereEqualTo(USER_ID, id)
        .getSnapshot()
        ?.run{
            first().get(LOGIN, String::class.java)
        } ?: throw UserNotFoundException()

    suspend fun UserState(login: String) =
        UserState(userDocumentReference = getUserDocument(login = login))

    suspend fun getUserDocument(login: String): DocumentReference = Firebase.firestore
        .collection(USERS)
        .whereEqualTo(LOGIN, login)
        .getSnapshot()
        ?.first()
        ?.reference
        ?: throw UserNotFoundException()

    private suspend fun getUserDocumentById(userId: String): DocumentReference = Firebase.firestore
        .collection(USERS)
        .whereEqualTo(USER_ID, userId)
        .getSnapshot()
        ?.first()
        ?.reference
        ?: throw UserNotFoundException()

    suspend fun getCurrentUserDocument(): DocumentReference =
        getUserDocumentById(userId = getCurrentUserId())

    private fun getCurrentUserId(): String = Firebase
        .auth
        .currentUser
        ?.uid ?: throw NoUserException()

}