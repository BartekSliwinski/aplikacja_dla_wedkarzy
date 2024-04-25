package com.example.projectpam

import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun BottomNavigationBar(navController: NavController) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Twoje,
        BottomNavItem.Add,
        BottomNavItem.Message,
        BottomNavItem.Account
    )

    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach() { item ->
            BottomNavigationItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                       BadgedBox(badge = {
                            if(item.hasNews) {
                                Badge()
                            }
                       }) {
                           Icon(imageVector = if(currentRoute == item.route) {
                                    item.selectedIcon
                           } else {
                                  item.unselectedIcon
                           },
                           contentDescription = item.title)
                       }
                },
                label = { Text(item.title) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtomPreview() {
    BottomNavigationBar(rememberNavController())
}