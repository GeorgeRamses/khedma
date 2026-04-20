package com.georgeramsis.khedma.khedma.data.repository

import com.georgeramsis.khedma.khedma.data.model.AppLanguage
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepository(private val settings: Settings) {
    private val _language = MutableStateFlow(
        AppLanguage.fromCode(settings.getString("app_language_code", "en"))
    )
    val language = _language.asStateFlow()

    private val _isDarkMode = MutableStateFlow(settings.getBoolean("dark_mode", false))
    val isDarkMode = _isDarkMode.asStateFlow()

    fun setLanguage(lang: AppLanguage) {
        settings.putString("app_language_code", lang.code)
        _language.value = lang
    }

    fun setDarkMode(isDarkMode: Boolean) {
        settings.putBoolean("dark_mode", isDarkMode)
        _isDarkMode.value = isDarkMode
    }
}