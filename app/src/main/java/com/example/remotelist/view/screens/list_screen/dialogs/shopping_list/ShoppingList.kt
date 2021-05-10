package com.example.remotelist.view.screens.list_screen.dialogs.shopping_list

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remotelist.R
import com.example.remotelist.library.data_structures.ShopItem
import com.example.remotelist.library.jetpack_compose.ShopItemComposable
import com.example.remotelist.library.jetpack_compose.Text
import com.example.remotelist.library.jetpack_compose.innerPadding
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ShoppingList(
    modifier: Modifier = Modifier,
    shoppingListController: ShoppingListController = viewModel<ShoppingListViewModel>()
) {
    val shoppingList: List<ShopItem> by shoppingListController.shoppingList.collectAsState()

    if (shoppingList.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = R.string.no_list)
                Spacer(modifier = innerPadding)
            }
        }
    } else
        LazyColumn(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            items(items = shoppingList) { shopItem ->
                val context = LocalContext.current

                ShopItemComposable(
                    shopItem = shopItem,
                    deleteShopItem = { shoppingListController.delete(context, it) },
                    onBuy = { shoppingListController.buy(context, it) },
                    onRefresh = { shoppingListController.refresh(context = LocalContext.current) }
                )
            }
        }
}

interface ShoppingListController {
    fun delete(context: Context, shopItem: ShopItem)
    fun buy(context: Context, shopItem: ShopItem)
    fun refresh(context: Context)

    val shoppingList: StateFlow<List<ShopItem>>
}
