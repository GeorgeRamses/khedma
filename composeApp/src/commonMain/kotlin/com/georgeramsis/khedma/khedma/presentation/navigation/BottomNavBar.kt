package com.georgeramsis.khedma.khedma.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(val route: Any, val label: String, val icon: ImageVector)

val items = listOf(
    BottomNavItem(route = HomeRoute, label = "Home", icon = Icons.Default.Home),
    BottomNavItem(route = StudentsRoute, label = "Students", icon = Icons.Default.People),
    BottomNavItem(route = AttendanceRoute, label = "Attendance", icon = Icons.Default.DateRange),
    BottomNavItem(route = VisitationsRoute, label = "Visitations", icon = Icons.Default.LocationOn),
    BottomNavItem(route = SettingsRoute, label = "Settings", icon = Icons.Default.Settings)
)

@Composable
fun BottomNavBar(navController: NavHostController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    NavigationBar() {
        items.forEach { item ->
            val selected =
                currentDestination?.hierarchy?.any { it.hasRoute(item.route::class.qualifiedName ?: "", null) } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        popUpTo<HomeRoute> {
                            inclusive = false
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}