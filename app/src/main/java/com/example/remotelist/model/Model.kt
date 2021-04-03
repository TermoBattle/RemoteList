package com.example.remotelist.model

import com.example.remotelist.model.data.ShopItem
import com.example.remotelist.model.data.UserState
import com.example.remotelist.model.repository.UserRepository
import com.example.remotelist.utils.firebase.DocumentNotFoundException
import com.example.remotelist.utils.firebase.NoUserException
import com.example.remotelist.utils.firebase.getSnapshot
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private val users: CollectionReference
    inline get() = Firebase.firestore.collection(USERS)

class Model @Inject constructor(private val userRepo: UserRepository) {
    val chosenUserLogin: MutableStateFlow<String> = MutableStateFlow("")

    private var _userState: MutableStateFlow<UserState> = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()

    init {
        GlobalScope.launch {
            chosenUserLogin.collect { login ->
                if(login.isBlank())
                    _userState = userRepo.currentUserState as MutableStateFlow<UserState>
                else
                    _userState.emit(userRepo.getUserState(login))
            }
        }
        GlobalScope.launch {
            userRepo.currentUserState.collect {
                launch {
                    _userState
                        .emit(
                            value = _userState
                                .value
                                .withShoppingList(shoppingList = it.shoppingList)
                        )
                }
            }
        }
    }

    suspend fun updateItem(shopItem: ShopItem) = userRepo.run {
        if (chosenUserLogin.value.isBlank())
            getCurrentUserDocument()
        else
            getUserDocument(uid = chosenUserLogin.value)
    }.shoppingList
        .findDocument(shopItem.name)
        ?.set(shopItem.increased())
        ?.await()
        ?: throw DocumentNotFoundException()

    suspend fun createItem(maxCount: Int, name: String, description: String) {
        userRepo
            .getCurrentUserDocument()
            .shoppingList
            .add(ShopItem(maxCount, name, description))
    }

    suspend fun deleteItem(shopItem: ShopItem) {
        userRepo
            .getCurrentUserDocument()
            .shoppingList
            .findDocument(shopItem.name)
            ?.delete()
            ?: throw DocumentNotFoundException()
    }

    fun register(login:String, email:String, password:String){

        userRepo.createUser(email, password)

        val userData = hashMapOf(
            "login" to login,
            "friends" to emptyArray<String>(),
            "userId" to Firebase.auth.currentUser!!.uid
        )

        users.document().set(userData)
    }

     suspend fun signIn(login: String, email: String, password: String) {
        userRepo.signInUser(email, password)
        chosenUserLogin.emit(login)
    }

    fun signOut() = userRepo.deleteUser()

}

private suspend fun findUser(login: String): DocumentReference = users
    .whereEqualTo(LOGIN, login)
    .getSnapshot()
    ?.let {
        it.first().reference
    } ?: throw NoUserException()

private suspend fun CollectionReference.findDocument(name: String): DocumentReference? = this
    .whereEqualTo(NAME, name)
    .getSnapshot()
    ?.first()
    ?.reference

private val DocumentReference.shoppingList: CollectionReference
    inline get() = collection(SHOPPING_LIST)