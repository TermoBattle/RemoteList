package com.example.remotelist.mvvm.model

import com.example.remotelist.mvvm.model.data.ShopItem
import com.example.remotelist.mvvm.model.data.User
import com.example.remotelist.mvvm.model.data.increased
import com.example.remotelist.mvvm.model.repository.*
import com.example.remotelist.utils.firebase.*
import com.example.remotelist.utils.log
import com.example.remotelist.utils.with
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class Model @Inject constructor(private val currentUserRepo: CurrentUserRepo) {
    var chosenUserLogin = MutableStateFlow("")
    val userLogged = MutableStateFlow(Firebase.auth.currentUser != null)
    val userData: MutableStateFlow<User?> = MutableStateFlow(null)

    suspend fun emitUserLogged(value: Boolean) = userLogged.emit(value = value)

    init {
//        GlobalScope.launch {
//            while (true) {
//                userData.emit(
//                    value = currentUserRepo
//                        .currentUser
//                        .value
//                        ?.copy(
//                            shoppingList = getShoppingListByLogin(chosenUserLogin.value)
//                        )
//                )
//            }
//        }
//        GlobalScope.launch {
//            Firebase.auth.addAuthStateListener {
//                launch { userLogged.emit(it.currentUser != null) }
//            }
//        }
        GlobalScope.launch {
            while (true) {
                log("Try to emit userData")
                userData.emit(
                    value = currentUserRepo
                        .currentUser
                        .value
                        ?.copy(
                            shoppingList = getShoppingListByLogin(chosenUserLogin.value)
                        )
                )
            }
        }
        GlobalScope.launch {
            while (true) {
                log("Try to emit userLogged")
                userLogged.emit(value = Firebase.auth.currentUser != null)
            }
        }
    }

    suspend fun getShoppingList() = chosenUserLogin.value.let { login ->
        if (login.isBlank())
            currentUserRepo.getCurrentUserDocument()
        else
            findUserDocumentByLogin(login = login)
    }.shoppingList

    suspend fun updateItem(shopItem: ShopItem): Void = getShoppingList()
        .findDocument(shopItem.name)
        ?.set(shopItem.increased)
        ?.await()
        ?: throw DocumentNotFoundException()

    suspend fun createItem(maxCount: Int, name: String, description: String) {
        currentUserRepo
            .getCurrentUserDocument()
            .shoppingList
            .add(ShopItem(maxCount, name, description))
    }

    suspend fun deleteItem(shopItem: ShopItem) {
        currentUserRepo
            .getCurrentUserDocument()
            .shoppingList
            .findDocument(shopItem.name)
            ?.delete()
            ?: throw DocumentNotFoundException()
    }

    fun register(login: String, email: String, password: String) {
        createUser(email, password).addOnCompleteListener { task ->
            val userData = hashMapOf(
                LOGIN to login,
                FRIENDS to emptyList<String>(),
                USER_ID to (
                        task
                            .result
                            ?.run {
                                user
                                    ?.uid
                                    ?: throw NoUserException()
                            } ?: throw RegisterFailedException()
                        )
            )

            users.document().let {
                it.set(userData)
                it.shoppingList
            }
        }
    }

    suspend fun signIn(email: String, password: String) {
        signInUser(email, password).addOnCompleteListener { task: Task<AuthResult> ->
            GlobalScope.launch {
                chosenUserLogin
                    .emit(
                        findUserDocumentByUId(
                            uid = task
                                .result
                                ?.user
                                ?.uid
                                ?: throw NoUserException()
                        ).getSnapshot()
                            .login
                    )
            }
        }.addOnFailureListener { throw it }
    }

    suspend fun addFriend(login: String) =
        currentUserRepo.getCurrentUserDocument().let { userDocument: DocumentReference ->

            val friendsList: List<String> = userDocument.getSnapshot().friends

            userDocument.set(
                hashMapOf(FRIENDS to friendsList.with(login))
            )

            Unit
        }

    suspend fun getCurrentFriendList(): List<String> =
        currentUserRepo.getCurrentUserDocument().getSnapshot().friends

    suspend fun getCurrentUserDocument(): DocumentSnapshot = currentUserRepo.getCurrentUserDocumentSnapshot()


}