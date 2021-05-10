package com.example.remotelist.view.screens.account_screen.dialogs.add_friend_dialog

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remotelist.R

@Composable
fun AddFriendDialog(dialogController: AddFriendDialogController = viewModel<AddFriendDialogViewModel>(), onClose: () -> Unit) =
    Dialog(onDismissRequest = onClose) {
        var friendLogin by dialogController.newFriendLogin

        Column(
            modifier = Modifier
                .background(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colors.onPrimary
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val context = LocalContext.current
            OutlinedTextField(
                value = friendLogin,
                onValueChange = {
                    friendLogin = it
                },
                isError = friendLogin.isBlank(),
                label = { Text(text = stringResource(R.string.login)) }
            )
            OutlinedButton(onClick = {
                dialogController.addFriend(context = context, login = friendLogin)
                onClose()
            }) {
                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null)
                Text(
                    text = stringResource(R.string.add_friend),
                    style = MaterialTheme.typography.button
                )
            }
        }
    }

interface AddFriendDialogController {
    val newFriendLogin: MutableState<String>

    fun addFriend(context: Context, login: String)
}
