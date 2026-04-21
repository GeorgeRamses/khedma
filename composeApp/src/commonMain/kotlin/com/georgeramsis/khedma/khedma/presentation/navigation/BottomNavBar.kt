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
import khedma.composeapp.generated.resources.Res
import khedma.composeapp.generated.resources.attendance_log
import khedma.composeapp.generated.resources.home
import khedma.composeapp.generated.resources.settings
import khedma.composeapp.generated.resources.students_log
import khedma.composeapp.generated.resources.visitation_log
import org.jetbrains.compose.resources.stringResource

data class BottomNavItem(val route: Any, val label: String, val icon: ImageVector)



@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem(route = HomeRoute, label = stringResource(Res.string.home), icon = Icons.Default.Home),
        BottomNavItem(route = StudentsRoute, label = stringResource(Res.string.students_log), icon = Icons.Default.People),
        BottomNavItem(route = AttendanceRoute, label = stringResource(Res.string.attendance_log), icon = Icons.Default.DateRange),
        BottomNavItem(route = VisitationsRoute, label = stringResource(Res.string.visitation_log), icon = Icons.Default.LocationOn),
        BottomNavItem(route = SettingsRoute, label = stringResource(Res.string.settings), icon = Icons.Default.Settings)
    )

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