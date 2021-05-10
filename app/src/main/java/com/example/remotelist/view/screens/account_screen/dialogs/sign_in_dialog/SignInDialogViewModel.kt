package com.example.remotelist.view.screens.account_screen.dialogs.sign_in_dialog

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remotelist.library.common.launchAndHandleFirestoreExceptions
import com.example.remotelist.repositories.FirebaseAuthentication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInDialogViewModel @Inject constructor(): ViewModel(), SignInDialogController {
    override val email: MutableState<String> = mutableStateOf("")
    override val password: MutableState<String> = mutableStateOf("")

    override fun signIn(context: Context) = context
        .launchAndHandleFirestoreExceptions(coroutineScope = viewModelScope){
            FirebaseAuthentication.signIn(email = email.value, password = password.value)
        }
}