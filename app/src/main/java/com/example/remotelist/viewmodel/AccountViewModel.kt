package com.example.remotelist.viewmodel

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remotelist.R
import com.example.remotelist.model.Model
import com.example.remotelist.utils.firebase.FirebaseException
import com.example.remotelist.utils.firebase.toastFirebaseException
import com.example.remotelist.utils.toast
import com.example.remotelist.utils.validate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val model: Model, private val context: Context) :
    ViewModel() {

    val newFriendLogin = mutableStateOf("")
    val addFriendDialog = mutableStateOf(false)
    var friendsList = mutableStateOf(emptyList<String>())

    val signInWithEmailAndPasswordDialog = mutableStateOf(false)
    val registerWithEmailAndPasswordDialog = mutableStateOf(false)
    val userLogged = mutableStateOf(false)

    val registerLogin = mutableStateOf("")
    val registerEmail = mutableStateOf("")
    val registerPassword = mutableStateOf("")

    val signInLogin = mutableStateOf("")
    val signInPassword = mutableStateOf("")
    val signInEmail = mutableStateOf("")

    init {
        viewModelScope.launch {
            model.userState.collect {
                launch {
                    it.friends.collect {
                        friendsList.value = it
                    }
                }
            }
        }

        Firebase.auth.addAuthStateListener {
            userLogged.value = it.currentUser != null
        }

    }

    //--- Functions Aliases ---//////////////////////////////////////
    private inline fun toastFirebaseException(firebaseException: FirebaseException) =
        context.toastFirebaseException(firebaseException)

    private inline fun toast(@StringRes message: Int) = context.toast(message)

    //--- Public API --- ////////////////////////////////////////////
    fun signOut() = viewModelScope.launch {
        try {
            model.signOut()
        }
        catch (e: FirebaseException) {
            context.toastFirebaseException(e)
        }
    }

    fun openRegisterDialog() = if (userLogged.value)
        toast(R.string.user_already_registered)
    else
        registerWithEmailAndPasswordDialog.value = true

    fun register() {
        registerLogin.validate {
            context.toast(R.string.no_login)
            return
        }
        registerEmail.validate {
            context.toast(R.string.toast_no_email)
            return
        }
        registerPassword.validate {
            context.toast(R.string.toast_no_password)
            return
        }

        try {
            model.register(
                login = registerLogin.value,
                email = registerEmail.value,
                password = registerPassword.value
            )
        } catch (e: FirebaseException) {
            toastFirebaseException(e)
        }

    }

    fun openSignInDialog() = if (userLogged.value)
        toast(R.string.user_already_registered)
    else
        signInWithEmailAndPasswordDialog.value = true

    fun signIn() {
        signInLogin.validate {
            context.toast(R.string.toast_no_login)
        }
        signInEmail.validate {
            context.toast(R.string.toast_no_email)
        }
        signInPassword.validate {
            context.toast(R.string.toast_no_password)
        }

        viewModelScope.launch {
            try {
                model.signIn(
                    login = signInLogin.value,
                    email = signInEmail.value,
                    password = signInPassword.value
                )
            } catch (e: FirebaseException) {
                toastFirebaseException(e)
            }

        }
    }

    fun addFriend() {
        newFriendLogin.validate {
            toast(R.string.toast_no_login)
            return
        }

        try {
            viewModelScope.launch { model.addFriend(login = newFriendLogin.value) }
        } catch (e: FirebaseException) {
            toastFirebaseException(e)
        }
    }
}