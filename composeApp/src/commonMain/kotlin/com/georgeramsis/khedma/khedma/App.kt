package com.georgeramsis.khedma.khedma

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.georgeramsis.khedma.khedma.presentation.navigation.HomeRoute
import com.georgeramsis.khedma.khedma.presentation.navigation.LoginRoute
import com.georgeramsis.khedma.khedma.presentation.navigation.StudentsRoute
import com.georgeramsis.khedma.khedma.presentation.screens.HomeScreen
import com.georgeramsis.khedma.khedma.presentation.screens.LoginScreen
import com.georgeramsis.khedma.khedma.presentation.screens.StudentScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = LoginRoute) {
            composable<LoginRoute> {
                LoginScreen(
                    onLoginSuccess =
                        {
                            navController.navigate(HomeRoute) {
                                popUpTo<LoginRoute> { inclusive = true }
                            }
                        })
            }
            composable<HomeRoute> {
                HomeScreen(navToStudents = { classId ->
                    navController.navigate(StudentsRoute(classId))
                })
            }
            composable<StudentsRoute> { backStackEntry ->
                val student = backStackEntry.toRoute<StudentsRoute>()
                StudentScreen(classId = student.classId)
            }
        }
    }
}