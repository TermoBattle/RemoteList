package com.example.remotelist.view.screens.list_screen.dialogs.choose_shopping_list_dialog

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remotelist.R
import com.example.remotelist.library.jetpack_compose.LoginButton
import com.example.remotelist.library.jetpack_compose.Text
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ChooseShoppingListDialog(
    dialogController: ChooseShoppingListDialogController = viewModel<ChooseShoppingListDialogViewModel>(),
    onClose: () -> Unit
) = Dialog(onDismissRequest = onClose) {

    val friends by dialogController.friends.collectAsState()

    Column(
        modifier = Modifier
            .background(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.onPrimary
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = R.string.choose_friends, style = MaterialTheme.typography.h4)
        Text(text = R.string.users, style = MaterialTheme.typography.h5)
        if (friends.isNotEmpty()) {
            LazyColumn(verticalArrangement = Arrangement.Center) {
                items(
                    items = friends,
                    itemContent = { friendLogin: String ->
                        val context = LocalContext.current
                        LoginButton(
                            login = friendLogin,
                            onClick = {
                                dialogController.chooseUser(context = context, login = friendLogin)
                                onClose()
                            }
                        )
                    }
                )
            }
        } else {
            Box(contentAlignment = Alignment.Center) {
                Text(text = R.string.no_friends)
            }
        }
    }
}

interface ChooseShoppingListDialogController {
    val friends: StateFlow<List<String>>

    fun chooseUser(context: Context, login: String)
}
