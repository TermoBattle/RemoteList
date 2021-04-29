package com.example.remotelist.mvvm.viewmodel

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remotelist.R
import com.example.remotelist.mvvm.model.Model
import com.example.remotelist.utils.firebase.FirebaseException
import com.example.remotelist.utils.firebase.friends
import com.example.remotelist.utils.firebase.login
import com.example.remotelist.utils.firebase.toast
import com.example.remotelist.utils.invoke
import com.example.remotelist.utils.log
import com.example.remotelist.utils.toast
import com.example.remotelist.utils.validate
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val model: Model, private val context: Context) :
    ViewModel() {

    private val _email = MutableStateFlow(Firebase.auth.currentUser?.email ?: "")
    private val _login = MutableStateFlow("")
    private val _friendsList = MutableStateFlow(emptyList<String>())

    val logged = model.userLogged.asStateFlow()
    val email = _email.asStateFlow()
    val login = _login.asStateFlow()
    var friendsList = _friendsList.asStateFlow()

    val newFriendLogin = mutableStateOf("")

    //Dialogs
    val addFriendDialog = MutableStateFlow(false)
    val signInWithEmailAndPasswordDialog = MutableStateFlow(false)
    val registerWithEmailAndPasswordDialog = MutableStateFlow(false)

    val registerLogin = mutableStateOf("")
    val registerEmail = mutableStateOf("")
    val registerPassword = mutableStateOf("")

    val signInPassword = mutableStateOf("")
    val signInEmail = mutableStateOf("")

    init {
        viewModelScope.launch {
            model.userData.collect {
                log("Collected new userData")
                _friendsList.emit(value = it?.friends ?: emptyList())
            }
        }
    }

    suspend fun emitUserLogged(value: Boolean) = model.emitUserLogged(value)

    //--- Functions Aliases ---//////////////////////////////////////
    private inline fun toastFirebaseException(firebaseException: FirebaseException) =
        context.toast(firebaseException)

    private inline fun toast(@StringRes message: Int) = context.toast(message)

    //--- Public API --- ////////////////////////////////////////////
    fun signOut() {
        viewModelScope.launch {
            try {
                Firebase.auth.signOut()
            } catch (e: FirebaseException) {
                context.toast(e)
            }
        }
    }

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
        } catch (e: FirebaseAuthUserCollisionException) {
            toast(R.string.user_already_exist)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            toast(R.string.invalid_password)
        }

    }

    fun signIn() {
        context {
            signInEmail.validate {
                toast(R.string.toast_no_email)
            }
            signInPassword.validate {
                toast(R.string.toast_no_password)
            }
        }

        viewModelScope.launch {
            try {
                model.signIn(
                    email = signInEmail.value,
                    password = signInPassword.value
                )
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                toast(R.string.invalid_password)
            } catch (e: FirebaseException) {
                toastFirebaseException(e)
            } catch (e:Exception){
                log(e.stackTraceToString())
                toast(R.string.toast_unknown_error)
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

    fun refresh() {
        viewModelScope.launch {
            emitUserLogged(value = Firebase.auth.currentUser != null)

            model.getCurrentUserDocument().run{
                _friendsList
                    .emit(
                        value = try {
                            friends
                        } catch (e:FirebaseException){
                            toastFirebaseException(e)
                            emptyList()
                        }
                    )
                _email.emit(
                    value = try {
                        Firebase.auth.currentUser?.email ?: ""
                    } catch (e:FirebaseException){
                        toastFirebaseException(e)
                        ""
                    }
                )
                _login.emit(
                    value = try {
                        login
                    } catch(e:FirebaseException){
                        ""
                    }
                )
            }

        }
    }

    //--- Dialogs ---/////////////////////////////////////////////////
    fun openRegisterDialog() {
        if (logged.value)
            toast(R.string.user_already_registered)
        else
            viewModelScope.launch { registerWithEmailAndPasswordDialog.emit(true) }
    }

    fun openSignInDialog() {
        if (logged.value)
            toast(R.string.user_already_registered)
        else
            signInWithEmailAndPasswordDialog.value = true
    }

    fun openAddFriendDialog() {
        if (logged.value) viewModelScope.launch {
            addFriendDialog.emit(true)
        } else toast(R.string.toast_no_user)
    }

    fun hideRegisterDialog() {
        viewModelScope.launch {
            registerWithEmailAndPasswordDialog.emit(false)
        }
    }

    fun hideSignInDialog() {
        viewModelScope.launch {
            signInWithEmailAndPasswordDialog.emit(false)
        }
    }

    fun hideAddFriendDialog() {
        viewModelScope.launch {
            addFriendDialog.emit(false)
        }
    }

}