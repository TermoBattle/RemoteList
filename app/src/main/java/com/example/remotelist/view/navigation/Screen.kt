package com.example.remotelist.view.navigation

sealed class Screen(val route:String)
object ListScreen: Screen("list_screen")
object AccountScreen: Screen("account_screen")