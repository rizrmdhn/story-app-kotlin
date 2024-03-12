package com.rizrmdhn.storyapp.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object AddStory : Screen("addStory")
    data object Map : Screen("map/{lat}/{lng}") {
        fun createRoute(lat: String, lng: String) = "map/$lat/$lng"
    }
    data object Loading : Screen("loading")
    data object Favorite : Screen("favorite")
    data object About : Screen("about")
    data object Settings : Screen("settings")
    data object DetailStory : Screen("home/{id}") {
        fun createRoute(id: String) = "home/$id"
    }
}