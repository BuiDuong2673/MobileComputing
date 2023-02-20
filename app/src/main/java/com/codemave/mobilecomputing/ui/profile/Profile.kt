package com.codemave.mobilecomputing.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codemave.mobilecomputing.data.entity.Account
import com.codemave.mobilecomputing.ui.login.getProfile
import com.codemave.mobilecomputing.ui.login.setProfile
import com.codemave.mobilecomputing.ui.signup.SignUpViewModel
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.launch

@Composable
fun Profile(
    navController: NavController,
    viewModel: SignUpViewModel = viewModel(),
    onBackPress: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val account = getProfile()
    val username = rememberSaveable{ mutableStateOf(account.name) }
    val password = rememberSaveable { mutableStateOf(account.password) }
    Surface(modifier = Modifier.fillMaxSize()) {
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
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { data -> password.value = data},
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                enabled = true,
                onClick = {
                    if ((username.value != account.name)
                        || password.value != account.password) {
                        coroutineScope.launch {
                            setProfile(
                                Account(
                                    id = account.id,
                                    name = username.value,
                                    password = password.value
                                ))
                            viewModel.editAccount(
                                Account(
                                    id = account.id,
                                    name = username.value,
                                    password = password.value
                                )
                            )
                            navController.navigate("home")
                        }
                        onBackPress()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .size(55.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Save",
                    color = Color.White,
                    fontSize = 30.sp
                )
            }
        }
    }
}