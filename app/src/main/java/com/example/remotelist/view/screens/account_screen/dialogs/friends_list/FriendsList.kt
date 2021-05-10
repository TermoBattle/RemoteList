package com.example.remotelist.view.screens.account_screen.dialogs.friends_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remotelist.R
import com.example.remotelist.library.jetpack_compose.AddFriendDialogButton
import com.example.remotelist.library.jetpack_compose.Login
import com.example.remotelist.library.jetpack_compose.Text
import kotlinx.coroutines.flow.StateFlow

@Composable
fun FriendsList(
    dialogController: FriendsListController = viewModel<FriendsListViewModel>(),
    openAddFriendDialog: () -> Unit,
) {
    val friends by dialogController.friends.collectAsState()
    Card {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = R.string.friends_list, style = MaterialTheme.typography.h5)
                }
            }

            items(items = friends) { login ->
                Login(login = login)
            }

            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    AddFriendDialogButton(onClick = openAddFriendDialog)
                }
            }
        }
    }
}

interface FriendsListController {
    val friends: StateFlow<List<String>>
}
