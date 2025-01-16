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
import com.example.glucoapp.navigation.Screen
import com.example.glucoapp.presentation.views.SettingsScreen
import com.example.glucoapp.ui.theme.GlucoAppTheme
import com.example.glucoapp.ui.viewmodels.LoginState
import com.example.glucoapp.ui.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GlucoAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginState by loginViewModel.loginState.collectAsState()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Main.route) { MainScreen(navController) }
        composable(Screen.AddNote.route) { AddNoteScreen(navController) }
        composable(Screen.AddMeal.route) { AddMealScreen(navController) }
        composable(Screen.Notes.route) { NotesScreen(navController) }
        composable(Screen.Meals.route) { MealsScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
    }

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> navController.navigate(Screen.Main.route)
            is LoginState.DoctorSuccess -> navController.navigate(Screen.Main.route)
            else -> Unit
        }
    }
}