package com.georgeramsis.khedma.khedma.di

import org.koin.core.context.startKoin

actual fun initKoin() {
    startKoin { modules(supabaseModule) }
}