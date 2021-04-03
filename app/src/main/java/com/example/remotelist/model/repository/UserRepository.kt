package com.example.remotelist.model.repository

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

    private val _currentUserState = MutableStateFlow(UserState())
    val currentUserState: StateFlow<UserState> = _currentUserState.asStateFlow()

    init {
        GlobalScope.launch {
            Firebase
                .auth
                .addAuthStateListener { it: FirebaseAuth ->
                    it.currentUser?.let{
                        launch { _currentUserState.emit(getUserState(it.uid)) }
                    }
                }
        }
    }

    private suspend fun getUserDocumentById(userId: String): DocumentReference = Firebase.firestore
        .collection(USERS)
        .whereEqualTo(USER_ID, userId)
        .getSnapshot()
        ?.first()
        ?.reference
        ?: throw UserNotFoundException()

    private fun getCurrentUserId(): String = Firebase
        .auth
        .currentUser
        ?.uid ?: throw NoUserException()

    suspend fun getCurrentUserDocument(): DocumentReference =
        getUserDocumentById(userId = getCurrentUserId())

    suspend fun getUserState(uid:String) = UserState(userDocumentReference = getUserDocument(uid))

    suspend fun getUserDocument(uid: String): DocumentReference = Firebase.firestore
        .collection(USERS)
        .whereEqualTo(USER_ID, uid)
        .getSnapshot()
        ?.first()
        ?.reference
        ?: throw UserNotFoundException()

    fun createUser(email:String, password:String){
        Firebase.auth.createUserWithEmailAndPassword(email, password)
    }

    fun signInUser(email: String, password: String){
        Firebase.auth.signInWithEmailAndPassword(email, password)
    }

    fun deleteUser(){
        Firebase.auth.currentUser?.delete() ?: throw NoUserException()
    }
}