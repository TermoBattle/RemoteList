package com.example.remotelist.view.screens.account_screen.dialogs.user_data

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remotelist.R
import com.example.remotelist.library.jetpack_compose.innerPadding
import com.example.remotelist.library.jetpack_compose.plus
import kotlinx.coroutines.flow.StateFlow

@Composable
fun UserData(dialogController:UserDataController = viewModel<UserDataViewModel>()) {
    Card {
        Column(modifier = innerPadding) {
            val login by dialogController.login.collectAsState()
            val email by dialogController.email.collectAsState()

            Text(text = R.string.login + ": $login")
            Text(text = R.string.email + ": $email")
        }
    }
}

interface UserDataController {
    val email: StateFlow<String>
    val login: StateFlow<String>
}
