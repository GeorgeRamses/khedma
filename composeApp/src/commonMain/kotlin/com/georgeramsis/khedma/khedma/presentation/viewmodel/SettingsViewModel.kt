package com.georgeramsis.khedma.khedma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.georgeramsis.khedma.khedma.data.model.AppLanguage
import com.georgeramsis.khedma.khedma.data.repository.SettingsRepository

class SettingsViewModel(private val repo: SettingsRepository) : ViewModel() {
    val language = repo.language
    val isDarkMode = repo.isDarkMode

    fun setLanguage(lang: AppLanguage) {
        repo.setLanguage(lang)
    }

    fun setDarkMode(isDarkMode: Boolean) {
        repo.setDarkMode(isDarkMode)
    }
}