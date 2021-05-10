package com.example.remotelist.view.screens.list_screen.dialogs.shopping_list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remotelist.library.common.launchAndHandleFirestoreExceptions
import com.example.remotelist.library.data_structures.ShopItem
import com.example.remotelist.repositories.FirestoreDataInteractor
import com.example.remotelist.repositories.FirestoreDataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor() :
    ViewModel(), ShoppingListController {

    override val shoppingList: StateFlow<List<ShopItem>> = FirestoreDataProvider.shoppingList

    override fun delete(context: Context, shopItem: ShopItem) = context
        .launchAndHandleFirestoreExceptions(coroutineScope = viewModelScope) {
            FirestoreDataInteractor.deleteItem(shopItem)
        }

    override fun buy(context: Context, shopItem: ShopItem) = context
        .launchAndHandleFirestoreExceptions(coroutineScope = viewModelScope) {
            FirestoreDataInteractor.updateItem(shopItem = shopItem.oneMoreBought)
        }

    override fun refresh(context: Context) = context
        .launchAndHandleFirestoreExceptions(
            coroutineScope = viewModelScope,
            block = FirestoreDataProvider::refresh
        )
}