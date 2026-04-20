package com.georgeramsis.khedma.khedma.di

import com.georgeramsis.khedma.khedma.data.repository.AuthRepository
import com.georgeramsis.khedma.khedma.data.repository.SettingsRepository
import com.georgeramsis.khedma.khedma.data.repository.StudentRepository
import com.georgeramsis.khedma.khedma.presentation.viewmodel.AuthViewModel
import com.georgeramsis.khedma.khedma.presentation.viewmodel.HomeViewModel
import com.georgeramsis.khedma.khedma.presentation.viewmodel.SettingsViewModel
import com.georgeramsis.khedma.khedma.presentation.viewmodel.StudentViewModel
import com.georgeramsis.khedma.khedma.supabase.getSupabaseKey
import com.georgeramsis.khedma.khedma.supabase.getSupabaseUrl
import com.russhwolf.settings.Settings
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val supabaseModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = getSupabaseUrl(),
            supabaseKey = getSupabaseKey()
        ) {
            install(Postgrest)
            install(Auth)
        }
    }
    single { AuthRepository(get()) }
    viewModel { AuthViewModel(get()) }
    single { StudentRepository(get()) }
    viewModel { StudentViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    single { Settings() }
    single { SettingsRepository(get()) }
    viewModel { SettingsViewModel(get()) }
}