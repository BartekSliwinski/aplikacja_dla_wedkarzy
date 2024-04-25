package com.example.projectpam.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.projectpam.Screen
import com.example.projectpam.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

@Composable
fun accountScreen(navController: NavController) {

    val reference : DatabaseReference?
    val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    val userid = firebase.uid
    var name: String by remember { mutableStateOf("") }

    reference = Firebase.database("https://projectpam-402a8-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("users").child(userid)
    reference.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val user = snapshot.getValue(User::class.java)!!
            name = user.userName
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Witaj, $name"
        )
        Button(onClick = {
            navController.navigate(Screen.Authorization.route) {
                /*popUpTo("main") {
                    inclusive = true
                }*/
            }
            Firebase.auth.signOut()
        }) {
            Text(text = "Wyloguj się")
        }
    }
}