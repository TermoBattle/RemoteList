package com.example.remotelist.view.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.remotelist.R
import com.example.remotelist.viewmodel.AccountViewModel

@Composable
fun AccountScreen(accountViewModel: AccountViewModel, onOpenDrawer: () -> Unit) = Scaffold(
    modifier = Modifier
        .animateContentSize(),
    topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = onOpenDrawer) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                }
            },
            title = {
                Text(text = stringResource(R.string.account_screen))
            }
        )
    },
    content = {
        val logged by accountViewModel.userLogged

        var registerDialog by accountViewModel.registerWithEmailAndPasswordDialog
        var signInDialog by accountViewModel.signInWithEmailAndPasswordDialog
        var addFriendDialog by accountViewModel.addFriendDialog

        // Диалоги для входа и регистрации
        when {
            registerDialog -> RegisterDialog(
                accountViewModel = accountViewModel,
                onClose = { registerDialog = false }
            )

            signInDialog -> SignInDialog(
                accountViewModel = accountViewModel,
                onClose = { signInDialog = false }
            )

            addFriendDialog -> AddFriendDialog(
                accountViewModel = accountViewModel,
                onClose = { addFriendDialog = false }
            )
        }

        Column {
            if (logged) {
                Text(text = stringResource(R.string.user_logged))

                FriendsList(
                    accountViewModel = accountViewModel,
                    onAddFriend = { addFriendDialog = true }
                )

                OutlinedButton(onClick = accountViewModel::signOut) {
                    Text(text = stringResource(id = R.string.sign_out))
                }
            } else {
                Text(text = stringResource(id = R.string.user_logged))
                OutlinedButton(
                    onClick = accountViewModel::openRegisterDialog,
                    content = { Text(text = stringResource(R.string.register)) }
                )
                OutlinedButton(
                    onClick = accountViewModel::openSignInDialog,
                    content = { Text(text = stringResource(R.string.sign_in)) }
                )
            }
        }
    }
)

@Composable
private fun AddFriendDialog(accountViewModel: AccountViewModel, onClose: () -> Unit) =
    Dialog(onDismissRequest = onClose) {
        var friendLogin by accountViewModel.newFriendLogin

        Column(
            modifier = Modifier
                .background(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colors.onPrimary
                )
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = friendLogin,
                onValueChange = {
                    friendLogin = it
                },
                isError = friendLogin.isBlank(),
                label = { Text(text = stringResource(R.string.login)) }
            )
            IconButton(onClick = accountViewModel::addFriend) {
                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null)
                Text(text = stringResource(R.string.add_friend))
            }
        }
    }

@Composable
private fun FriendsList(accountViewModel: AccountViewModel, onAddFriend: () -> Unit) {
    val friendsList by accountViewModel.friendsList
    LazyColumn {
        items(friendsList) { Text(text = it) }
        item {
            IconButton(onClick = onAddFriend) {
                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null)
                Text(text = stringResource(R.string.add_friend))
            }
        }
    }
}

@Composable
private fun SignInDialog(accountViewModel: AccountViewModel, onClose: () -> Unit) =
    Dialog(onDismissRequest = onClose) {
        Text(text = stringResource(R.string.sign_in_with_email_and_password))

        var login by accountViewModel.signInLogin
        var email by accountViewModel.signInEmail
        var password by accountViewModel.signInPassword

        Column(
            modifier = Modifier
                .background(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colors.onPrimary
                )
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text(text = stringResource(id = R.string.login)) },
                isError = login.isBlank(),
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(id = R.string.email)) },
                isError = email.isBlank(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(id = R.string.password)) },
                isError = password.isBlank(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            OutlinedButton(
                onClick = accountViewModel::signIn,
                content = { Text(text = stringResource(R.string.register)) }
            )
        }
    }

@Composable
private fun RegisterDialog(accountViewModel: AccountViewModel, onClose: () -> Unit) =
    Dialog(onDismissRequest = onClose) {

        var login by accountViewModel.registerLogin
        var email by accountViewModel.registerEmail
        var password by accountViewModel.registerPassword

        Column(
            modifier = Modifier
                .background(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colors.onPrimary
                )
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text(text = stringResource(R.string.login)) },
                isError = login.isBlank()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(R.string.email)) },
                isError = email.isBlank(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(R.string.password)) },
                isError = password.isBlank(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            IconButton(onClick = accountViewModel::register) {
                Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
                Text(text = stringResource(R.string.register))
            }
        }
    }