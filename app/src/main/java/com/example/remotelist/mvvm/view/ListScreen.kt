package com.example.remotelist.mvvm.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.remotelist.R
import com.example.remotelist.mvvm.model.data.ShopItem
import com.example.remotelist.mvvm.viewmodel.ListViewModel
import com.example.remotelist.utils.*
import kotlinx.coroutines.launch


@Composable
fun ListScreen(listViewModel: ListViewModel, onOpenDrawer: () -> Unit) {
    var newShoppingItemDialog: Boolean by listViewModel.newShoppingItemDialog
    var chooseFriendDialog: Boolean by listViewModel.chooseFriendDialog

    val friendsCount by listViewModel.friendsCount.collectAsState()

    //Экран с названием и списком
    Scaffold(
        modifier = Modifier
            .animateContentSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                },
                title = { Text(text = R.string.shopping_list) },
                actions = {
                    if (friendsCount > 0) IconButton(onClick = { chooseFriendDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterN(friendsCount),
                            contentDescription = null
                        )
                    }
                    RefreshButton(listViewModel)
                }
            )
        },
        content = {
            when {
                newShoppingItemDialog -> NewShoppingItemDialog(
                    onClose = { newShoppingItemDialog = false },
                    listViewModel = listViewModel
                )
                chooseFriendDialog -> ChooseShoppingListDialog(
                    onClose = { chooseFriendDialog = false },
                    listViewModel = listViewModel,
                    onChooseUser = listViewModel::updateUser
                )
            }
            ShoppingList(modifier = Modifier.fillMaxSize(), listViewModel = listViewModel)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                imageVector = Icons.Default.Create,
                text = stringResource(R.string.add_item),
                onClick = {
                    newShoppingItemDialog = true
                    listViewModel.refresh()
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    )


}

@Composable
private fun ChooseShoppingListDialog(
    onClose: () -> Unit,
    listViewModel: ListViewModel,
    onChooseUser: (login: String) -> Unit
) = Dialog(onDismissRequest = onClose) {

    val friendsList by listViewModel.friends.collectAsState()

    Column(
        modifier = Modifier
            .background(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.onPrimary
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = R.string.choose_friends)
        Text(text = R.string.users)
        if (friendsList.isNotEmpty())
            LazyColumn(verticalArrangement = Arrangement.Center) {
                items(friendsList) { friendLogin ->
                    Text(
                        modifier = Modifier.clickable {
                            onChooseUser(friendLogin)
                            onClose()
                        },
                        text = friendLogin
                    )
                }
            }
        else
            Box(contentAlignment = Alignment.Center) {
                Text(text = R.string.no_friends)
            }
    }
}

@Composable
private fun ShoppingList(modifier: Modifier = Modifier, listViewModel: ListViewModel) {
    val shoppingList: List<ShopItem> by listViewModel.shoppingList.collectAsState()

    if (shoppingList.isEmpty())
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = R.string.no_list)
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    else
        LazyColumn(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            items(items = shoppingList) { shopItem ->
                ShopItemComposable(
                    shopItem = shopItem,
                    deleteShopItem = { listViewModel.delete(it) },
                    increase = { listViewModel.increase(it) },
                    onRefresh = listViewModel::refresh
                )
            }
        }
}

@Composable
private fun NewShoppingItemDialog(onClose: () -> Unit, listViewModel: ListViewModel) =
    Dialog(onDismissRequest = onClose) {
        var maxCount by listViewModel.maxCount
        var name by listViewModel.name
        var description by listViewModel.description

        Column(
            modifier = Modifier
                .background(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colors.onPrimary
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = R.string.new_shopping_item)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = R.string.name) }
            )
            OutlinedTextField(
                value = if (maxCount == null || maxCount?.let { it <= 0 } == true)
                    ""
                else
                    maxCount.toString(),
                onValueChange = { text: String ->
                    maxCount = text.toIntOrNull()?.let {
                        if (it <= 0)
                            null
                        else
                            it
                    }
                },
                label = { Text(text = R.string.count) },
                isError = maxCount == 0,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(text = R.string.description) })

            Divider(modifier = Modifier.padding(8.dp))

            OutlinedButton(
                onClick = {
                    listViewModel.create()
                    onClose()
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = ""
                    )
                    Text(text = R.string.create)
                },
            )

        }
    }

@Composable
private fun ShopItemComposable(
    shopItem: ShopItem,
    deleteShopItem: (ShopItem) -> Unit,
    increase: (ShopItem) -> Unit,
    onRefresh: () -> Unit
) = Row(verticalAlignment = Alignment.CenterVertically) {


    shopItem.apply {
        Box(contentAlignment = Alignment.Center) {
            if (shopItem.shopCounter.isFull)
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
            else
                Text(text = "${shopCounter.count}/${shopCounter.maxCount}")
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Column {
            Text(text = name)
            Text(text = description)
        }

    }

    if (shopItem.shopCounter.isNotFull) IconButton(
        onClick = {
            increase(shopItem)
            onRefresh()
        },
    ) {
        Icon(
            imageVector = Icons.Default.PlusOne,
            contentDescription = null
        )
    }

    val coroutineScope = rememberCoroutineScope()
    IconButton(
        onClick = {
            deleteShopItem(shopItem)
            coroutineScope.launch {
                repeat(ON_REFRESH_REPEAT_COUNT) { onRefresh() }
            }
        },
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null
        )
    }
}
