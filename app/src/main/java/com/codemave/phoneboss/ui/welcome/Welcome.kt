package com.codemave.phoneboss.ui.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codemave.phoneboss.ui.theme.Gray
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun Welcome(
    navController: NavController
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "WELCOME\nTO\nPHONEBOSS!",
                color = MaterialTheme.colors.primary,
                fontSize = 50.sp,
                fontWeight = Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(100.dp))
            Button(
                onClick = { navController.navigate("login")},
                enabled = true,
                modifier = Modifier.fillMaxWidth().size(55.dp),
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = "Log In",
                    color = White,
                    fontSize = 30.sp
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = { navController.navigate("signup") },
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(55.dp),
                shape = MaterialTheme.shapes.small,
                colors = buttonColors(Gray)
            ) {
                Text(
                    text = "Sign Up",
                    color = MaterialTheme.colors.primary,
                    fontSize = 30.sp
                )
            }
        }
    }
}