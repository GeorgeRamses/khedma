package com.georgeramsis.khedma.khedma

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.georgeramsis.khedma.khedma.presentation.navigation.AttendanceRoute
import com.georgeramsis.khedma.khedma.presentation.navigation.BottomNavBar
import com.georgeramsis.khedma.khedma.presentation.navigation.HomeRoute
import com.georgeramsis.khedma.khedma.presentation.navigation.LoginRoute
import com.georgeramsis.khedma.khedma.presentation.navigation.SettingsRoute
import com.georgeramsis.khedma.khedma.presentation.navigation.StudentsRoute
import com.georgeramsis.khedma.khedma.presentation.navigation.VisitationsRoute
import com.georgeramsis.khedma.khedma.presentation.screens.AttendanceScreen
import com.georgeramsis.khedma.khedma.presentation.screens.HomeScreen
import com.georgeramsis.khedma.khedma.presentation.screens.LoginScreen
import com.georgeramsis.khedma.khedma.presentation.screens.SettingsScreen
import com.georgeramsis.khedma.khedma.presentation.screens.StudentScreen
import com.georgeramsis.khedma.khedma.presentation.screens.VisitationsScreen
import com.georgeramsis.khedma.khedma.presentation.viewmodel.AuthState
import com.georgeramsis.khedma.khedma.presentation.viewmodel.AuthViewModel
import com.georgeramsis.khedma.khedma.presentation.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = koinViewModel()
    val settingViewModel: SettingsViewModel = koinViewModel()

    val state by authViewModel.state.collectAsState()
    val currentUser by authViewModel.profile.collectAsState()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val lang by settingViewModel.language.collectAsState()
    val isDarkMode by settingViewModel.isDarkMode.collectAsState()

    val layoutDirection = if (lang.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    val colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme()

    val currentRoute = currentBackStack?.destination?.route ?: LoginRoute::class.qualifiedName
    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        key(lang.code) {
            MaterialTheme(colorScheme = colorScheme) {
                if (state == AuthState.Checking) {
                    // Show loading screen or splash screen
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Text("Checking authentication...")
                        CircularProgressIndicator()
                    }
                } else {
                    Scaffold(bottomBar = {
                        if (currentRoute != LoginRoute::class.qualifiedName) BottomNavBar(
                            navController
                        )
                    }) {

                        NavHost(
                            modifier = Modifier.padding(it),
                            navController = navController,
                            startDestination = if (currentUser != null) HomeRoute else LoginRoute
                        ) {
                            composable<LoginRoute> {
                                LoginScreen(
                                    onLoginSuccess =
                                        {
                                            navController.navigate(HomeRoute) {
                                                popUpTo<LoginRoute> { inclusive = true }
                                            }
                                        }, authViewModel
                                )
                            }
                            composable<HomeRoute> {
                                HomeScreen(authViewModel = authViewModel)
                            }
                            composable<StudentsRoute> {
                                StudentScreen(authViewModel = authViewModel)
                            }
                            composable<AttendanceRoute> {
                                AttendanceScreen()
                            }
                            composable<VisitationsRoute> {
                                VisitationsScreen()
                            }
                            composable<SettingsRoute> {
                                SettingsScreen(authViewModel, settingViewModel, onSignOut = {
                                    navController.navigate(LoginRoute) {
                                        popUpTo<HomeRoute> { inclusive = true }
                                    }
                                })
                            }
                        }

                    }
                }
            }
        }
    }
}