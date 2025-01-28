package com.example.glucoapp.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.glucoapp.data.db.models.Activity
import com.example.glucoapp.data.db.models.InsulinType
import com.example.glucoapp.data.db.models.Ingredient
import com.example.glucoapp.ui.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var showAddInsulinDialog by remember { mutableStateOf(false) }
    var showAddIngredientDialog by remember { mutableStateOf(false) }
    var showDeleteInsulinDialog by remember { mutableStateOf(false) }
    var showDeleteIngredientDialog by remember { mutableStateOf(false) }
    var showAddActivityDialog by remember { mutableStateOf(false) }
    var showDeleteActivityDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showAddInsulinDialog = true }) {
            Text("Add Insulin")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { showAddIngredientDialog = true }) {
            Text("Add Ingredient")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { showDeleteInsulinDialog = true }) {
            Text("Delete Insulin")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { showDeleteIngredientDialog = true }) {
            Text("Delete Ingredient")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { showAddActivityDialog = true }) {
            Text("Add Activity")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { showDeleteActivityDialog = true }) {
            Text("Delete Activity")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            navController.navigate("login") {
                popUpTo("settings") { inclusive = true }
            }
        }) {
            Text("Logout")
        }
    }

    if (showAddInsulinDialog) {
        AddInsulinDialog(onDismiss = { showAddInsulinDialog = false }, viewModel = viewModel)
    }

    if (showAddIngredientDialog) {
        AddIngredientDialog(onDismiss = { showAddIngredientDialog = false }, viewModel = viewModel)
    }

    if (showDeleteInsulinDialog) {
        DeleteInsulinDialog(onDismiss = { showDeleteInsulinDialog = false }, viewModel = viewModel)
    }

    if (showDeleteIngredientDialog) {
        DeleteIngredientDialog(onDismiss = { showDeleteIngredientDialog = false }, viewModel = viewModel)
    }

    if (showAddActivityDialog) {
        AddActivityDialog(onDismiss = { showAddActivityDialog = false }, viewModel = viewModel)
    }

    if (showDeleteActivityDialog) {
        DeleteActivityDialog(onDismiss = { showDeleteActivityDialog = false }, viewModel = viewModel)
    }
}

@Composable
fun AddActivityDialog(onDismiss: () -> Unit, viewModel: SettingsViewModel) {
    var activityType by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Activity") },
        text = {
            Column {
                OutlinedTextField(
                    value = activityType,
                    onValueChange = { activityType = it },
                    label = { Text("Activity Type") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duration (minutes)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                viewModel.addActivity(Activity(
                    activityType = activityType,
                    duration = duration.toInt(),
                    notes = notes,
                    userId = 1, // Replace with actual user ID
                    timestamp = System.currentTimeMillis()
                ))
                onDismiss()
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DeleteActivityDialog(onDismiss: () -> Unit, viewModel: SettingsViewModel) {
    val activities by viewModel.activities.collectAsState(initial = emptyList())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Activity") },
        text = {
            LazyColumn {
                items(activities) { activity ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(activity.activityType)
                        Button(onClick = {
                            viewModel.deleteActivity(activity)
                            onDismiss()
                        }) {
                            Text("Delete")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var newPassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Password") },
        text = {
            Column {
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(newPassword) }) {
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

@Composable
fun ChangeDoctorPasswordDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var newPassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Doctor Password") },
        text = {
            Column {
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Doctor Password") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(newPassword) }) {
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

@Composable
fun AddInsulinDialog(onDismiss: () -> Unit, viewModel: SettingsViewModel) {
    var insulinName by remember { mutableStateOf("") }
    var typeName by remember { mutableStateOf("") }
    val userId by viewModel.userId.collectAsState(initial = null)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Insulin") },
        text = {
            Column {
                OutlinedTextField(
                    value = insulinName,
                    onValueChange = { insulinName = it },
                    label = { Text("Insulin Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = typeName,
                    onValueChange = { typeName = it },
                    label = { Text("Type Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    userId?.let {
                        viewModel.addInsulinType(InsulinType(insulinName = insulinName, typeName = typeName, userId = it))
                        onDismiss()
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddIngredientDialog(onDismiss: () -> Unit, viewModel: SettingsViewModel) {
    var ingredientName by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var kcal by remember { mutableStateOf("") }
    val userId by viewModel.userId.collectAsState(initial = null)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Ingredient") },
        text = {
            Column {
                OutlinedTextField(
                    value = ingredientName,
                    onValueChange = { ingredientName = it },
                    label = { Text("Ingredient Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it },
                    label = { Text("Protein") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = carbs,
                    onValueChange = { carbs = it },
                    label = { Text("Carbs") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = fat,
                    onValueChange = { fat = it },
                    label = { Text("Fat") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = kcal,
                    onValueChange = { kcal = it },
                    label = { Text("Kcal") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                userId?.let {
                    viewModel.addIngredient(Ingredient(
                        foodName = ingredientName,
                        protein = protein.toFloat(),
                        carbs = carbs.toFloat(),
                        fat = fat.toFloat(),
                        kcal = kcal.toFloat(),
                        userId = it
                    ))
                }
                onDismiss()
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DeleteInsulinDialog(onDismiss: () -> Unit, viewModel: SettingsViewModel) {
    val insulinTypes by viewModel.insulinTypes.collectAsState(initial = emptyList())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Insulin") },
        text = {
            LazyColumn {
                items(insulinTypes) { insulinType ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(insulinType.insulinName)
                        Button(onClick = {
                            viewModel.deleteInsulinType(insulinType)
                            onDismiss()
                        }) {
                            Text("Delete")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun DeleteIngredientDialog(onDismiss: () -> Unit, viewModel: SettingsViewModel) {
    val ingredients by viewModel.ingredients.collectAsState(initial = emptyList())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Ingredient") },
        text = {
            LazyColumn {
                items(ingredients) { ingredient ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(ingredient.foodName)
                        Button(onClick = {
                            viewModel.deleteIngredient(ingredient)
                            onDismiss()
                        }) {
                            Text("Delete")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}