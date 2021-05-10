package com.example.remotelist.view.screens.account_screen.dialogs.friends_list

import androidx.lifecycle.ViewModel
import com.example.remotelist.repositories.FirestoreDataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FriendsListViewModel @Inject constructor() : ViewModel(), FriendsListController {
    override val friends: StateFlow<List<String>> = FirestoreDataProvider.friends
}