package com.example.projectpam

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

open class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean
)
{

    object Home : BottomNavItem(Screen.Home.route, "Home", Icons.Filled.Home, Icons.Outlined.Home, false)
    object Twoje : BottomNavItem(Screen.YourEvent.route, "Twoje wydarzenia", Icons.Filled.DateRange, Icons.Outlined.DateRange, false)
    object Add : BottomNavItem(Screen.AddEvent.route, "Dodaj", Icons.Filled.Add, Icons.Outlined.Add, false)
    object Message : BottomNavItem(Screen.Chats.route, "Message", Icons.Filled.Email, Icons.Outlined.Email, false)
    object Account : BottomNavItem(Screen.Account.route, "Konto", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle, false)

}
