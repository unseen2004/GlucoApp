package com.example.glucoapp.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.glucoapp.data.db.models.User
import com.example.glucoapp.navigation.Screen
import com.example.glucoapp.ui.viewmodels.LoginState
import com.example.glucoapp.ui.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
            is LoginState.DoctorSuccess -> {
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (loginState) {
            is LoginState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password5") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.login(username, password)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        navController.navigate(Screen.Register.route)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create Account")
                }

                if (loginState is LoginState.Error) {
                    Text("Error: ${(loginState as LoginState.Error).message}")
                }
            }
        }
    }
}