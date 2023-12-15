package com.rhonda.ui.navigation

interface AppNavigationDestination {
    val route: String
    val destination: String

    fun routeTo(): String = route
}