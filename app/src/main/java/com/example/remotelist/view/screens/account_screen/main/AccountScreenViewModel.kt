package com.example.remotelist.view.screens.account_screen.main

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remotelist.library.common.launchAndHandleFirestoreExceptions
import com.example.remotelist.repositories.FirebaseAuthentication
import com.example.remotelist.repositories.FirestoreDataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AccountScreenViewModel @Inject constructor() :
    ViewModel(), AccountScreenController {

    override val registerWithEmailAndPasswordDialog = mutableStateOf(false)
    override val signInWithEmailAndPasswordDialog = mutableStateOf(false)
    override val addFriendDialog = mutableStateOf(false)

    override val logged: StateFlow<Boolean> = FirestoreDataProvider.logged

    override fun signOut(context: Context) = context
        .launchAndHandleFirestoreExceptions(
            coroutineScope = viewModelScope,
            block = FirebaseAuthentication::signOut
        )

    override fun refresh(context: Context) = context
        .launchAndHandleFirestoreExceptions(
            coroutineScope = viewModelScope,
            block = FirestoreDataProvider::refresh
        )

}