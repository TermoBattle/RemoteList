package com.example.remotelist.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remotelist.R
import com.example.remotelist.model.Model
import com.example.remotelist.utils.firebase.FirebaseException
import com.example.remotelist.utils.firebase.toastFirebaseException
import com.example.remotelist.utils.toast
import com.example.remotelist.utils.validate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(val model: Model) : ViewModel() {
    val signInWithEmailAndPasswordDialog = mutableStateOf(false)
    val registerWithEmailAndPasswordDialog = mutableStateOf(false)
    val userLogged = mutableStateOf(false)

    val registerLogin = mutableStateOf("")
    val registerEmail = mutableStateOf("")
    val registerPassword = mutableStateOf("")

    val signInLogin = mutableStateOf("")
    val signInPassword = mutableStateOf("")
    val signInEmail = mutableStateOf("")

    fun signOut(context: Context) = try {
        model.signOut()
    } catch (e: FirebaseException) {
        context.toastFirebaseException(e)
    }

    fun register(context: Context) {
        registerLogin.validate {
            context.toast(R.string.no_login)
        }
        registerEmail.validate {
            context.toast(R.string.toast_no_email)
            return
        }
        registerPassword.validate {
            context.toast(R.string.toast_no_password)
            return
        }

        model.register(
            login = registerLogin.value,
            email = registerEmail.value,
            password = registerPassword.value
        )
    }

    fun signIn(context: Context) {
        signInLogin.validate {
            context.toast(R.string.toast_no_login)
        }
        signInEmail.validate{
            context.toast(R.string.toast_no_email)
        }
        signInPassword.validate{
            context.toast(R.string.toast_no_password)
        }

        viewModelScope.launch {
            model.signIn(
                login = signInLogin.value,
                email = signInEmail.value,
                password = signInPassword.value
            )
        }
    }
}