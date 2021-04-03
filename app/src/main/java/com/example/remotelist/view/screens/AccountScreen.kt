package com.example.remotelist.view.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.remotelist.R
import com.example.remotelist.utils.IconButton
import com.example.remotelist.viewmodel.AccountViewModel

@Composable
fun AccountScreen(accountViewModel: AccountViewModel, onOpenDrawer: () -> Unit) = Scaffold(
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
            title = {
                Text(text = stringResource(R.string.account_screen))
            }
        )
    },
    content = {
        val logged by accountViewModel.userLogged

        var registerDialog by accountViewModel.registerWithEmailAndPasswordDialog
        var signInDialog by accountViewModel.signInWithEmailAndPasswordDialog

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
        }

        Column {
            if (logged) {
                val context = LocalContext.current
                Text(
                    text = stringResource(
                        R.string.user_logged
                    )
                )
                OutlinedButton(onClick = { accountViewModel.signOut(context) }) {
                    Text(text = stringResource(id = R.string.sign_out))
                }
            } else {
                Text(text = stringResource(id = R.string.user_logged))
                OutlinedButton(
                    onClick = { registerDialog = true },
                    content = { Text(text = stringResource(R.string.register)) }
                )
                OutlinedButton(
                    onClick = { signInDialog = true },
                    content = { Text(text = stringResource(R.string.sign_in)) }
                )
            }
        }
    }
)

@Composable
fun SignInDialog(accountViewModel: AccountViewModel, onClose: () -> Unit) =
    Dialog(onDismissRequest = onClose) {
        Text(text = stringResource(R.string.sign_in_with_email_and_password))

        var login by accountViewModel.signInLogin
        var email by accountViewModel.signInEmail
        var password by accountViewModel.signInPassword

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

        val context = LocalContext.current
        OutlinedButton(
            onClick = { accountViewModel.signIn(context = context) },
            content = { Text(text = stringResource(R.string.register)) }
        )
    }

@Composable
fun RegisterDialog(accountViewModel: AccountViewModel, onClose: () -> Unit) =
    Dialog(onDismissRequest = onClose) {
        val context = LocalContext.current

        Column {
            Text(text = stringResource(R.string.register_with_email_and_password))


            var login by accountViewModel.registerLogin
            var email by accountViewModel.registerEmail
            var password by accountViewModel.registerPassword

            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text(text = stringResource(id = R.string.login)) },
                isError = login.isBlank()
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
                onClick = { accountViewModel.register(context) },
                content = { Text(text = stringResource(R.string.register)) }
            )
        }
    }