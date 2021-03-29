package com.example.remotelist.model.data

import com.example.remotelist.model.FRIENDS
import com.example.remotelist.model.SHOPPING_LIST
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

typealias ShoppingList = List<ShopItem>
typealias Friends = List<String>

class UserState private constructor(
    private val _shoppingList: MutableStateFlow<ShoppingList>,
    private val _friends: MutableStateFlow<Friends>,
    private val _friendsCount: MutableStateFlow<Int>
) {

    constructor(userDocumentReference: DocumentReference) : this() {
        GlobalScope.launch {
            userDocumentReference
                .addSnapshotListener { data, e ->
                    if (e == null && data != null) {
                        launch { _shoppingList.emit(data[SHOPPING_LIST] as ShoppingList) }
                        launch { _friends.emit(data[FRIENDS] as Friends) }
                        launch { _friendsCount.emit(_friends.value.size) }
                    }
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

}