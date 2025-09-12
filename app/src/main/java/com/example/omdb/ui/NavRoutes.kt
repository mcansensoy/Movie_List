package com.example.omdb.ui

sealed class NavRoutes(val route: String) {
    object Menu : NavRoutes("menu")
    object Search : NavRoutes("search")
    object Completed : NavRoutes("completed")
    object Watchlist : NavRoutes("watchlist")
}