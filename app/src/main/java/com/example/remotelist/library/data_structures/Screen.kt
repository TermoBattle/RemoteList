package com.example.remotelist.library.data_structures

sealed class Screen(val route:String)
object ListScreen: Screen("list_screen")
object AccountScreen: Screen("account_screen")