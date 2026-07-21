package com.georgeramsis.khedma.khedma

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.georgeramsis.khedma.khedma.data.model.AppLanguage

actual fun updateAppLocale(lang: AppLanguage) {
    val local = LocaleListCompat.forLanguageTags(lang.code)
    AppCompatDelegate.setApplicationLocales(local)
}