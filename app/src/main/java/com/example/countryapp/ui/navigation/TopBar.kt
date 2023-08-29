package com.example.countryapp.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.countryapp.R

//TODO: Solve the bug: - The menu should display also on Dashboard
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    onNavigationIconClick: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (currentRoute == null || currentRoute == Destinations.Splash.name) {
        return
    }

    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.empty))
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Image(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = null
                )
            }
        },
    )
}