package com.georgeramsis.khedma.khedma.supabase

actual fun getSupabaseKey(): String = System.getProperty("SUPABASE_KEY")

actual fun getSupabaseUrl(): String = System.getProperty("SUPABASE_URL")