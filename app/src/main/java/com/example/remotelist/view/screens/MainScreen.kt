package com.example.remotelist.view.screens

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
import com.example.remotelist.view.screens.account_screen.main.AccountScreen
import com.example.remotelist.view.screens.list_screen.main.ListScreen
import com.example.remotelist.library.data_structures.AccountScreen
import com.example.remotelist.library.data_structures.ListScreen
import com.example.remotelist.library.data_structures.Screen
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    var currentScreen: Screen by remember { mutableStateOf(ListScreen) }
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    fun Screen.navigate(){
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
                    onClick = AccountScreen::navigate
                )
                DrawerButton(
                    icon = Icons.Default.List,
                    text = stringResource(R.string.shopping_list),
                    isSelected = currentScreen.route == ListScreen.route,
                    onClick = ListScreen::navigate
                )

            }
        },
    ) {
        NavHost(navController = navController, startDestination = ListScreen.route) {

            composable(route = AccountScreen.route) {
                AccountScreen(
                    onOpenDrawer = drawerState::openDrawer
                )
            }

            composable(route = ListScreen.route) {
                ListScreen(
                    onOpenDrawer = drawerState::openDrawer,
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