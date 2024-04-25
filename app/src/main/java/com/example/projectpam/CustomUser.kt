package com.example.projectpam

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun CustomUser(
    navController: NavController,
    user : User
) {
    val context = LocalContext.current
    Row(modifier = Modifier
        .background(Color.LightGray)
        .clickable {
            navController.navigate("${Screen.Chat.route}/${user.userId}/${user.userName}")
        }
        .fillMaxWidth()
        .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Username: ${user.userName}")
    }
}


@Preview(showBackground = true)
@Composable
fun CustomUserPreview() {
    CustomUser(rememberNavController(), User("sadasre", "kociński"))
}