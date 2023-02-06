package com.codemave.phoneboss.ui
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codemave.phoneboss.PhoneBossAppState
import com.codemave.phoneboss.rememberPhoneBossAppState
import com.codemave.phoneboss.ui.home.Home
import com.codemave.phoneboss.ui.login.LogInScreen
import com.codemave.phoneboss.ui.payment.Payment
import com.codemave.phoneboss.ui.signup.SignUp
import com.codemave.phoneboss.ui.welcome.Welcome

@Composable
fun PhoneBossApp(
    appState: PhoneBossAppState = rememberPhoneBossAppState(),
    appPreferences: SharedPreferences
) {
    NavHost(
        navController = appState.navController,
        startDestination = "welcome"
    ) {
        composable(route = "welcome") {
            Welcome(navController = appState.navController)
        }
        composable(route = "login") {
            LogInScreen(navController = appState.navController, sharedPreferences = appPreferences)
        }
        composable(route = "signup") {
            SignUp(navController = appState.navController, sharedPreferences = appPreferences)
        }
        composable(route = "home") {
            Home(navController = appState.navController)
        }
        composable(route = "payment") {
            Payment(onBackPress = appState::navigateBack)
        }
    }
}