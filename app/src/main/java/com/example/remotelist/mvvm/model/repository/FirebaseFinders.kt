package com.example.remotelist.mvvm.model.repository

import com.example.remotelist.mvvm.model.data.ShopItem
import com.example.remotelist.utils.firebase.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

@Throws(UserNotFoundException::class)
suspend fun findUserDocumentByUId(uid: String): DocumentReference {
    val querySnapshot = Firebase.firestore
        .collection(USERS)
        .whereEqualTo(USER_ID, uid)
        .getSnapshot()

    querySnapshot?.let {
        if (it.isEmpty)
            throw UserDocumentEmptyException()
        else
            return it.documents[0].reference
    } ?: throw UserNotFoundException()
}

suspend fun findUserDocumentByLogin(login: String): DocumentReference = users
    .whereEqualTo(LOGIN, login)
    .getSnapshot()
    ?.first()
    ?.reference ?: throw UserNotFoundException()

suspend fun getShoppingListByLogin(login: String): List<ShopItem> {
    return findUserDocumentByLogin(login = login).shoppingList.getSnapshot()?.toObjects()
        ?: throw ShoppingListNotFoundException()
}