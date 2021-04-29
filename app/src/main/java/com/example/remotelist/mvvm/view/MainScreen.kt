package com.example.remotelist.mvvm.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.remotelist.R
import com.example.remotelist.mvvm.viewmodel.AccountViewModel
import com.example.remotelist.mvvm.viewmodel.ListViewModel
import com.example.remotelist.navigation.AccountScreen
import com.example.remotelist.navigation.ListScreen
import com.example.remotelist.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun MainScreen(listViewModel: ListViewModel, accountViewModel: AccountViewModel) {
    var currentScreen: Screen by remember { mutableStateOf(ListScreen) }
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    fun Screen.navigateHere(){
        currentScreen = this
        navController.navigate(route)
    }
    fun DrawerState.openDrawer(){
        coroutineScope.launch { open() }
    }

    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(modifier = Modifier.fillMaxSize()) {

                Spacer(Modifier.height(16.dp))


                DrawerButton(
                    icon = Icons.Default.AccountBox,
                    text = stringResource(R.string.account_screen),
                    isSelected = currentScreen.route == AccountScreen.route,
                    onClick = AccountScreen::navigateHere
                )
                DrawerButton(
                    icon = Icons.Default.List,
                    text = stringResource(R.string.shopping_list),
                    isSelected = currentScreen.route == ListScreen.route,
                    onClick = ListScreen::navigateHere
                )

            }
        },
    ) {
        NavHost(navController = navController, startDestination = ListScreen.route) {

            composable(route = AccountScreen.route) {
                AccountScreen(
                    accountViewModel = accountViewModel,
                    onOpenDrawer = drawerState::openDrawer
                )
            }

            composable(route = ListScreen.route) {
                ListScreen(
                    listViewModel = listViewModel,
                    onOpenDrawer = drawerState::openDrawer
                )
            }

        }
    }
}

@Composable
private fun DrawerButton(
    icon: ImageVector,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colors

    val imageAlpha = if (isSelected) 1f else 0.6f

    val textIconColor = if (isSelected) colors.primary else colors.onSurface.copy(alpha = 0.6f)

    val backgroundColor = if (isSelected) colors.primary.copy(alpha = 0.12f) else Color.Transparent

    val surfaceModifier = modifier
        .padding(start = 8.dp, top = 8.dp, end = 8.dp)
        .fillMaxWidth()

    Surface(
        modifier = surfaceModifier,
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {

        TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Image(
                    imageVector = icon,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(textIconColor),
                    alpha = imageAlpha
                )
                Spacer(Modifier.width(16.dp))
                Text(text = text, style = MaterialTheme.typography.body2, color = textIconColor)

            }
        }
    }
}