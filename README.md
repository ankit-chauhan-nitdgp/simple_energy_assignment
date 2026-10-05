# Vehicle Dashboard — Android

A simple Android vehicle dashboard application built for the SimpleEnergy
Android technical assessment.

The app allows users to view a list of vehicles and open an individual
vehicle to see detailed information such as battery level, range, speed,
odometer, connectivity and last updated time.

The application provides a vehicle dashboard with:

- Vehicle list
- Vehicle details
- Battery percentage
- Estimated range
- Current speed
- Odometer
- Online/offline status
- Connectivity status
- Last updated time
- Refresh functionality
- API failure and empty-state handling

## Tech Stack

- **Kotlin**
- **Jetpack Compose**
- **Navigation Compose**
- **MVVM**
- **Clean Architecture**
- **Coroutines**
- **Flow / StateFlow**
- **Retrofit**
- **Gson**
- **Hilt**
- **JUnit**
- **MockK**
- **Kotlin Coroutines Test**
- **Beeceptor** for mock REST APIs

---

## Build / Run Instructions

### Requirements

- Android Studio with a recent stable Android SDK
- JDK 11
- Android device or emulator
- Internet connection for the mock API

### Steps

1. Clone the repository:

```bash
git clone <repository-url>
```

2. Open the project in Android Studio.

3. Allow Gradle to sync and download the required dependencies.

4. Connect an Android device or start an emulator.

5. Run the `app` configuration from Android Studio.

Alternatively:

```bash
./gradlew assembleDebug
```

Install the generated APK on a connected device/emulator.

### API

The application uses the following mock API:

```text
Base URL:
https://android-mockapi.free.beeceptor.com/
```

Endpoints:

```text
GET /api/vehicles
GET /api/vehicles/{id}
```

The API is configured in:

```text
com.simple.energy.di.NetworkModule
```

---

# Architecture / Approach

The application follows a lightweight **Clean Architecture + MVVM** structure.

```text
UI / Presentation
        ↓
ViewModel
        ↓
Use Case
        ↓
Repository Interface
        ↓
Repository Implementation
        ↓
Retrofit API
        ↓
Mock REST API
```

### Package Structure

```text
com.simple.energy
│
├── core
│   ├── ApiResult.kt
│   └── network
│       └── SafeApiCall.kt
│
├── data
│   ├── remote
│   │   ├── VehicleApi.kt
│   │   └── VehicleDto.kt
│   │
│   ├── mapper
│   │   └── VehicleMapper.kt
│   │
│   └── repository
│       └── VehicleRepositoryImpl.kt
│
├── domain
│   ├── model
│   │   ├── VehicleSummary.kt
│   │   └── VehicleDetails.kt
│   │
│   ├── repository
│   │   └── VehicleRepository.kt
│   │
│   └── usecase
│       ├── GetVehicleUseCase.kt
│       └── GetVehicleDetailsUseCase.kt
│
├── di
│   ├── NetworkModule.kt
│   └── RepositoryModule.kt
│
└── presentation
    ├── navigation
    │   └── AppNavigation.kt
    │
    ├── vehicle_list
    │   ├── VehicleListScreen.kt
    │   ├── VehicleListUiState.kt
    │   └── VehicleListViewModel.kt
    │
    └── vehicle_details
        ├── VehicleDetailsScreen.kt
        ├── VehicleDetailsUiState.kt
        └── VehicleDetailsViewModel.kt
```

## Presentation Layer

Jetpack Compose is used for the UI.
Details page has dedicated refresh button.

Each screen has a dedicated `ViewModel` and immutable UI state exposed through `StateFlow`.

For example:

```text
VehicleListScreen
        ↓
VehicleListViewModel
        ↓
VehicleListUiState
```

The UI observes state using:

```kotlin
collectAsStateWithLifecycle()
```

This keeps UI rendering separate from business and data logic.

## Navigation

Navigation Compose is used instead of Fragments.

The vehicle ID is passed when navigating to the details screen:

```text
vehicle_list
      ↓
vehicle_details/{vehicleId}
```

Only the ID is passed through navigation. The details screen fetches its own data using that ID.

The `vehicleId` is obtained through `SavedStateHandle` in `VehicleDetailsViewModel`.

## Domain Layer

The domain layer contains models, repository contracts and use cases.

The repository interface is independent of Retrofit:

```kotlin
interface VehicleRepository {
    suspend fun getVehicles(): ApiResult<List<VehicleSummary>>

    suspend fun getVehicleDetails(
        vehicleId: String
    ): ApiResult<VehicleDetails>
}
```

This keeps the domain layer independent from implementation details.

## Data Layer

Retrofit is used for REST API communication.

The API response is represented by `VehicleDTO`.

DTOs are mapped into domain models:

```text
VehicleDTO
    ↓
VehicleMapper
    ↓
VehicleSummary / VehicleDetails
```

This prevents API models from leaking into the presentation/domain layers.

## State Management

`StateFlow` is used for screen state.

The details screen distinguishes between:

```text
Initial loading
    isLoading = true

Refresh
    isRefreshing = true
```

During refresh, the existing vehicle information remains visible instead of replacing the entire screen with a loading indicator.

## Error Handling

Network/API calls are wrapped using `safeApiCall`, this let us handle edge cases and failures.

It handles:

- HTTP errors
- Network/IO errors
- Unexpected exceptions

`CancellationException` is rethrown so coroutine cancellation behaves correctly.

---

# Testing

Unit tests include the main data and presentation flows.
Used backticked naming convention for easy to understand.

### Repository Tests

Verify:

- Successful API response mapping
- Details mapping
- Network failure handling

### ViewModel Tests

Verify:

- Successful vehicle loading
- Error state handling
- Retry behavior
- Vehicle details loading
- Details refresh behavior

Testing tools:

```text
JUnit
MockK
kotlinx-coroutines-test
```

---

# Key Assumptions / Limitations

### Assumptions

1. The REST Api we are using returns static data.

2. The vehicle ID is unique key for detail fetching.

3. The mock API is available during application execution.


### Limitations

1. **Mock API**

   Using "https://app.beeceptor.com/" for mocking API (50 call/day is allowed).

2. **No local persistence**

   No offline persistent caching of data only in memory state caching.

3. **No authentication**

   Authentication and authorization are outside the scope of the assignment.

4. **No real-time vehicle updates**

   Vehicle information is completely static no dynamic values.

5. **Last updated timestamp**

   The timestamp is displayed directly without formatting.

6. **Production observability**

   Crash reporting, analytics, performance monitoring and production logging are not included because they are outside the scope of the assessment.

---

## Scope

Avoided unnecessary complexity such as multi-module Gradle architecture, Room, WorkManager, Paging, offline synchronization, or real-time communication because the assignment specifies a small 2–3 hour implementation.

The focus is on:

**Correctness → Architecture → Code Quality → State Management → Testing**
