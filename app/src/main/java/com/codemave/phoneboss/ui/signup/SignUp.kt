package com.codemave.phoneboss.ui.signup

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codemave.phoneboss.ui.theme.Orange
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun SignUp(
    navController: NavController,
    sharedPreferences: SharedPreferences
) {

    Surface(modifier = Modifier.fillMaxSize()) {
        val username = rememberSaveable {mutableStateOf("")}
        val password = rememberSaveable {mutableStateOf("")}
        val editor = sharedPreferences.edit()
        editor.putString("username", username.value)
        editor.putString("password", password.value)
        editor.apply()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "CREATE ACCOUNT",
                color = Orange,
                fontSize = 40.sp,
                fontWeight = Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(50.dp))
            OutlinedTextField(
                value = username.value,
                onValueChange = {data -> username.value = data},
                label = { Text("Username:")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { data -> password.value = data },
                label = { Text("Password")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    navController.navigate("login") },
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(55.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Sign Up",
                    color = White,
                    fontSize = 30.sp
                )
            }
        }
    }
}