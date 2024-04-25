package com.example.projectpam.fragments

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.projectpam.Event
import com.example.projectpam.Screen
import com.example.projectpam.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


@Composable
fun EventScreen(
    eventId: String,
    navController: NavController,
    context : Context
) {
    var EventId: String by remember { mutableStateOf("") }
    var ownerId: String by remember { mutableStateOf("") }
    var beginDate: String by remember { mutableStateOf("") }
    var beginTime: String by remember { mutableStateOf("") }
    var endingDate: String by remember { mutableStateOf("") }
    var endingTime: String by remember { mutableStateOf("") }
    var fishingSpot: String by remember { mutableStateOf("") }
    var level: String by remember { mutableStateOf("") }
    var localization: String by remember { mutableStateOf("") }
    var userId: String by remember { mutableStateOf("") }
    var name: String by remember { mutableStateOf("") }

    val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    val useridd = firebase.uid
    val reference : DatabaseReference?
    val dbReference : DatabaseReference?
    dbReference = Firebase.database("https://projectpam-402a8-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("events").child(eventId)
    Log.d("kot", "koty1")
    dbReference.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val event = snapshot.getValue(Event::class.java)!!
            EventId = event.eventId
            ownerId = event.ownerId
            beginTime = event.beginTime
            beginDate = event.beginDate
            endingDate = event.endingDate
            endingTime = event.endingTime
            fishingSpot = event.fishingSpot
            level = event.level
            localization = event.localization
            Log.d("kot", "kotyc ${event.eventId}")
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })
    Log.d("kot", "koty3")
    reference = Firebase.database("https://projectpam-402a8-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("users").child(ownerId)
    reference.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val user = snapshot.getValue(User::class.java)!!
            name = user.userName
            userId = user.userId
            //Log.d("kot", "kotyc ${userId}")
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })


    Column(
        modifier = Modifier
            .fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Użytkownik: ${name}  ")
            Text(text = "Wody: ${fishingSpot}")

        }

        Text(text = "Od:${beginDate} ${beginTime}")
        Text(text = "Do:${endingDate} ${endingTime}")
        Text(text = "Lokalizacja: ${localization}")
        if(ownerId != useridd) {
            Button(onClick = {
                navController.navigate("${Screen.Chat.route}/${userId}/${name}")
            }) {
                Text(text = "Napisz wiadomość")
            }
        } else {
            Button(onClick = {
                navController.navigate(Screen.Home.route)
                deleteEvent(eventId, context)
            }) {
                Text(text = "Usuń wydarzenie")
            }
        }
    }
}


fun deleteEvent(eventId: String, context: Context) {
    val eventReference = Firebase.database("https://projectpam-402a8-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("events").child(eventId)

    eventReference.removeValue()
        .addOnSuccessListener {
            // Usunięto dane pomyślnie
            Toast.makeText(context, "Usunięto pomyślnie!", Toast.LENGTH_SHORT).show()
            println("Event deleted successfully!")
        }
        .addOnFailureListener { error ->
            // Wystąpił błąd podczas usuwania danych
            Toast.makeText(context, "Błąd: $error", Toast.LENGTH_SHORT).show()
        }
}
