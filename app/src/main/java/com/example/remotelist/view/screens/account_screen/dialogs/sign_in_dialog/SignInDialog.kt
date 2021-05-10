package com.example.remotelist.view.screens.account_screen.dialogs.sign_in_dialog

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
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
fun SignInDialog(
    dialogController: SignInDialogController = viewModel<SignInDialogViewModel>(),
    onClose: () -> Unit
) =
    Dialog(onDismissRequest = onClose) {
        Text(R.string.sign_in_with_email_and_password)

        var email by dialogController.email
        var password by dialogController.password

        Column(
            modifier = Modifier
                .background(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colors.onPrimary
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            val context = LocalContext.current

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = R.string.email) },
                isError = email.isBlank(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = R.string.password) },
                isError = password.isBlank(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            OutlinedButton(
                onClick = {
                    dialogController.signIn(context = context)
                    onClose()
                },
                content = { Text(text = R.string.sign_in, style = MaterialTheme.typography.button) })
        }
    }

interface SignInDialogController {
    val email: MutableState<String>
    val password: MutableState<String>

    fun signIn(context: Context)
}
