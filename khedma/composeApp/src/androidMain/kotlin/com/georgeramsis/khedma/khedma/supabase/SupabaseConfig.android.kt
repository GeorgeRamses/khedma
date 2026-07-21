package com.georgeramsis.khedma.khedma.supabase


import com.georgeramsis.khedma.khedma.BuildConfig

actual fun getSupabaseKey(): String = BuildConfig.SUPABASE_KEY
actual fun getSupabaseUrl(): String = BuildConfig.SUPABASE_URL
