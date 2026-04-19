package com.georgeramsis.khedma.khedma.data.repository

import com.georgeramsis.khedma.khedma.data.model.Profile
import com.georgeramsis.khedma.khedma.data.model.ServantPermission
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest

class AuthRepository(private val client: SupabaseClient) {
    suspend fun signIn(email: String, password: String) {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signOut() {
        client.auth.signOut()
    }

    suspend fun awaitReady() {
        client.auth.awaitInitialization()
    }

    fun currentUser() = client.auth.currentUserOrNull()

    suspend fun getProfile(): Profile? {
        val userId = currentUser()?.id ?: return null
        return client.postgrest["profiles"].select {
            filter {
                eq("id", userId)
            }
        }.decodeSingleOrNull<Profile>()
    }

    suspend fun getServantClass(): ServantPermission? {
        return client.postgrest["servant_permissions"].select() {
            filter {
                eq("servant_id", client.auth.currentUserOrNull()?.id ?: "")
            }
        }.decodeSingleOrNull<ServantPermission>()
    }
}

