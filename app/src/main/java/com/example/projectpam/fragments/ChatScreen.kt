package com.example.projectpam.fragments

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projectpam.Chat
import com.example.projectpam.pushnotification.PushNotification
import com.example.projectpam.pushnotification.RetrofitInstance
import com.example.projectpam.User
import com.example.projectpam.left_message
import com.example.projectpam.right_message
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavHostController,
    userId: String,
    userName: String,
    context : Context
) {


    Log.d("kot", "koty")
    var topic = ""
    var name: String by remember { mutableStateOf("") }
    val firebaseUser: FirebaseUser?
    val reference : DatabaseReference?
    var message by remember { mutableStateOf("") }

    val chatListState = remember { mutableStateOf<List<Chat>>(emptyList()) }
    firebaseUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(Unit) {
        readMessage(chatListState,firebaseUser!!.uid, userId)
    }

    reference = Firebase.database("https://projectpam-402a8-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("users").child(userId)


    reference.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val user = snapshot.getValue(User::class.java)
            name = user!!.userName
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(45.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                // Twoja zawartość LazyColumn
                items(chatListState.value) { chat ->
                    if(chat.senderId != firebaseUser!!.uid) {
                    left_message(chat)
                    } else {
                        right_message(chat)
                    }
                }
            }
        }

        // TextField na dole strony
        Row {
            TextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .padding(8.dp)
            )
            Button(
                onClick = {
                    if(TextUtils.isEmpty(message)) {
                        Toast.makeText(context, "Enter message", Toast.LENGTH_SHORT).show()
                        message = ""
                        return@Button
                    } else {
                        sendMessage(firebaseUser!!.uid, userId, message)
                        message = ""
                        topic = "/topics/$userId"
                        /*PushNotification(NotificationData(userName, message), topic).also {
                            sendNotification(context, it)
                        }*/
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send Message"
                )
            }
        }
        
    }


}

private fun sendMessage(senderId: String, receiverId: String, message:String) {

    val reference: DatabaseReference = Firebase.database("https://projectpam-402a8-default-rtdb.europe-west1.firebasedatabase.app/")
    .getReference()

    val hashMap: HashMap<String, String> = HashMap()
    hashMap.put("senderId", senderId)
    hashMap.put("receiverId", receiverId)
    hashMap.put("message", message)

    reference.child("chat").push().setValue(hashMap)
}
fun readMessage(chatListState: MutableState<List<Chat>>, senderId: String, receiverId: String) {
    val reference: DatabaseReference = Firebase.database("https://projectpam-402a8-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("chat")

    reference.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val chatList = mutableListOf<Chat>()

            for (datasnaphot: DataSnapshot in snapshot.children) {
                val chat = datasnaphot.getValue(Chat::class.java)

                if (chat!!.senderId.equals(senderId) && chat.receiverId.equals(receiverId) ||
                    chat.senderId.equals(receiverId) && chat.receiverId.equals(senderId)) {
                    chatList.add(chat)

                }
            }

            // Ustawiamy nową listę użytkowników w userListState.value
            chatListState.value = chatList
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })
    }

private fun sendNotification(context: Context,notification: PushNotification) = CoroutineScope(
    Dispatchers.IO).launch {
    try {
        val response = RetrofitInstance.api.postNotification(notification)
        if(response.isSuccessful) {
            Toast.makeText(context, "Response ${Gson().toJson(response)}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
        }
    } catch (e:Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }
}


@Preview(showBackground = true)
@Composable
fun ChatPreview() {
    ChatScreen(navController = rememberNavController(), "", "",LocalContext.current)
}