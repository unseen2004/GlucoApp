package com.example.glucoapp.ui.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.glucoapp.data.UserPreferences
import com.example.glucoapp.navigation.Screen
import com.example.glucoapp.ui.theme.GlucoAppTheme
import com.example.glucoapp.ui.viewmodels.LoginState
import com.example.glucoapp.ui.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GlucoAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation(userPreferences)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(userPreferences: UserPreferences) {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginState by loginViewModel.loginState.collectAsState()
    val isDoctor by userPreferences.isDoctorLoggedIn.collectAsState(initial = false)

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Main.route) { MainScreen(navController, userPreferences = userPreferences) }
        composable(Screen.AddNote.route) { AddNoteScreen(navController) }
        composable(Screen.AddMeal.route) { AddMealScreen(navController) }
        composable(Screen.Notes.route) { NotesScreen(navController, userPreferences = userPreferences) }
        composable(Screen.Meals.route) { MealsScreen(navController, isDoctor = loginState is LoginState.DoctorSuccess) }
        composable(Screen.Settings.route) { SettingsScreen(navController, isDoctor = loginState is LoginState.DoctorSuccess) }
        composable(Screen.Register.route) { RegisterScreen(navController) }
    }

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> navController.navigate(Screen.Main.route)
            is LoginState.DoctorSuccess -> navController.navigate(Screen.Main.route)
            else -> Unit
        }
    }
}