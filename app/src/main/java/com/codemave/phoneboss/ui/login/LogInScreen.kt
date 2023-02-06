package com.codemave.phoneboss.ui.login

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import com.google.accompanist.insets.systemBarsPadding
@Composable
fun LogInScreen(
    navController: NavController,
    sharedPreferences: SharedPreferences
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val newUsername = sharedPreferences.getString("username", "")
        val newPassword = sharedPreferences.getString("password", "")
        val username = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                tint = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = username.value,
                onValueChange = { data -> username.value = data},
                label = {Text("Username:")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { data -> password.value = data},
                label = {Text("Password:")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    if (username.value != "" && password.value != "" && newUsername == username.value && newPassword == password.value) {
                        navController.navigate("home")
                    }
                },
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(55.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Log In",
                    color = White,
                    fontSize = 30.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Please enter correct Username and Password to Login",
                fontWeight = Bold,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Don't have account?",
                fontWeight = Bold,
                color = MaterialTheme.colors.primary
            )
            Button(
                onClick = { navController.navigate("signup") },
                enabled = true,
                colors = buttonColors(White)
            ) {
                Text(
                    text = "Sign Up",
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp
                )
            }
        }
    }
}