package com.example.remotelist.view.screens.list_screen.dialogs.choose_shopping_list_dialog

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remotelist.repositories.FirestoreDataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseShoppingListDialogViewModel @Inject constructor():ViewModel(), ChooseShoppingListDialogController {
    override val friends: StateFlow<List<String>> = FirestoreDataProvider.friends

    override fun chooseUser(context: Context, login: String) {
        viewModelScope.launch {
            FirestoreDataProvider.emitNewLogin(login)
        }
    }
}