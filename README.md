# Outstanding

This repository contains the ongoing development of a KMP application for a geolocation social media platform. The project was originally prototyped by [@tavro](https://tavro.se) and [@parslie](https://parslie.github.io) as part of a university course and later reimagined as a hobby project. The goal of this repository is to evolve that prototype into a finalized open-source implementation.

## Technology Stack

  | Layer                | Technology             |
  |----------------------|------------------------|
  | Language             | Kotlin                 |
  | UI Framework         | Compose Multiplatform  |
  | Architecture Pattern | Component-based        |
  | Dependency Injection | Koin                   |
  | Navigation           | Decompose              |
  | Networking           | Ktor                   |
  | Database             | AndroidX Room          |
  | Serialization        | kotlinx.serialization  |
  | Build System         | Gradle with Kotlin DSL |

## Jade Design System

UI components are built on top of a shared design system called **Jade**, living in `designsystem/`. It provides:

- **Theme**: `JadeTheme` exposes `colorScheme`, `typography`, and `shapes` via `CompositionLocal`
- **Primitives**: `JadeSurface`, `JadeText`
- **Layout**: `JadeScaffold`
- **Inputs**: `JadeTextField` (single-line and multiline, with auto-size support), `JadeFilledButton`

## Component Structure

Each feature exposes a `Component` that owns its state and business logic, and renders itself via a `@Composable Render()` function.

```kotlin
class LoginComponent(
    componentContext: ComponentContext,
    private val navigator: Navigator,
    private val authService: AuthService,
    private val accountRepository: AccountRepository,
    configuration: Configuration,
) : ComponentContext by componentContext, Component {

    private val _state = MutableStateFlow<LoginScreenState>(LoginScreenState.Initial)
    val state = _state.asStateFlow()

    fun onLogin(username: String, password: String) { ... }
    fun onRegister(username: String, password: String, email: String) { ... }

    @Composable
    override fun Render(modifier: Modifier) = LoginScreen(this, modifier)
}
```

## Navigation System

### Config

```kotlin
@Serializable
sealed interface Config {
    @Serializable
    data class Main(val tab: Tab = Tab.Map) : Config

    @Serializable
    sealed interface Onboarding : Config {
        @Serializable
        data object Login : Onboarding
    }

    enum class Tab { Map, Feed, Create, Profile }
}
```

### Navigator

```kotlin
class Navigator {
    val stack = StackNavigation<Config>()

    fun navigateTo(config: Config) = stack.navigate { listOf(config) }
    fun popToMain(tab: Config.Tab = Config.Tab.Map) = stack.navigate { listOf(Config.Main(tab)) }
    fun pop() = stack.navigate { it.dropLast(1).ifEmpty { listOf(Config.Main()) } }
}
```

`RootComponent` observes `AccountProvider.accountFlow` on startup, if no account exists it pushes `Config.Onboarding.Login`, otherwise it goes straight to `Config.Main`.

## Dependency Injection

  Modules will be organized per feature:

```kotlin
val uiModule = module {
    includes(
        mapModule,
        feedModule,
        profileModule,
        navigationModule,
    )

    factory<RootComponent> { params ->
        RootComponent(
            componentContext = params.get(),
            navigator = get(),
        )
    }

    factoryOf(::MainComponent)
    factoryOf(::MapComponent)
    factoryOf(::FeedComponent)
    // ...
}
```

## Data Layer

### Auth

`AuthService` posts credentials to the backend and returns a `UserSession` containing `user_id`, `username`, and `token`. On success, `LoginComponent` persists an `Account` to the local database and `RootComponent` navigates to the main screen.

### Repository Pattern

```kotlin
class AccountRepository(private val dao: AccountDao) {
    suspend fun save(account: Account): Long
    suspend fun update(account: Account)
    suspend fun updateAuthToken(id: Long, authToken: String?)
    fun getAccountFlow(): Flow<Account?>
    suspend fun clear()
}
```

### Room Database

```kotlin
@Database(entities = [AccountEntity::class], version = 1)
abstract class OutstandingDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "auth_token") val authToken: String? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long = 0L,
)
```

---

## Project Structure

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that's common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you're sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

## Backend

The app connects to a backend running locally on port `8000`. Start the backend server before launching the app.

### Android device (port forwarding)

Android devices cannot reach host `localhost` directly. Use `adb reverse` to forward the device's port to your machine:

```shell
adb reverse tcp:8000 tcp:8000
```

Run this after connecting your device and before launching the app. Re-run it if you reconnect the device.

## Build and Run

### Android

```shell
./gradlew :composeApp:assembleDebug
```

### iOS

Open the [/iosApp](./iosApp) directory in Xcode and run it from there, or use the IDE run configuration.

### Web (Wasm — modern browsers)

```shell
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

### Web (JS — broader browser support)

```shell
./gradlew :composeApp:jsBrowserDevelopmentRun
```

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/).
