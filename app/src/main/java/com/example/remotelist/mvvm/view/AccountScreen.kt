package com.example.remotelist.mvvm.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceAround
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType.Email
import androidx.compose.ui.text.input.KeyboardType.Password
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.remotelist.R
import com.example.remotelist.mvvm.viewmodel.AccountViewModel
import com.example.remotelist.utils.RefreshButton
import com.example.remotelist.utils.Text

@Composable
fun AccountScreen(accountViewModel: AccountViewModel, onOpenDrawer: () -> Unit) = Scaffold(
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
                RefreshButton(accountViewModel)
            }
        )
    },
    content = {
        val registerDialog by accountViewModel.registerWithEmailAndPasswordDialog.collectAsState()
        val signInDialog by accountViewModel.signInWithEmailAndPasswordDialog.collectAsState()
        val addFriendDialog by accountViewModel.addFriendDialog.collectAsState()

        val logged by accountViewModel.logged.collectAsState()
        val email: String by accountViewModel.email.collectAsState()
        val login: String by accountViewModel.login.collectAsState()

        // Диалоги для входа и регистрации
        when {
            registerDialog -> RegisterDialog(
                accountViewModel = accountViewModel,
                onClose = accountViewModel::hideRegisterDialog
            )

            signInDialog -> SignInDialog(
                accountViewModel = accountViewModel,
                onClose = accountViewModel::hideSignInDialog
            )

            addFriendDialog -> AddFriendDialog(
                accountViewModel = accountViewModel,
                onClose = accountViewModel::hideAddFriendDialog
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = SpaceAround
        ) {
            if (logged) {
                Text(text = R.string.user_logged)
                Text(text = stringResource(R.string.login) + login)
                Text(text = stringResource(R.string.email) + email)
            } else
                Text(text = R.string.user_not_logged)

            FriendsList(
                accountViewModel = accountViewModel,
                onAddFriend = accountViewModel::openAddFriendDialog
            )
            Row(
                modifier = Modifier
                    .scrollable(orientation = Horizontal, state = rememberScrollState()),
            ) {
                OutlinedButton(
                    onClick = accountViewModel::openRegisterDialog,
                    content = { Text(text = R.string.register) }
                )
                Spacer(modifier = Modifier.padding(16.dp))
                OutlinedButton(
                    onClick = accountViewModel::openSignInDialog,
                    content = { Text(text = R.string.sign_in) }
                )
            }
            Spacer(modifier = Modifier.padding(16.dp))
            OutlinedButton(onClick = accountViewModel::signOut) {
                Text(text = R.string.sign_out)
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
                .padding(16.dp),
            verticalArrangement = SpaceBetween
        ) {
            OutlinedTextField(
                value = friendLogin,
                onValueChange = {
                    friendLogin = it
                },
                isError = friendLogin.isBlank(),
                label = { Text(text = stringResource(R.string.login)) }
            )
            IconButton(onClick = {
                accountViewModel.addFriend()
                onClose()
            }) {
                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null)
                Text(text = stringResource(R.string.add_friend))
            }
        }
    }

@Composable
private fun FriendsList(accountViewModel: AccountViewModel, onAddFriend: () -> Unit) {
    val friendsList by accountViewModel.friendsList.collectAsState()
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(items = friendsList){ friendLogin ->
            Text(text = friendLogin)
        }
        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                OutlinedButton(onClick = onAddFriend) {
                    Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null)
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(R.string.add_friend)
                }
            }
        }
    }
}

@Composable
private fun SignInDialog(accountViewModel: AccountViewModel, onClose: () -> Unit) =
    Dialog(onDismissRequest = onClose) {
        Text(R.string.sign_in_with_email_and_password)

        var email by accountViewModel.signInEmail
        var password by accountViewModel.signInPassword

        Column(
            modifier = Modifier
                .background(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colors.onPrimary
                )
                .padding(16.dp),
            verticalArrangement = SpaceAround
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = R.string.email) },
                isError = email.isBlank(),
                keyboardOptions = KeyboardOptions(keyboardType = Email),
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = R.string.password) },
                isError = password.isBlank(),
                keyboardOptions = KeyboardOptions(keyboardType = Password)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            OutlinedButton(
                onClick = {
                    accountViewModel.signIn()
                    onClose()
                },
                content = { Text(text = R.string.sign_in) })
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
                .padding(16.dp),
            verticalArrangement = SpaceBetween
        ) {
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text(text = R.string.login) },
                isError = login.isBlank()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = R.string.email) },
                isError = email.isBlank(),
                keyboardOptions = KeyboardOptions(keyboardType = Email)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = R.string.password) },
                isError = password.isBlank(),
                keyboardOptions = KeyboardOptions(keyboardType = Password)
            )

            OutlinedButton(onClick = {
                accountViewModel.register()
                onClose()
            }) {
                Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
                Text(text = R.string.register)
            }
        }
    }