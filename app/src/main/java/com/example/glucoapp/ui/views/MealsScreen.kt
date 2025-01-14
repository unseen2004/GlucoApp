package com.example.glucoapp.ui.views

@Composable
fun MealsScreen(navController: NavController, mealsViewModel: MealsViewModel = hiltViewModel()) {
    val meals by mealsViewModel.meals.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Meals") }) },
        floatingActionButton = {
            IconButton(onClick = { navController.navigate(Screen.AddMeal.route) }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Meal")
            }
        }
    ) { innerPadding ->
        MealsContent(meals = meals, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun MealsContent(meals: List<Meal>, modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        items(meals) { meal ->
            MealItem(meal = meal)
        }
    }
}

@Composable
fun MealItem(meal: Meal) {
    Card(modifier = Modifier.padding(8.dp), elevation = 2.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Food Name: ${meal.foodName}")
            Text(text = "Protein: ${meal.protein}g")
            Text(text = "Carbs: ${meal.carbs}g")
            Text(text = "Fat: ${meal.fat}g")
            // Add other details like Kcal, Image, etc. if needed
        }
    }
}