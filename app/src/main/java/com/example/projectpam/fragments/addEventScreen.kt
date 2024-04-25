package com.example.projectpam.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.projectpam.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun addEventScreen(
    navController : NavController,
    context: Context
) {
    var databaseReference : DatabaseReference?
    val firebaseUser: FirebaseUser?


    firebaseUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current
    var beginDate by remember { mutableStateOf("Dodaj czas") }
    val beginTime = remember { mutableStateOf("Dodaj") }
    var endDate by remember { mutableStateOf("Dodaj czas") }
    val endTime = remember { mutableStateOf("Dodaj") }

    var localization by remember { mutableStateOf("") }
    var locIsExpanded by remember { mutableStateOf(false) }
    val wojewodztwa = listOf(
        "dolnośląskie",
        "kujawsko-pomorskie",
        "lubelskie",
        "lubuskie",
        "łódzkie",
        "małopolskie",
        "mazowieckie",
        "opolskie",
        "podkarpackie",
        "podlaskie",
        "pomorskie",
        "śląskie",
        "świętokrzyskie",
        "warmińsko-mazurskie",
        "wielkopolskie",
        "zachodniopomorskie"
    )
    var textFilledSize1 by remember { mutableStateOf(Size.Zero) }

    val icon1 = if (locIsExpanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    var level by remember { mutableStateOf("") }
    var lvlIsExpanded by remember { mutableStateOf(false) }
    val levelList = listOf(
        "brak", "początkujący", "średnio-zaawansowany",
        "zaawansowany"
    )
    var textFilledSize2 by remember { mutableStateOf(Size.Zero) }

    val icon2 = if (lvlIsExpanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }


    var plcIsExpanded by remember { mutableStateOf(false) }
    var place by remember { mutableStateOf("") }
    val list = listOf(
        "komercyjne",
        "państwowe"
    )
    var textFilledSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (plcIsExpanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val myCalendar = Calendar.getInstance()
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd-MM-yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale("pl", "PL"))
            beginDate = sdf.format(myCalendar.time).toString()
        }
        val datePicker1 = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd-MM-yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale("pl", "PL"))
            endDate = sdf.format(myCalendar.time).toString()
        }
        Text(
            text = "Stwórz swoje ogłoszenie!",
            modifier = Modifier
        )
        Text(text = "Data i godzina rozpoczęcia")
        Row {
            Button(onClick = {
                DatePickerDialog(
                    context,
                    datePicker,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }) {
                Text(text = "Data")
            }
            Text(text = beginDate)

            Button(onClick = {
                val hour = myCalendar[Calendar.HOUR_OF_DAY]
                val minute = myCalendar[Calendar.MINUTE]
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, hour: Int, minute: Int ->
                        val formattedHour = if (hour < 10) "0$hour" else "$hour"
                        val formattedMinute = if (minute < 10) "0$minute" else "$minute"
                        beginTime.value = "$formattedHour:$formattedMinute"
                    }, hour, minute, true
                ).show()
            }) {
                Text(text = "Godzina")
            }
            Text(text = beginTime.value)
        }
        Text(text = "Data i godzina zakończenia")
        Row {
            Button(onClick = {
                DatePickerDialog(
                    context, datePicker1, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }) {
                Text(text = "Data")
            }
            Text(text = endDate)

            Button(onClick = {
                val hour = myCalendar[Calendar.HOUR_OF_DAY]
                val minute = myCalendar[Calendar.MINUTE]
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, hour: Int, minute: Int ->
                        val formattedHour = if (hour < 10) "0$hour" else "$hour"
                        val formattedMinute = if (minute < 10) "0$minute" else "$minute"
                        endTime.value = "$formattedHour:$formattedMinute"
                    }, hour, minute, true
                ).show()
            }) {
                Text(text = "Godzina")
            }
            Text(text = endTime.value)
        }





        Column(modifier = Modifier.padding(20.dp)) {


            OutlinedTextField(
                value = localization,
                onValueChange = { localization = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFilledSize1 = coordinates.size.toSize()
                    },
                readOnly = true,
                label = { Text(text = "Lokalizacja") },
                trailingIcon = {
                    Icon(icon1, "", Modifier.clickable { locIsExpanded = !locIsExpanded })
                }

            )

            DropdownMenu(
                expanded = locIsExpanded,
                onDismissRequest = { locIsExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFilledSize1.width.toDp() })
            ) {
                wojewodztwa.forEach() { label ->
                    DropdownMenuItem(onClick = {
                        localization = label
                        locIsExpanded = false
                    }) {
                        Text(text = label)
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {


            OutlinedTextField(
                value = level,
                onValueChange = { level = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFilledSize2 = coordinates.size.toSize()
                    },
                readOnly = true,
                label = { Text(text = "Zaawansowanie") },
                trailingIcon = {
                    Icon(icon2, "", Modifier.clickable { lvlIsExpanded = !lvlIsExpanded })
                }

            )

            DropdownMenu(
                expanded = lvlIsExpanded,
                onDismissRequest = { lvlIsExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFilledSize2.width.toDp() })
            ) {
                levelList.forEach() { label ->
                    DropdownMenuItem(onClick = {
                        level = label
                        lvlIsExpanded = false
                    }) {
                        Text(text = label)
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {


            OutlinedTextField(
                value = place,
                onValueChange = { place = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFilledSize = coordinates.size.toSize()
                    },
                readOnly = true,
                label = { Text(text = "Łowisko") },
                trailingIcon = {
                    Icon(icon, "", Modifier.clickable { plcIsExpanded = !plcIsExpanded })
                }

            )

            DropdownMenu(
                expanded = plcIsExpanded,
                onDismissRequest = { plcIsExpanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFilledSize.width.toDp() })
            ) {
                list.forEach() { label ->
                    DropdownMenuItem(onClick = {
                        place = label
                        plcIsExpanded = false
                    }) {
                        Text(text = label)
                    }
                }
            }
        }

        Button(onClick = {
            if (beginDate == "Dodaj czas" || beginTime.value == "Dodaj" ||
                endDate == "Dodaj czas" || endTime.value == "Dodaj" ||
                localization.isBlank() || level.isBlank() || place.isBlank()
            ) {
                Toast.makeText(
                    context,
                    "Wypełnij wszystkie pola przed dodaniem ogłoszenia",
                    Toast.LENGTH_SHORT
                ).show()
                return@Button
            }

            val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            val beginLocalDateTime =
                LocalDateTime.parse("$beginDate ${beginTime.value}", dateTimeFormatter)
            val endLocalDateTime =
                LocalDateTime.parse("$endDate ${endTime.value}", dateTimeFormatter)
            if (endLocalDateTime.isBefore(beginLocalDateTime)) {
                Toast.makeText(context, "Zakończenie wydarzenia musi być po jego rozpoczęciu", Toast.LENGTH_SHORT).show()
                return@Button
            }
            val database = Firebase.database("https://projectpam-402a8-default-rtdb.europe-west1.firebasedatabase.app/")
            databaseReference = database.getReference("events")

            val eventId = databaseReference!!.push().key

            val hashMap:HashMap<String, String> = HashMap()
            hashMap.put("eventId", eventId!!)
            hashMap.put("ownerId", firebaseUser!!.uid)
            hashMap.put("beginDate", beginDate)
            hashMap.put("beginTime", beginTime.value)
            hashMap.put("endingDate", endDate)
            hashMap.put("endingTime", endTime.value)
            hashMap.put("level", level)
            hashMap.put("localization", localization)
            hashMap.put("fishingSpot", place)
            databaseReference!!.child(eventId).setValue(hashMap)
            Toast.makeText(context, "Dodano ogłoszenie!", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.YourEvent.route)
        }) {
            Text(text = "Dodaj ogloszenie")
        }

    }
}




@Preview(showBackground = true)
@Composable
fun addEventPreview() {
    addEventScreen(rememberNavController(), LocalContext.current)
}