package com.example.projectpam.fragments

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projectpam.CustomEvent
import com.example.projectpam.Event
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

@Composable
fun yourevents(
    navController: NavController,
    context : Context
) {
    val eventListState = remember { mutableStateOf<List<Event>>(emptyList()) }

    LaunchedEffect(Unit) {
        getYourEventList(eventListState, context)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(eventListState.value.isEmpty()) {
            Text(text = "Tu pojawią się twoje wydarzenia")
        }
        LazyColumn(
            Modifier.padding(all = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(eventListState.value) { event ->
                CustomEvent(event = event, navController)
            }
        }
    }

}

fun getYourEventList(
    eventListState: MutableState<List<Event>>,
    context: Context
) {
    val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    val db = Firebase.database("https://projectpam-402a8-default-rtdb.europe-west1.firebasedatabase.app/")
    val dbReference = db.getReference("events")



    dbReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val events = mutableListOf<Event>()
            for (datasnaphot: DataSnapshot in snapshot.children) {
                val event = datasnaphot.getValue(Event::class.java)
                if (event != null && event!!.ownerId == firebase.uid) {
                    events.add(event)
                }
            }

            // Ustawiamy nową listę użytkowników w userListState.value
            eventListState.value = events
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
    })
}