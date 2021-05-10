package com.example.remotelist.view.screens.list_screen.main

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remotelist.library.common.launchAndHandleFirestoreExceptions
import com.example.remotelist.repositories.FirestoreDataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ListScreenViewModel @Inject constructor() :
    ViewModel(), ListScreenController {

    override val friendsCount: StateFlow<Int> = FirestoreDataProvider.friendsCount

    override val chooseFriendDialog = mutableStateOf(false)
    override val newShoppingItemDialog = mutableStateOf(false)

    override fun refresh(context: Context) = context
        .launchAndHandleFirestoreExceptions(
            coroutineScope = viewModelScope,
            block = FirestoreDataProvider::refresh
        )

}