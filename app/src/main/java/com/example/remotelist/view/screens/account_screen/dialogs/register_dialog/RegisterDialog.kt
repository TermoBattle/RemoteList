package com.example.remotelist.view.screens.account_screen.dialogs.register_dialog

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remotelist.R
import com.example.remotelist.library.jetpack_compose.Text

@Composable
fun RegisterDialog(
    dialogController: RegisterDialogController = viewModel<RegisterDialogViewModel>(),
    onClose: () -> Unit
) = Dialog(onDismissRequest = onClose) {

    var login by dialogController.login
    var email by dialogController.email
    var password by dialogController.password

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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = R.string.password) },
            isError = password.isBlank(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        OutlinedButton(onClick = {
            dialogController.register(context = context)
            onClose()
        }) {
            Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
            Text(text = R.string.register, style = MaterialTheme.typography.button)
        }
    }
}

interface RegisterDialogController {

    val login: MutableState<String>
    val email: MutableState<String>
    val password: MutableState<String>

    fun register(context: Context)
}