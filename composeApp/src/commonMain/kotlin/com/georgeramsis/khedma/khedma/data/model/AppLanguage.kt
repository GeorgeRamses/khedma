package com.georgeramsis.khedma.khedma.data.model

sealed class AppLanguage(val code: String, val displayName: String, val isRtl: Boolean) {
    object English : AppLanguage(code = "en", displayName = "English", isRtl = false)
    object Arabic : AppLanguage(code = "ar", displayName = "العربية", isRtl = true)
    companion object {
        fun fromCode(code: String): AppLanguage {
            return when (code) {
                "ar" -> Arabic
                else -> English
            }
        }
    }
}