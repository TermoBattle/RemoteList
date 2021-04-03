package com.example.remotelist

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.remotelist.view.screens.MainScreen
import com.example.remotelist.view.theme.RemoteListTheme
import com.example.remotelist.viewmodel.AccountViewModel
import com.example.remotelist.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val accountViewModel: AccountViewModel by viewModels()
    private val listViewModel:ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RemoteListTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(listViewModel, accountViewModel)
                }
            }
        }

    }

}