package com.example.projectpam.fragments

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projectpam.CustomUser
import com.example.projectpam.pushnotification.FirebaseService
import com.example.projectpam.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessaging


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    navController: NavController,
    context: Context
) {
    // Używamy mutableStateOf do przechowywania listy użytkowników
    val userListState = remember { mutableStateOf<List<User>>(emptyList()) }
    FirebaseService.sharedPref = context.getSharedPreferences("sharedPref",Context.MODE_PRIVATE)
    FirebaseMessaging.getInstance().token.addOnSuccessListener { result ->
        if(result != null){
            FirebaseService.token = result
        }
    }

    // Wywołujemy funkcję asynchroniczną getUsersList i ustawiamy wynik w userListState.value
    LaunchedEffect(Unit) {
        getUsersList(userListState, context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(userListState.value.isEmpty()) {
            Text(text = "Tu pojawią się twoje wiadomości")
        }
        // Wykorzystujemy userListState.value do odczytu listy użytkowników
        LazyColumn(modifier = Modifier
            .padding(all = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = userListState.value) { user ->
                CustomUser(navController, user = user)
            }
        }
    }
}

fun getUsersList(
    userListState: MutableState<List<User>>,
    context: Context
) {
    val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    //val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    val database = Firebase.database("https://projectpam-402a8-default-rtdb.europe-west1.firebasedatabase.app/")
    val databaseReference = database.getReference("users")
    val userid = firebase.uid
    FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")

    databaseReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val users = mutableListOf<User>()

            for (datasnaphot: DataSnapshot in snapshot.children) {
                val user = datasnaphot.getValue(User::class.java)


                if (user!!.userId != firebase.uid) {
                    users.add(user)
                }
            }

            // Ustawiamy nową listę użytkowników w userListState.value
            userListState.value = users
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
    })
}


@Preview(showBackground = true)
@Composable
fun ChatsPreview() {
    ChatsScreen(navController = rememberNavController(), LocalContext.current)
}