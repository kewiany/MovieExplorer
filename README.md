# Movie Explorer - Movie Discovery App

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![TMDB](https://img.shields.io/badge/TMDB-01B4E4?style=for-the-badge&logo=themoviedatabase&logoColor=white)
![codecov](https://codecov.io/gh/kewiany/MovieExplorer/branch/main/graph/badge.svg)
![Coverage](https://img.shields.io/badge/Coverage-31%25-orange?style=flat&logo=kotlin&logoColor=white)

## Table of Contents

- [Project Description](#project-description)
- [Features](#features)
  - [Popular Movies Screen](#-popular-movies-screen)
  - [Search Screen](#-search-screen)
  - [Movie Details Screen](#-movie-details-screen)
  - [Favorites Management](#-favorites-management)
- [Technical Architecture](#technical-architecture)
  - [Clean Architecture](#️-clean-architecture)
  - [Technologies](#️-technologies)
  - [API Integration](#-api-integration)
  - [Offline Support](#-offline-support)
- [Project Structure](#project-structure)
- [Requirements](#requirements)
- [Installation and Setup](#installation-and-setup)
- [Testing](#testing)
- [Code Coverage](#code-coverage)
- [API Configuration](#api-configuration)

## Project Description

**Movie Explorer** is a native Android application built with Kotlin using Jetpack Compose. The app was created as a recruitment task and enables users to browse popular movies, search for movies, and manage their favorite movies.

## Features

### 🎬 Popular Movies Screen
- List of popular movies with pagination
- Display movie title and poster
- Navigate to movie details
- Filter to show only favorite movies

### 🔍 Search Screen  
- Automatic search while typing
- Display results with movie title and poster
- Search history saved locally

### 📱 Movie Details Screen
- Detailed movie information
- Load additional data (overview, release date) via two concurrent network requests
- Movie languages list (displayed modally)
- Add/remove from favorites button

### ❤️ Favorites Management
- Mark movies as favorites
- Filter list to show only favorite movies
- Persistent storage of favorite movies between app launches

## Technical Architecture

### 🏗️ Clean Architecture
- **Presentation Layer**: Jetpack Compose UI + ViewModels
- **Domain Layer**: Use Cases + Repository interfaces
- **Data Layer**: Repository implementations + Room database + Retrofit API

### 🛠️ Technologies

| Category | Technology |
|----------|------------|
| **UI** | ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white) ![Material Design](https://img.shields.io/badge/Material%20Design-757575?style=flat&logo=materialdesign&logoColor=white) |
| **Architecture** | ![MVVM](https://img.shields.io/badge/MVVM-FF6B6B?style=flat&logo=android&logoColor=white) ![Clean Architecture](https://img.shields.io/badge/Clean%20Architecture-4ECDC4?style=flat&logo=android&logoColor=white) |
| **DI** | ![Hilt](https://img.shields.io/badge/Hilt-FF6B35?style=flat&logo=android&logoColor=white) |
| **Navigation** | ![Navigation Compose](https://img.shields.io/badge/Navigation%20Compose-4285F4?style=flat&logo=android&logoColor=white) |
| **Networking** | ![Retrofit](https://img.shields.io/badge/Retrofit-3DDC84?style=flat&logo=android&logoColor=white) ![OkHttp](https://img.shields.io/badge/OkHttp-3DDC84?style=flat&logo=android&logoColor=white) ![Moshi](https://img.shields.io/badge/Moshi-FF6B35?style=flat&logo=android&logoColor=white) |
| **Database** | ![Room](https://img.shields.io/badge/Room-4285F4?style=flat&logo=android&logoColor=white) ![SQLite](https://img.shields.io/badge/SQLite-003B57?style=flat&logo=sqlite&logoColor=white) |
| **Image Loading** | ![Coil](https://img.shields.io/badge/Coil-FF6B35?style=flat&logo=android&logoColor=white) |
| **Pagination** | ![Paging 3](https://img.shields.io/badge/Paging%203-4285F4?style=flat&logo=android&logoColor=white) |
| **Async** | ![Kotlin Coroutines](https://img.shields.io/badge/Kotlin%20Coroutines-7F52FF?style=flat&logo=kotlin&logoColor=white) ![Flow](https://img.shields.io/badge/Flow-7F52FF?style=flat&logo=kotlin&logoColor=white) |

### 📊 API Integration
- **The Movie Database (TMDB)** API v3
- Popular movies endpoint
- Search movies endpoint  
- Movie details endpoint
- Image loading from TMDB CDN

### 💾 Offline Support
- Persistent storage of movie data in Room database
- Offline access to favorite movies and search history
- Image caching via Coil

## Project Structure

```
app/src/main/java/xyz/kewiany/movieexplorer/
├── common/           # Shared components (navigation, utils, viewmodel)
├── data/            # Data layer (API, database, repository implementations)
├── di/              # Dependency Injection modules
├── domain/          # Domain layer (models, repositories, use cases)
├── presentation/    # Presentation layer (screens, viewmodels)
└── ui/              # MainActivity and UI components
```

## Requirements

| Requirement | Version |
|-------------|---------|
| **JDK** | ![JDK](https://img.shields.io/badge/JDK-17-orange?style=flat&logo=openjdk&logoColor=white) (recommended) |
| **Android Studio** | ![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84?style=flat&logo=androidstudio&logoColor=white) (latest stable) |
| **Min SDK** | ![Min SDK](https://img.shields.io/badge/Min%20SDK-30-green?style=flat&logo=android&logoColor=white) (Android 11) |
| **Target SDK** | ![Target SDK](https://img.shields.io/badge/Target%20SDK-36-blue?style=flat&logo=android&logoColor=white) (Android 14) |

## Installation and Setup

1. **Install JDK 17** (macOS example):
```bash
brew install --cask temurin@17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

2. **Open the project in Android Studio** and wait for Gradle sync to complete

3. **Build the app**:
```bash
./gradlew :app:assembleDebug
```

4. **Run on device/emulator** from Android Studio using the "app" run configuration

## Testing

### 🧪 Unit tests:
```bash
./gradlew :app:testDebugUnitTest
```

![Testing](https://img.shields.io/badge/Testing-JUnit-green?style=flat&logo=junit5&logoColor=white)
![Mocking](https://img.shields.io/badge/Mocking-MockK-blue?style=flat&logo=kotlin&logoColor=white)

## Code Coverage

[![codecov](https://codecov.io/gh/kewiany/MovieExplorer/branch/main/graph/badge.svg)](https://codecov.io/gh/kewiany/MovieExplorer)

### Coverage Report
- **Overall Coverage**: 31% (Line Coverage)
- **Instruction Coverage**: 31%
- **Branch Coverage**: 12%
- **Method Coverage**: 36%
- **Class Coverage**: 36%

### Generate Local Coverage Report
```bash
./gradlew jacocoTestReport
```

### View Coverage Report
Open `app/build/reports/jacoco/jacocoTestReport/index.html` in your browser.

### Coverage Details
Click the Codecov badge above to view detailed coverage reports, line-by-line coverage, and coverage trends.

## API Configuration

![TMDB API](https://img.shields.io/badge/TMDB%20API-01B4E4?style=flat&logo=themoviedatabase&logoColor=white)

To make the app work properly, you need an API key from The Movie Database:

1. **Register** at [themoviedb.org](https://www.themoviedb.org)
2. **Generate** a v3 API key
3. **Add** the key to `local.properties` file:
```properties
TMDB_API_KEY=your_api_key_here
```

---

*This project demonstrates my skills in modern Android mobile application development from August 2025. With 5+ years of experience in mobile application programming, you can learn more about my background on [LinkedIn](https://www.linkedin.com/in/kewiany/).*