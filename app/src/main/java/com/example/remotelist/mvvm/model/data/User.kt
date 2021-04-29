package com.example.remotelist.mvvm.model.data

import com.example.remotelist.mvvm.model.repository.findUserDocumentByLogin
import com.example.remotelist.mvvm.model.repository.findUserDocumentByUId
import com.example.remotelist.utils.firebase.FRIENDS
import com.example.remotelist.utils.firebase.SHOPPING_LIST
import com.example.remotelist.utils.firebase.getSnapshot
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

typealias ShoppingList = List<ShopItem>
typealias Friends = List<String>

data class User(val shoppingList:ShoppingList, val friends:Friends, val friendsCount:Int){
    constructor():this(shoppingList = emptyList(), friends = emptyList(), friendsCount = -1)
    companion object {
        operator fun invoke(userDocumentReference: DocumentReference): User {
            var shoppingList = emptyList<ShopItem>()
            var friendsList = emptyList<String>()

            GlobalScope.launch {
                userDocumentReference.getSnapshot().let { snapshot ->
                    shoppingList = snapshot[SHOPPING_LIST] as List<ShopItem>? ?: emptyList()
                    friendsList = snapshot[FRIENDS] as List<String>? ?: emptyList()
                }
            }

            return User(shoppingList = shoppingList, friends = friendsList, friendsCount = friendsList.size)
        }
    }
}

suspend fun getUser(uid: String) = User(userDocumentReference = findUserDocumentByUId(uid))

suspend fun getUserByLogin(login: String) = User(
    userDocumentReference = findUserDocumentByLogin(login = login)
)

/*
data class UserState (
//    private val _login:MutableStateFlow<String>,
//    val userDocumentReference: DocumentReference,
    var _shoppingList: MutableStateFlow<ShoppingList>,
    private val _friends: MutableStateFlow<Friends>,
    private val _friendsCount: MutableStateFlow<Int>
) {

    constructor(userDocumentReference: DocumentReference) : this() {
        GlobalScope.launch {
            userDocumentReference
                .addSnapshotListener { userData, e ->
                    if (e == null && userData != null) {
                        launch { _shoppingList.emit(userData[SHOPPING_LIST, Array<ShopItem>::class.java]!!.asList()) }
                        launch { _friends.emit(userData[FRIENDS, Array<String>::class.java]!!.asList()) }
                        launch { _friendsCount.emit(_friends.value.size) }
                    }
                }
            userDocumentReference.collection(SHOPPING_LIST).addSnapshotListener { snapshot, error ->
                if (snapshot != null && error == null)
                    _shoppingList.value = snapshot.toObjects()
            }
        }
    }

    constructor():this(
        MutableStateFlow(emptyList()),
        MutableStateFlow(emptyList()),
        MutableStateFlow(0)
    )

    val shoppingList = _shoppingList.asStateFlow()
    val friends = _friends.asStateFlow()
    val friendsCount = _friendsCount.asStateFlow()


    val isEmpty get() = _shoppingList.value == emptyList<ShopItem>() && _friends.value == emptyList<String>() && _friendsCount.value == 0
}*/
