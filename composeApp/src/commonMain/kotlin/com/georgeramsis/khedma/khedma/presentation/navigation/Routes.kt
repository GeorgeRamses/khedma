package com.georgeramsis.khedma.khedma.presentation.navigation

sealed class Routes(val route: String) {
    object Login_Screen : Routes("login")
    object Home_Screen:Routes("home")
    object Students_Screen : Routes("student")
}