package com.georgeramsis.khedma.khedma.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.georgeramsis.khedma.khedma.presentation.viewmodel.AuthViewModel
import com.georgeramsis.khedma.khedma.presentation.viewmodel.HomeState
import com.georgeramsis.khedma.khedma.presentation.viewmodel.HomeViewModel
import khedma.composeapp.generated.resources.Res
import khedma.composeapp.generated.resources.birthdays
import khedma.composeapp.generated.resources.need_visits
import khedma.composeapp.generated.resources.upcoming_activities
import khedma.composeapp.generated.resources.upcoming_birthdays
import khedma.composeapp.generated.resources.welcome
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel = koinViewModel(),
    homeViewModel: HomeViewModel = koinViewModel(),
) {
    val permission by authViewModel.permission.collectAsState()
    val profile by authViewModel.profile.collectAsState()
    val birthdate by homeViewModel.upcomingBirthdays.collectAsState()
    val needVisits by homeViewModel.studentNeedVisit.collectAsState()
    val activity by homeViewModel.activities.collectAsState()
    val state by homeViewModel.state.collectAsState()


    LaunchedEffect(permission) {
        permission?.let {
            homeViewModel.loadHomeData(it)
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                stringResource(Res.string.welcome, profile?.fullName ?: "..."),
                style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            when (state) {
                is HomeState.Loading -> CircularProgressIndicator()
                is HomeState.Success -> Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SummaryCard(
                        title = stringResource(Res.string.upcoming_birthdays),
                        count = birthdate.size,
                        icon = Icons.Default.Cake
                    )
                    SummaryCard(
                        title = stringResource(Res.string.need_visits),
                        count = needVisits.size,
                        icon = Icons.Default.PersonSearch
                    )
                    SummaryCard(
                        title = stringResource(Res.string.upcoming_activities),
                        count = activity.size,
                        icon = Icons.Default.Schedule
                    )

                }

                is HomeState.Error -> {
                    Text((state as HomeState.Error).errorMessage)
                }

                else -> {}
            }
        }

        if (birthdate.isNotEmpty()) {
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    SeparationTitle(stringResource(Res.string.upcoming_birthdays), Icons.Default.Cake)
                }

            }
            items(birthdate) { student ->
                DetailCard(
                    title = "${student.firstName} ${student.lastName}",
                    date = student.dateOfBirth
                )
            }
        }
        if (needVisits.isNotEmpty()) {
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    SeparationTitle(stringResource(Res.string.need_visits), Icons.Default.PersonSearch)
                }
            }
            items(needVisits) { student ->
                DetailCard(
                    title = "${student.studentName}: ",
                    date = "last Attendance: (${student.date})"
                )
            }
        }
        if (activity.isNotEmpty()) {
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    SeparationTitle(stringResource(Res.string.upcoming_activities), Icons.Default.Schedule)
                }
            }
            items(activity) { act ->
                DetailCard(
                    title = "${act.title}: ",
                    date = "(${act.date})"
                )
            }
        }
    }
}

@Composable
fun RowScope.SummaryCard(title: String, count: Int, icon: ImageVector) {
    ElevatedCard(modifier = Modifier.weight(1f).padding(4.dp)) {
        Column(modifier = Modifier.fillMaxSize().padding(7.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(count.toString(), style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
fun SeparationTitle(title: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(7.dp)) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(title, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun DetailCard(title: String, date: String) {
    ElevatedCard(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(date, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

