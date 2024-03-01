package com.rizrmdhn.storyapp.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Loading : Screen("loading")
    data object Favorite : Screen("favorite")
    data object About : Screen("about")
    data object DetailStory : Screen("home/{id}") {
        fun createRoute(id: String) = "home/$id"
    }
}