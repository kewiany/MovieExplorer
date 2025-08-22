## README explaining how to run the project

### Requirements

- JDK 17 (recommended)
- Android Studio (latest stable)

### Setup

1) Install JDK 17 and set JAVA_HOME (macOS example):
```bash
brew install --cask temurin@17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

2) Open the project in Android Studio and let Gradle sync finish.

3) Build the app:
```bash
./gradlew :app:assembleDebug
```

4) Run on a device/emulator from Android Studio using the "app" run configuration.

### Running tests

- Unit tests:
```bash
./gradlew :app:testDebugUnitTest
```