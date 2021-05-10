package com.example.remotelist.view.screens.account_screen.dialogs.register_dialog

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remotelist.R.string.*
import com.example.remotelist.library.common.launchAndHandleFirestoreExceptions
import com.example.remotelist.library.common.toast
import com.example.remotelist.library.common.validate
import com.example.remotelist.repositories.FirebaseAuthentication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterDialogViewModel @Inject constructor() : ViewModel(), RegisterDialogController {
    override val login = mutableStateOf("")
    override val email = mutableStateOf("")
    override val password = mutableStateOf("")

    private inline fun Context.validateFields(onInvalid: () -> Unit) {
        login.value.validate {
            toast(no_login)
            onInvalid()
        }
        email.value.validate {
            toast(toast_no_email)
            onInvalid()
        }
        password.value.validate {
            toast(toast_no_password)
            onInvalid()
        }
    }

    override fun register(context: Context) {
        val login = login.value
        val email = email.value
        val password = password.value

        context.validateFields { return }

        context.launchAndHandleFirestoreExceptions(coroutineScope = viewModelScope) {
            FirebaseAuthentication.createUser(
                login = login,
                email = email,
                password = password
            )
        }
    }
}