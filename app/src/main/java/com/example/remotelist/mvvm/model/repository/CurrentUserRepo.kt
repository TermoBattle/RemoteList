package com.example.remotelist.mvvm.model.repository

import com.example.remotelist.mvvm.model.data.User
import com.example.remotelist.mvvm.model.data.getUser
import com.example.remotelist.utils.firebase.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


private fun getCurrentUserID(): String = Firebase
    .auth
    .currentUser
    ?.uid ?: throw NoUserException()


class CurrentUserRepo @Inject constructor() {

    private val _currentUser: MutableStateFlow<User?> = MutableStateFlow(null)

    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        GlobalScope.launch {
            Firebase
                .auth
                .addAuthStateListener {
                    launch {
                        _currentUser.run {
                            emit(
                                value = it.currentUser.run {
                                    if (this == null)
                                        null
                                    else
                                        getUser(uid = uid)
                                }
                            )
                        }
                    }
                }
        }
    }
    suspend fun getCurrentUserDocument(): DocumentReference =
        findUserDocumentByUId(uid = getCurrentUserID())

    suspend fun getCurrentUserDocumentSnapshot(): DocumentSnapshot =
        getCurrentUserDocumentSnapshotByUId(uid = getCurrentUserID())

}

suspend fun getCurrentUserDocumentSnapshotByUId(uid: Any?): DocumentSnapshot {
    val querySnapshot = Firebase.firestore
        .collection(USERS)
        .whereEqualTo(USER_ID, uid)
        .getSnapshot()

    querySnapshot?.let {
        if (it.isEmpty)
            throw UserDocumentEmptyException()
        else
            return it.documents[0]
    } ?: throw UserNotFoundException()
}
