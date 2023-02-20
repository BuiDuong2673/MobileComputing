package com.codemave.mobilecomputing.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codemave.mobilecomputing.data.entity.Account
import com.codemave.mobilecomputing.ui.signup.SignUpViewModel
import com.google.accompanist.insets.systemBarsPadding
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codemave.mobilecomputing.R

@Composable
fun LogIn(
    navController: NavController,
    viewModel: SignUpViewModel = viewModel()
) {
    val viewState by viewModel.state.collectAsState()
    Surface(modifier = Modifier.fillMaxSize()) {
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
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_app),
                contentDescription = "app_icon",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit,
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
                onClick = {
                    val account = checkAccount(viewState.accounts, username.value, password.value)
                    if (viewState.accounts.isNotEmpty() && account != null) {
                        setProfile(account)
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
                    color = Color.White,
                    fontSize = 30.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Please enter correct Username and Password to Login",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Don't have account?",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )
            Button(
                onClick = { navController.navigate("signup") },
                enabled = true,
                colors = ButtonDefaults.buttonColors(Color.White)
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

fun checkAccount (accounts: List<Account>, username: String, password: String): Account? {
    return accounts.firstOrNull { account ->
        username != ""
        && password != ""
        && username == account.name
        && password == account.password
    }
}

var loginAccount: Account = Account(name = "Default name", password ="Default password")
fun setProfile(account : Account) {
    loginAccount = account
}
fun getProfile(): Account {
    return loginAccount
}