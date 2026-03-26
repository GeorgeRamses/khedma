package com.georgeramsis.khedma.khedma.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable object LoginRoute
@Serializable object HomeRoute
@Serializable data class StudentsRoute(val classId: String)  // classId is a typed field

