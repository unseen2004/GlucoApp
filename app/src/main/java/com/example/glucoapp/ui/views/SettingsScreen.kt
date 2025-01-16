package com.example.glucoapp.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.glucoapp.data.db.models.InsulinType
import com.example.glucoapp.data.db.models.PredefinedMeal
import com.example.glucoapp.navigation.Screen
import com.example.glucoapp.presentation.viewmodels.SettingsUiState
import com.example.glucoapp.presentation.viewmodels.SettingsViewModel
import com.example.glucoapp.ui.viewmodels.LoginState
import com.example.glucoapp.ui.viewmodels.MainViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val uiState by settingsViewModel.uiState.collectAsState()
    val insulinTypes by settingsViewModel.insulinTypes.collectAsState()
    val predefinedMeals by settingsViewModel.predefinedMeals.collectAsState()
    val loginState by mainViewModel.loginState.collectAsState()

    var showAddInsulinTypeDialog by remember { mutableStateOf(false) }
    var showAddPredefinedMealDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showChangeDoctorPasswordDialog by remember { mutableStateOf(false) }
    var showChangeLoginDialog by remember { mutableStateOf(false) }
    var currentUserId by remember { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        settingsViewModel.loadCurrentUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (val state = uiState) {
            is SettingsUiState.Loading -> {
                CircularProgressIndicator()
            }
            is SettingsUiState.Success -> {
                currentUserId = state.user.userId
            }
            is SettingsUiState.Error -> {
                Text("Error: ${state.message}")
            }
        }
        // Display if the user is logged in as a doctor or a regular user
        when (loginState) {
            is LoginState.DoctorSuccess -> {
                Text("Logged in as Doctor", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
            }
            is LoginState.Success -> {
                Text("Logged in as User", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
            }
            else -> {
                Text("", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Button(
            onClick = { showAddInsulinTypeDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add/Edit Insulin Type")
        }

        Button(
            onClick = { showAddPredefinedMealDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add/Edit Predefined Meal")
        }

        Button(
            onClick = { showChangePasswordDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change Password")
        }

        Button(
            onClick = { showChangeDoctorPasswordDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change Doctor Password")
        }

        Button(
            onClick = { showChangeLoginDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change Login")
        }

        Button(
            onClick = {
                mainViewModel.logout()
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Main.route) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }

        if (showAddInsulinTypeDialog) {
            AddInsulinTypeDialog(
                onDismiss = { showAddInsulinTypeDialog = false },
                onSave = { newInsulinType ->
                    settingsViewModel.insertInsulinType(newInsulinType)
                    showAddInsulinTypeDialog = false
                },
                insulinTypes = insulinTypes,
                onDelete = { insulinType ->
                    settingsViewModel.deleteInsulinType(insulinType)
                }
            )
        }

        if (showAddPredefinedMealDialog) {
            AddPredefinedMealDialog(
                onDismiss = { showAddPredefinedMealDialog = false },
                onSave = { newPredefinedMeal ->
                    settingsViewModel.insertPredefinedMeal(newPredefinedMeal)
                    showAddPredefinedMealDialog = false
                },
                predefinedMeals = predefinedMeals,
                onDelete = { predefinedMeal ->
                    settingsViewModel.deletePredefinedMeal(predefinedMeal)
                }
            )
        }

        if (showChangePasswordDialog) {
            ChangePasswordDialog(
                onDismiss = { showChangePasswordDialog = false },
                onSave = { newPassword ->
                    settingsViewModel.updatePassword(currentUserId, newPassword)
                    showChangePasswordDialog = false
                }
            )
        }

        if (showChangeDoctorPasswordDialog) {
            ChangeDoctorPasswordDialog(
                onDismiss = { showChangeDoctorPasswordDialog = false },
                onSave = { newPassword ->
                    settingsViewModel.updateDoctorPassword(currentUserId, newPassword)
                    showChangeDoctorPasswordDialog = false
                }
            )
        }

        if (showChangeLoginDialog) {
            ChangeLoginDialog(
                onDismiss = { showChangeLoginDialog = false },
                onSave = { newUsername ->
                    settingsViewModel.updateLogin(currentUserId, newUsername)
                    showChangeLoginDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeLoginDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var newUsername by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Login") },
        text = {
            Column {
                OutlinedTextField(
                    value = newUsername,
                    onValueChange = { newUsername = it },
                    label = { Text("New Username") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(newUsername)
                },
                enabled = newUsername.isNotEmpty()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInsulinTypeDialog(
    onDismiss: () -> Unit,
    onSave: (InsulinType) -> Unit,
    insulinTypes: List<InsulinType>,
    onDelete: (InsulinType) -> Unit
) {
    var typeName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Insulin Type") },
        text = {
            Column {
                OutlinedTextField(
                    value = typeName,
                    onValueChange = { typeName = it },
                    label = { Text("Insulin Type Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(insulinTypes) { insulinType ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(insulinType.typeName, modifier = Modifier.weight(1f))
                            IconButton(onClick = { onDelete(insulinType) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSave(InsulinType(typeName = typeName)) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPredefinedMealDialog(
    onDismiss: () -> Unit,
    onSave: (PredefinedMeal) -> Unit,
    predefinedMeals: List<PredefinedMeal>,
    onDelete: (PredefinedMeal) -> Unit
) {
    var foodName by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var kcal by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Predefined Meal") },
        text = {
            Column {
                OutlinedTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = { Text("Food Name") }
                )
                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it },
                    label = { Text("Protein") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = carbs,
                    onValueChange = { carbs = it },
                    label = { Text("Carbs") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = fat,
                    onValueChange = { fat = it },
                    label = { Text("Fat") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = kcal,
                    onValueChange = { kcal = it },
                    label = { Text("Kcal") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(predefinedMeals) { meal ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(meal.foodName, modifier = Modifier.weight(1f))
                            IconButton(onClick = { onDelete(meal) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(PredefinedMeal(
                    foodName = foodName,
                    protein = protein.toDoubleOrNull() ?: 0.0,
                    carbs = carbs.toDoubleOrNull() ?: 0.0,
                    fat = fat.toDoubleOrNull() ?: 0.0,
                    kcal = kcal.toDoubleOrNull() ?: 0.0,
                    predefinedMealId = 0,
                    imagePath = null
                ))
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Password") },
        text = {
            Column {
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = confirmNewPassword,
                    onValueChange = { confirmNewPassword = it },
                    label = { Text("Confirm New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (passwordError) {
                    Text("Passwords do not match", color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newPassword == confirmNewPassword) {
                        onSave(newPassword)
                    } else {
                        passwordError = true
                    }
                },
                enabled = newPassword.isNotEmpty() && confirmNewPassword.isNotEmpty()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeDoctorPasswordDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Doctor Password") },
        text = {
            Column {
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Doctor Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = confirmNewPassword,
                    onValueChange = { confirmNewPassword = it },
                    label = { Text("Confirm New Doctor Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (passwordError) {
                    Text("Passwords do not match", color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newPassword == confirmNewPassword) {
                        onSave(newPassword)
                    } else {
                        passwordError = true
                    }
                },
                enabled = newPassword.isNotEmpty() && confirmNewPassword.isNotEmpty()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}