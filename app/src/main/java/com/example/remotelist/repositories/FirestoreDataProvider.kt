package com.example.remotelist.repositories

import com.example.remotelist.library.data_structures.ShopItem
import com.example.remotelist.library.firebase.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object FirestoreDataProvider {

    private val _chosenUserLogin = MutableStateFlow("")
    val chosenUserLogin = _chosenUserLogin.asStateFlow()

    private val _logged = MutableStateFlow(Firebase.auth.currentUser != null)
    val logged = _logged.asStateFlow()

    private val _email = MutableStateFlow(Firebase.auth.currentUser?.email ?: "")
    val email = _email.asStateFlow()

    private val _login = MutableStateFlow("")
    val login = _login.asStateFlow()

    private val _shoppingList = MutableStateFlow(emptyList<ShopItem>())
    val shoppingList = _shoppingList.asStateFlow()

    private val _friends = MutableStateFlow(emptyList<String>())
    val friends = _friends.asStateFlow()

    private val _friendsCount = MutableStateFlow(0)
    val friendsCount = _friendsCount.asStateFlow()

    suspend fun refresh() {
        val snapshot = getCurrentUserDocument()

        _logged.emit(
            value = Firebase.auth.currentUser != null
        )
        _email.emit(
            value = Firebase.auth.currentUser?.email ?: ""
        )
        _login.emit(
            value = snapshot.login
        )
        _shoppingList.emit(
            value = snapshot.shoppingList
        )
        _friends.emit(
            value = snapshot.friends
        )
        _friendsCount.emit(
            value = friends.value.size
        )
    }

    suspend fun getShoppingListReference(): CollectionReference = chosenUserLogin.value.let { login ->
        if (login.isBlank())
            getCurrentUserDocumentReference()
        else
            findUserDocumentByLogin(login = login)
    }.shoppingList

    suspend fun emitNewLogin(login: String) {
        _chosenUserLogin
            .emit(login)
    }

    private val currentUserID: String
        inline get() = Firebase
            .auth
            .currentUser
            ?.uid ?: throw NoUserException()

    private suspend fun getCurrentUserDocumentSnapshotByUId(uid: Any?): DocumentSnapshot {
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

    suspend fun getCurrentUserDocumentReference(): DocumentReference =
        findUserDocumentByUId(uid = currentUserID)

    private suspend fun getCurrentUserDocumentSnapshot(): DocumentSnapshot =
        getCurrentUserDocumentSnapshotByUId(uid = currentUserID)


    private suspend fun getCurrentUserDocument(): DocumentSnapshot = getCurrentUserDocumentSnapshot()

    suspend fun findUserDocumentByUId(uid: String): DocumentReference {
        val querySnapshot = users
            .whereEqualTo(USER_ID, uid)
            .getSnapshot()

        querySnapshot?.let { snapshot ->
            if (snapshot.isEmpty)
                throw UserDocumentEmptyException()
            else
                return snapshot.documents[0].reference
        } ?: throw UserNotFoundException()
    }

    private suspend fun findUserDocumentByLogin(login: String): DocumentReference = users
        .whereEqualTo(LOGIN, login)
        .getSnapshot()
        ?.first()
        ?.reference ?: throw UserNotFoundException()
}