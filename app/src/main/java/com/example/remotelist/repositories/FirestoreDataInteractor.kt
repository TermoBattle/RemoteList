package com.example.remotelist.repositories

import com.example.remotelist.library.data_structures.ShopItem
import com.example.remotelist.library.firebase.DocumentNotFoundException
import com.example.remotelist.library.firebase.findDocument
import com.example.remotelist.library.firebase.shoppingList
import kotlinx.coroutines.tasks.await

object FirestoreDataInteractor {
    suspend fun updateItem(shopItem: ShopItem): Void = FirestoreDataProvider.getShoppingListReference()
        .findDocument(shopItem.name)
        ?.set(shopItem)
        ?.await()
        ?: throw DocumentNotFoundException()

    suspend fun createItem(maxCount: Int, name: String, description: String) {
        FirestoreDataProvider.getCurrentUserDocumentReference()
            .shoppingList
            .add(ShopItem(maxCount, name, description))
            .await()
    }

    suspend fun deleteItem(shopItem: ShopItem) {
        FirestoreDataProvider.getCurrentUserDocumentReference()
            .shoppingList
            .findDocument(shopItem.name)
            ?.delete()
            ?: throw DocumentNotFoundException()
    }
}