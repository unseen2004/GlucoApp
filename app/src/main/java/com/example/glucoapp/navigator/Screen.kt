package com.example.glucoapp.navigator

sealed class Screen(val route: String) {
    object Notes : Screen("notes")
    object Meals : Screen("meals")
    object Settings : Screen("settings")
    object AddNote : Screen("add_note")
    object AddMeal : Screen("add_meal")
    object Login : Screen("login")
}