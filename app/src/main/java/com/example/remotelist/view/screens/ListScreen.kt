package com.example.remotelist.view.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.remotelist.R
import com.example.remotelist.model.data.ShopItem
import com.example.remotelist.utils.ExtendedFloatingActionButton
import com.example.remotelist.utils.FilterN
import com.example.remotelist.utils.IconButton
import com.example.remotelist.viewmodel.ListViewModel


@Composable
fun ListScreen(listViewModel: ListViewModel, onOpenDrawer: () -> Unit) {
    var newShoppingItemDialog: Boolean by listViewModel.newShoppingItemDialog
    var chooseUserDialog: Boolean by listViewModel.chooseUserDialog

    val friendsCount by listViewModel.friendsCount
    //Экран с названием и списком
    Scaffold(
        modifier = Modifier
            .padding(16.dp)
            .animateContentSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onOpenDrawer,
                        imageVector = Icons.Default.Menu,
                        contentDescription = null
                    )
                },
                title = { Text(text = stringResource(R.string.shopping_list)) },
                actions = {
                    if (friendsCount > 0) IconButton(
                        onClick = { chooseUserDialog = true },
                        imageVector = Icons.Default.FilterN(friendsCount),
                        contentDescription = null
                    )
                }
            )
        },
        content = {
            when {
                newShoppingItemDialog -> NewShoppingItemDialog(onClose = {
                    newShoppingItemDialog = false
                }, listViewModel = listViewModel)
                chooseUserDialog -> ChooseShoppingListDialog(
                    onClose = { chooseUserDialog = false },
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
                onClick = { newShoppingItemDialog = true }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    )


}

@Composable
fun ChooseShoppingListDialog(
    onClose: () -> Unit,
    listViewModel: ListViewModel,
    onChooseUser: (login: String) -> Unit
) = Dialog(onDismissRequest = onClose) {

    val friendsList by listViewModel.friends

    Column {
        Text(text = stringResource(R.string.choose_friends))
        Text(text = stringResource(R.string.users))
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
                Text(text = stringResource(id = R.string.no_friends))
            }
    }
}

@Composable
private fun ShoppingList(modifier: Modifier = Modifier, listViewModel: ListViewModel) {
    val context = LocalContext.current
    val shoppingList: List<ShopItem> by listViewModel.shoppingList

    if (shoppingList.isEmpty())
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(text = stringResource(id = R.string.no_list))
        }
    else
        LazyColumn(modifier = modifier) {
            items(items = shoppingList) { shopItem ->
                ShopItemComposable(
                    shopItem = shopItem,
                    deleteShopItem = { listViewModel.delete(context, it) },
                    increase = { listViewModel.increase(context, it) }
                )
            }
        }
}

@Composable
private fun NewShoppingItemDialog(onClose: () -> Unit, listViewModel: ListViewModel) =
    Dialog(onDismissRequest = onClose) {
        val context = LocalContext.current

        var maxCount by listViewModel.maxCount
        var name by listViewModel.name
        var description by listViewModel.description

        Column(modifier = Modifier.background(color = MaterialTheme.colors.onPrimary, shape = MaterialTheme.shapes.medium).padding(16.dp)) {
            Text(text = "Новая покупка")
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Название") }
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
                label = { Text(text = "Количество") },
                isError = maxCount == 0,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(text = "Описание") })

            Divider(modifier = Modifier.padding(8.dp))

            OutlinedButton(
                onClick = {
                    listViewModel.create(context)
                    onClose()
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = ""
                    )
                    Text(text = "Создать")
                },
            )

        }
    }

@Composable
private fun ShopItemComposable(shopItem: ShopItem, deleteShopItem: (ShopItem) -> Unit, increase: (ShopItem) -> Unit) = Row {
    shopItem.apply {
        Text(text = "${shopCounter.count}/${shopCounter.maxCount}")
        Divider(modifier = Modifier.padding(16.dp))
        Row {
            Text(text = name)
            Row(
                modifier = Modifier.scrollable(
                    orientation = Orientation.Horizontal,
                    state = rememberScrollState()
                )
            ) { Text(text = description) }
        }
    }

    IconButton(onClick = { increase(shopItem) }) {
        Icon(
            imageVector = Icons.Default.PlusOne,
            contentDescription = "Одна штука куплена"
        )
    }

    IconButton(onClick = { deleteShopItem(shopItem) }) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Удалить покупку"
        )
    }
}