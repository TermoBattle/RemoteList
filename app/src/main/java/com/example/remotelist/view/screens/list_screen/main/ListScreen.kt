package com.example.remotelist.view.screens.list_screen.main

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remotelist.R
import com.example.remotelist.library.data_structures.Refresher
import com.example.remotelist.library.jetpack_compose.FilterN
import com.example.remotelist.library.jetpack_compose.RefreshButton
import com.example.remotelist.library.jetpack_compose.innerPadding
import com.example.remotelist.view.screens.list_screen.dialogs.choose_shopping_list_dialog.ChooseShoppingListDialog
import com.example.remotelist.view.screens.list_screen.dialogs.new_shopping_list_dialog.NewShoppingItemDialog
import com.example.remotelist.view.screens.list_screen.dialogs.shopping_list.ShoppingList
import kotlinx.coroutines.flow.StateFlow


@Composable
fun ListScreen(
    screenController: ListScreenController = viewModel<ListScreenViewModel>(),
    onOpenDrawer: () -> Unit
) {
    var newShoppingItemDialog: Boolean by screenController.newShoppingItemDialog
    var chooseFriendDialog: Boolean by screenController.chooseFriendDialog

    val friendsCount by screenController.friendsCount.collectAsState()

    //Экран с названием и списком
    Scaffold(
        modifier = Modifier.animateContentSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                },
                title = { com.example.remotelist.library.jetpack_compose.Text(text = R.string.shopping_list) },
                actions = {
                    if (friendsCount > 0)
                        IconButton(onClick = { chooseFriendDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.FilterN(friendsCount),
                                contentDescription = null
                            )
                        }
                    RefreshButton(screenController)
                }
            )
        },
        content = {
            when {
                newShoppingItemDialog -> NewShoppingItemDialog(
                    onClose = { newShoppingItemDialog = false },
                )
                chooseFriendDialog -> ChooseShoppingListDialog(
                    onClose = { chooseFriendDialog = false },
                )
            }
            ShoppingList(
                modifier = innerPadding.fillMaxSize(),
            )
        },
        floatingActionButton = {
            val context = LocalContext.current
            com.example.remotelist.library.jetpack_compose.ExtendedFloatingActionButton(
                imageVector = Icons.Default.Create,
                text = stringResource(R.string.add_item),
                onClick = {
                    newShoppingItemDialog = true
                    screenController.refresh(context = context)
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    )


}

interface ListScreenController : Refresher {
    val friendsCount: StateFlow<Int>

    val chooseFriendDialog: MutableState<Boolean>
    val newShoppingItemDialog: MutableState<Boolean>

}