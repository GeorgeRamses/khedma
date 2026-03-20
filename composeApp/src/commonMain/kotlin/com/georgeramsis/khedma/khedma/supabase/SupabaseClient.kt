package com.georgeramsis.khedma.khedma.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest



val supabase = createSupabaseClient(
    supabaseUrl = "",
    supabaseKey = "your_public_anon_key"
) {

    install(Postgrest)
}
