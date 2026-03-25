package com.georgeramsis.khedma.khedma

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.georgeramsis.khedma.khedma.presentation.navigation.Routes.Home
import com.georgeramsis.khedma.khedma.presentation.navigation.Routes.Login
import com.georgeramsis.khedma.khedma.presentation.screens.HomeScreen
import com.georgeramsis.khedma.khedma.presentation.screens.LoginScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(navController, startDestination = Login.route) {
            composable(Login.route) {
                LoginScreen(onLoginSuccess = {
                    navController.navigate(Home.route) {
                        popUpTo(Login.route) {
                            inclusive = true
                        }
                    }
                })
            }
            composable(Home.route) {
                HomeScreen()
            }
        }
    }
}