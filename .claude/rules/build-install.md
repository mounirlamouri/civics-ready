# Build & Install — CivicsReady Android App

## Prerequisites
- **JDK 11+** on PATH (Android Gradle plugin requires it)
- **Android SDK** installed (via Android Studio or `sdkmanager`)
- `ANDROID_HOME` environment variable set, e.g. `C:\Users\<you>\AppData\Local\Android\Sdk`
- For device install: `adb` on PATH (ships with Android SDK platform-tools)

## Common Gradle commands

All commands are run from the `civicsready/` directory (the Gradle root, where `gradlew` lives).

### Build debug APK
```bash
./gradlew assembleDebug
```
Output: `app/build/outputs/apk/debug/app-debug.apk`

### Build release APK (minified, ProGuard enabled)
```bash
./gradlew assembleRelease
```
Output: `app/build/outputs/apk/release/app-release-unsigned.apk`

> Release builds are unsigned by default. To sign, configure a `signingConfig` in
> `app/build.gradle.kts` or use `apksigner` manually.

### Build & install debug APK directly onto a connected device/emulator
```bash
./gradlew installDebug
```
Requires exactly one device visible to `adb` (`adb devices`).

### Run all unit tests
```bash
./gradlew test
```

### Run connected (instrumented) tests
```bash
./gradlew connectedAndroidTest
```

### Clean build outputs
```bash
./gradlew clean
```

## Manual install via adb

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

`-r` replaces an existing installation. Use `-d` to allow version downgrade.

## Troubleshooting

| Problem | Fix |
|---|---|
| `SDK location not found` | Create `civicsready/local.properties` with `sdk.dir=C\:\\Users\\<you>\\AppData\\Local\\Android\\Sdk` |
| `JAVA_HOME` errors | Set `JAVA_HOME` to Android Studio's bundled JDK: `C:\Program Files\Android\Android Studio\jbr` |
| `adb: command not found` | `adb` is not on PATH; use the full path: `C:\Users\mounir\AppData\Local\Android\Sdk\platform-tools\adb.exe` |
| `adb: device not found` | Enable USB debugging on the device; run `adb devices` to confirm it appears |
| `minSdk` mismatch | App requires Android 8.0 (API 26) or higher |
| KSP / Hilt errors after a sync | Run `./gradlew clean` then rebuild — KSP incremental caches can get stale |
