package com.example.projectpam

sealed class Screen(val route : String) {
    object Authorization: Screen(route = "auth")
    object Start : Screen(route = "start_screen")
    object Login : Screen(route = "login_screen")
    object Register : Screen(route = "register_screen")

    object Main : Screen(route = "main")
    object Home : Screen(route = "home_screen")
    object AddEvent : Screen(route = "add_event_screen")
    object Chats : Screen(route = "chats_screen")
    object Chat : Screen(route = "chat_screen")
    object Event : Screen(route = "event_screen")
    object YourEvent : Screen(route = "your_screen")
    object Account : Screen(route = "account_screen")
}
