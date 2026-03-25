package com.georgeramsis.khedma.khedma

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.georgeramsis.khedma.khedma.presentation.navigation.Routes
import com.georgeramsis.khedma.khedma.presentation.navigation.Routes.Home_Screen
import com.georgeramsis.khedma.khedma.presentation.navigation.Routes.Login_Screen
import com.georgeramsis.khedma.khedma.presentation.navigation.Routes.Students_Screen
import com.georgeramsis.khedma.khedma.presentation.screens.HomeScreen
import com.georgeramsis.khedma.khedma.presentation.screens.LoginScreen
import com.georgeramsis.khedma.khedma.presentation.screens.StudentScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(navController, startDestination = Login_Screen.route) {
            composable(Login_Screen.route) {
                LoginScreen(onLoginSuccess = {
                    navController.navigate(Home_Screen.route) {
                        popUpTo(Login_Screen.route) {
                            inclusive = true
                        }
                    }
                })
            }
            composable(Home_Screen.route) {
                HomeScreen(navToStudents = {
                    navController.navigate(Students_Screen.route)
                })
            }
            composable(Students_Screen.route) {
                StudentScreen("")
            }
        }
    }
}