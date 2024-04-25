package com.example.projectpam

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.projectpam.fragments.ChatScreen
import com.example.projectpam.fragments.ChatsScreen
import com.example.projectpam.fragments.EventScreen
import com.example.projectpam.fragments.HomeScreen
import com.example.projectpam.fragments.LoginScreen
import com.example.projectpam.fragments.RegisterScreen
import com.example.projectpam.fragments.StartScreen
import com.example.projectpam.fragments.accountScreen
import com.example.projectpam.fragments.addEventScreen
import com.example.projectpam.fragments.yourevents
import com.example.projectpam.ui.theme.ProjectPAMTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var navController: NavHostController


    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectPAMTheme {
                val context = LocalContext.current
                navController = rememberNavController()
                val database = Firebase.database("https://projectpam-402a8-default-rtdb.europe-west1.firebasedatabase.app/")
                databaseReference = database.getReference("users")
                auth = Firebase.auth
                // Dodaj listener, aby sprawdzić, kiedy inicjalizacja zostanie zakończona
                auth.addAuthStateListener { firebaseAuth ->
                    if (firebaseAuth.currentUser != null) {
                        // Jeśli użytkownik jest zalogowany, nawiguj do "main"
                        navController.navigate("main") {
                            popUpTo("auth") {
                                inclusive = true
                            }
                        }
                    }
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = Screen.Authorization.route) {
                        navigation(
                            startDestination = Screen.Start.route,
                            route = Screen.Authorization.route
                        ) {
                            composable(route = Screen.Start.route) {
                                StartScreen(navController)
                            }
                            composable(route = Screen.Login.route) {
                                LoginScreen(navController, context, auth)
                            }
                            composable(route = Screen.Register.route) {
                                RegisterScreen(navController, context, auth, databaseReference)
                            }
                        }
                        navigation(
                            startDestination = Screen.Home.route,
                            route = Screen.Main.route
                        ) {
                            composable(route = Screen.Home.route) {
                                Scaffold(
                                    bottomBar = { BottomNavigationBar(navController = navController) }
                                ) {
                                    HomeScreen(navController, context)
                                }
                            }

                            composable(route = Screen.AddEvent.route) {
                                Scaffold(
                                    bottomBar = { BottomNavigationBar(navController = navController) }
                                ) {
                                    addEventScreen(navController, context)
                                }

                            }
                            composable(route = Screen.Chats.route) {
                                Scaffold(
                                    bottomBar = { BottomNavigationBar(navController = navController) }
                                ) {
                                    ChatsScreen(navController, context)
                                }
                            }
                            composable(route = "${Screen.Chat.route}/{userId}/{userName}") {
                                val userId = it.arguments?.getString("userId")
                                val userName = it.arguments?.getString("userName")
                                Scaffold(
                                   topBar = {
                                       TopAppBar(
                                           title = { Text("Back") },
                                           navigationIcon = {
                                               IconButton(onClick = { navController.popBackStack() }) {
                                                   Icon(
                                                       imageVector = Icons.Default.ArrowBack,
                                                       contentDescription = null
                                                   )
                                               }
                                           }
                                       )
                                   }
                                ) {
                                    ChatScreen(navController, userId = userId!!, userName = userName!!, context)
                                }



                            }
                            composable(route = "${Screen.Event.route}/{eventId}") {
                                val eventId = it.arguments?.getString("eventId") ?: ""
                                Scaffold(
                                    topBar = {
                                        TopAppBar(
                                            title = { Text("Back") },
                                            navigationIcon = {
                                                IconButton(onClick = { navController.popBackStack() }) {
                                                    Icon(
                                                        imageVector = Icons.Default.ArrowBack,
                                                        contentDescription = null
                                                    )
                                                }
                                            }
                                        )
                                    }
                                ) {
                                    EventScreen(eventId, navController, context)
                                }
                            }
                            composable(route = Screen.YourEvent.route) {
                                Scaffold(
                                    bottomBar = { BottomNavigationBar(navController = navController) }
                                ) {
                                    yourevents(navController, context)
                                }
                            }
                            composable(route = Screen.Account.route) {
                                Scaffold(
                                    bottomBar = { BottomNavigationBar(navController = navController) }
                                ) {
                                    accountScreen(navController)
                                }
                            }
                        }

                    }


                }

                }
            }
        }
    }







