package com.example.remotelist.view.screens.account_screen.main

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement.SpaceAround
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remotelist.R
import com.example.remotelist.R.string.sign_out
import com.example.remotelist.library.data_structures.Refresher
import com.example.remotelist.library.jetpack_compose.AddFriendDialogButton
import com.example.remotelist.library.jetpack_compose.RefreshButton
import com.example.remotelist.library.jetpack_compose.RegisterDialogButton
import com.example.remotelist.library.jetpack_compose.padding
import com.example.remotelist.view.screens.account_screen.dialogs.add_friend_dialog.AddFriendDialog
import com.example.remotelist.view.screens.account_screen.dialogs.friends_list.FriendsList
import com.example.remotelist.view.screens.account_screen.dialogs.register_dialog.RegisterDialog
import com.example.remotelist.view.screens.account_screen.dialogs.sign_in_dialog.SignInDialog
import com.example.remotelist.view.screens.account_screen.dialogs.user_data.UserData
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AccountScreen(
    screenController: AccountScreenController = viewModel<AccountScreenViewModel>(),
    onOpenDrawer: () -> Unit
) = Scaffold(
    modifier = Modifier.animateContentSize(),
    topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = onOpenDrawer) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                }
            },
            title = { Text(text = stringResource(R.string.account_screen)) },
            actions = {
                RefreshButton(screenController)
            }
        )
    },
    content = {
        var registerDialog by screenController.registerWithEmailAndPasswordDialog
        var signInDialog by screenController.signInWithEmailAndPasswordDialog
        var addFriendDialog by screenController.addFriendDialog

        val logged by screenController.logged.collectAsState()

        // Диалоги для входа и регистрации
        when {
            registerDialog -> RegisterDialog(
                onClose = { registerDialog = false }
            )
            signInDialog -> SignInDialog(
                onClose = { signInDialog = false }
            )
            addFriendDialog -> AddFriendDialog(
                onClose = { addFriendDialog = false }
            )
        }

        Column(
            modifier = padding.fillMaxSize(),
            verticalArrangement = SpaceAround
        ) {
            if (logged) {
                UserData()
                FriendsList(openAddFriendDialog = { addFriendDialog = false })
                Column {
                    val context = LocalContext.current
                    RegisterDialogButton { registerDialog = true }
                    Spacer(modifier = Modifier.padding(8.dp))
                    OutlinedButton(
                        onClick = { signInDialog = true },
                        content = {
                            com.example.remotelist.library.jetpack_compose.Text(
                                text = R.string.sign_in,
                                style = MaterialTheme.typography.button
                            )
                        }
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    OutlinedButton(onClick = { screenController.signOut(context = context) }) {
                        com.example.remotelist.library.jetpack_compose.Text(
                            text = sign_out,
                            style = MaterialTheme.typography.button
                        )
                    }
                }
            } else {
                AddFriendDialogButton { addFriendDialog = true }
            }
        }
    }
)

interface AccountScreenController : Refresher {

    val registerWithEmailAndPasswordDialog: MutableState<Boolean>
    val signInWithEmailAndPasswordDialog: MutableState<Boolean>
    val addFriendDialog: MutableState<Boolean>

    val logged: StateFlow<Boolean>

    fun signOut(context: Context)
}