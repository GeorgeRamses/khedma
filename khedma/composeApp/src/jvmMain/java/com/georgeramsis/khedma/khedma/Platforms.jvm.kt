package com.georgeramsis.khedma.khedma

import com.georgeramsis.khedma.khedma.data.model.AppLanguage
import java.util.Locale

actual fun updateAppLocale(lang: AppLanguage) {
    Locale.setDefault(Locale.forLanguageTag(lang.code))
}