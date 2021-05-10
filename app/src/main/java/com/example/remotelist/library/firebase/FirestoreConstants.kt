package com.example.remotelist.library.firebase

import com.example.remotelist.library.data_structures.ShopItem
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//Main collection
const val USERS = "users"
val users: CollectionReference
    inline get() = Firebase.firestore.collection(USERS)

//UserDocument fields
const val USER_ID = "userId"
//val DocumentSnapshot.userId: String
//    get() = this[USER_ID, String::class.java] ?: throw UserIdNotFoundException()

const val FRIENDS = "friends"
val DocumentSnapshot.friends: List<String>
    get() = this[FRIENDS] as List<String>? ?: throw FriendsNotFoundException()

const val LOGIN = "login"
val DocumentSnapshot.login: String
    get() = this[LOGIN, String::class.java] ?: throw LoginNotFoundException()

//UserDocument collections
const val SHOPPING_LIST = "ShoppingList"
val DocumentReference.shoppingList: CollectionReference
    inline get() = collection(SHOPPING_LIST)
val DocumentSnapshot.shoppingList: List<ShopItem>
    get() = this[SHOPPING_LIST] as List<ShopItem>? ?: throw ShoppingListNotFoundException()

//ShopItem fields
const val NAME = "name"
