package com.example.remotelist.view.screens.account_screen.dialogs.user_data

import androidx.lifecycle.ViewModel
import com.example.remotelist.repositories.FirestoreDataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor():ViewModel(), UserDataController {
    override val email: StateFlow<String> = FirestoreDataProvider.email
    override val login: StateFlow<String> = FirestoreDataProvider.login
}