# Khedma — AI Agent Guide

## Project Overview
Khedma ("service" in Arabic) is a **Kotlin Multiplatform** app for church servant/student management, targeting **Android** and **Desktop (JVM)**. All business logic lives in `commonMain`; platform-specific code is minimal.

## Architecture

```
commonMain
  data/model/       – @Serializable data classes (snake_case DB → camelCase Kotlin via @SerialName)
  data/repository/  – Supabase query layer (AuthRepository, StudentRepository)
  di/               – Koin DI module (SupabaseModule.kt) + expect fun initKoin()
  presentation/
    navigation/     – Type-safe @Serializable route objects (Routes.kt)
    screens/        – @Composable screens (LoginScreen, HomeScreen, StudentsScreen)
    viewmodel/      – ViewModel + sealed state classes (Idle/Loading/Success/Error)
  supabase/         – expect fun getSupabaseUrl()/getSupabaseKey()
androidMain / jvmMain – actual implementations only
```

**Stack**: Compose Multiplatform · Material3 · Supabase (PostgREST + Auth + Realtime) · Koin DI · Jetpack Navigation Compose (type-safe) · kotlinx.serialization · Ktor HTTP client

## Critical: Supabase Credentials
Credentials are **never committed**. Add them to `local.properties`:
```
SUPABASE_URL=https://your-project.supabase.co
SUPABASE_KEY=your-anon-key
```
- **Android**: injected as `BuildConfig.SUPABASE_URL/KEY` by `composeApp/build.gradle.kts`
- **Desktop/JVM**: a custom Gradle task `generateDesktopSupabaseConfig` code-generates `SupabaseConfig.jvm.kt` into `build/generated/supabase/kotlin/` before `compileKotlinJvm` runs. Never hand-edit that file.

## Build & Run (Windows)
```powershell
.\gradlew.bat :composeApp:assembleDebug   # Android APK
.\gradlew.bat :composeApp:run             # Desktop app
.\gradlew.bat :composeApp:test            # Common tests
```
Hot-reload is available for Desktop via the `composeHotReload` plugin (use IDE run config).

## expect/actual Pattern
Every platform boundary uses `expect`/`actual`:
- `di/KoinInitializer.kt` declares `expect fun initKoin()` — actuals (android/jvm) call `startKoin { modules(supabaseModule) }`.
- `supabase/SupabaseConfig.kt` declares credential `expect fun`s — Android reads `BuildConfig`, JVM reads the generated file.
- When adding a new platform API, always declare `expect` in `commonMain` and provide `actual` in each target source set.

## Data Models (`data/model/Models.kt`)
All models are `@Serializable`. DB column names are snake_case; Kotlin properties are camelCase, mapped with `@SerialName`. Example:
```kotlin
@SerialName("first_name") val firstName: String
```
Key domain entities: `Profile` (servant), `Student`, `Stage`, `SchoolClass`, `StudentClass`, `Attendance`, `Visitation`, `Activity`, `AcademicYear`.

## Adding a New Screen / Feature
1. Add a `@Serializable` route object/data class to `presentation/navigation/Routes.kt`.
2. Register it in `NavHost` inside `App.kt`.
3. Create a ViewModel in `presentation/viewmodel/` following the `Idle/Loading/Success/Error` sealed state pattern.
4. Register `single { Repository(get()) }` and `viewModel { MyViewModel(get()) }` in `di/SupabaseModule.kt`.
5. Supabase queries go in `data/repository/` using `client.postgrest["table_name"].select { … }.decodeList<Model>()`.

## Dependency Versions
All versions are centralized in `gradle/libs.versions.toml`. Use `libs.` aliases in `build.gradle.kts` — never hardcode version strings.

