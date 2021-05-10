package com.example.remotelist.view.screens.account_screen.dialogs.add_friend_dialog

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remotelist.library.common.launchAndHandleFirestoreExceptions
import com.example.remotelist.library.common.with
import com.example.remotelist.library.firebase.getSnapshot
import com.example.remotelist.repositories.FirestoreDataProvider.getCurrentUserDocumentReference
import com.example.remotelist.library.firebase.FRIENDS
import com.example.remotelist.library.firebase.friends
import com.google.firebase.firestore.SetOptions.merge
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddFriendDialogViewModel @Inject constructor() : ViewModel(), AddFriendDialogController {
    override val newFriendLogin: MutableState<String> = mutableStateOf("")

    override fun addFriend(context: Context, login: String) = context
        .launchAndHandleFirestoreExceptions(coroutineScope = viewModelScope) {
            val userDocument = getCurrentUserDocumentReference()

            val friendsList: List<String> = userDocument.getSnapshot().friends
            userDocument.set(
                hashMapOf(FRIENDS to friendsList.with(login)),
                merge()
            )
        }
}