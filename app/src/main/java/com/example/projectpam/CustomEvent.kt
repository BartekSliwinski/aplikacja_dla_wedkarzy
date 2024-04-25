package com.example.projectpam

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

@Composable
fun CustomEvent(
    event: Event,
    navController: NavController
) {
    val reference : DatabaseReference?
    var name: String by remember { mutableStateOf("") }
    reference = Firebase.database("https://projectpam-402a8-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("users").child(event.ownerId)
    reference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val user = snapshot.getValue(User::class.java)
            name = user!!.userName
        }

        override fun onCancelled(error: DatabaseError) {
            // Obsługa błędu, jeśli wystąpi
        }
    })
    Column(modifier = Modifier
        .background(Color.LightGray)
        .fillMaxWidth()
        .padding(24.dp)
        .clickable {
            navController.navigate("${Screen.Event.route}/${event.eventId}")
        }
    ) {
        Row {
            Text(text = "Użytkownik: $name  ")
            Text(text = "Wody: ${event.fishingSpot}")

        }
        Text(text = "Od:${event.beginDate} ${event.beginTime}")
        Text(text = "Do:${event.endingDate} ${event.endingTime}")
        Text(text = "Lokalizacja: ${event.localization}")
    }
}


@Preview(showBackground = true)
@Composable
fun CustomEventPreview() {
   CustomEvent(event = Event("asda","sadsaf","04-02-2024", "02:40", "07-02-2024", "02:40", "początkujący", "lubelskie", "państwowe"), rememberNavController())
}