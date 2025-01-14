package com.example.glucoapp.ui.views

@Composable
fun AddMealScreen(navController: NavController, mealsViewModel: MealsViewModel = hiltViewModel()) {
    var foodName by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var kcal by remember { mutableStateOf("") }
    // Add state for imagePath if you are handling images

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Meal") }) }
    ) {  innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {
            OutlinedTextField(
                value = foodName,
                onValueChange = { foodName = it },
                label = { Text("Food Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = protein,
                onValueChange = { protein = it },
                label = { Text("Protein (g)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = carbs,
                onValueChange = { carbs = it },
                label = { Text("Carbs (g)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = fat,
                onValueChange = { fat = it },
                label = { Text("Fat (g)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = kcal,
                onValueChange = { kcal = it },
                label = { Text("Kcal") },
                modifier = Modifier.fillMaxWidth()
            )
            // Add field for image if necessary

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val newMeal = Meal(
                        userId = mealsViewModel.mainViewModel.user.value?.userId ?: 0,
                        foodName = foodName,
                        protein = protein.toDoubleOrNull() ?: 0.0,
                        carbs = carbs.toDoubleOrNull() ?: 0.0,
                        fat = fat.toDoubleOrNull() ?: 0.0,
                        kcal = kcal.toDoubleOrNull() ?: 0.0,
                        imagePath = null, // Set image path if applicable
                        isPredefined = 0 // Or 1 if it's a predefined meal
                    )
                    viewModelScope.launch {
                        val newMealId = mealsViewModel.insertMeal(newMeal)
                        // Handle the new meal ID if needed
                        navController.navigate(Screen.Meals.route)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Meal")
            }
        }
    }
}