# Civics Ready

An Android app to help U.S. naturalization applicants study for the **2025 USCIS Civics Test**.

It presents all 128 official questions as self-grading flashcards — the same oral Q&A format used in the real interview. Enter your zip code to see your Governor, Senators, and Representative automatically filled into the relevant questions.

## Features

- **Practice Mode** — Study all 128 questions as flashcards (swipe or tap to self-grade)
- **Test Mode** — Simulate the real exam: 20 random questions, need 12 correct to pass
- **65/20 Special Consideration** — Toggle for applicants 65+ with 20+ years of permanent residency (10 questions from a pool of 20, need 6 to pass)
- **Location-aware answers** — Enter your zip code to auto-fill your state's Governor, Senators, Representative, and capital into dynamic questions
- **Progress tracking** — Per-question accuracy stats stored locally
- **Fully offline** — No internet connection or API keys required

## Screenshots

<!-- TODO: Add screenshots -->

## Requirements

- Android 8.0 (API 26) or higher
- JDK 11+ to build
- Android SDK with `ANDROID_HOME` set

## Build & Run

```bash
# Debug build
./gradlew assembleDebug

# Build and install on a connected device
./gradlew installDebug

# Run unit tests
./gradlew test
```

See [.claude/rules/build-install.md](.claude/rules/build-install.md) for full build instructions and troubleshooting.

## Architecture

The app follows MVVM with clean architecture layers, built with Jetpack Compose and Hilt.

```
JSON assets ──► OfficialAssetLoader ──► CivicsRepository ◄── Room DB (progress)
                                              │                     │
                                         DataStore (zip,       ProgressDao
                                         officials, 65/20)
                                              │
                                     ViewModels (StateFlow)
                                              │
                                      Compose screens
```

**Key libraries:** Jetpack Compose (Material 3), Hilt, Room, DataStore, Navigation Compose, kotlinx.serialization

## Project Structure

```
app/src/main/
├── assets/                  # Bundled JSON (officials, districts, zip codes)
├── kotlin/com/civicsready/
│   ├── domain/model/        # CivicsQuestion, Officials, QuestionProgress, TestResult
│   ├── data/                # Repository, Room DB, DataStore prefs, asset loader
│   ├── di/                  # Hilt AppModule
│   └── ui/
│       ├── home/            # Dashboard with stats and mode selection
│       ├── practice/        # Flashcard study mode (all questions)
│       ├── test/            # Simulated exam with results screen
│       ├── settings/        # Zip lookup, 65/20 toggle, official display
│       ├── components/      # FlashCard composable
│       ├── navigation/      # NavGraph
│       └── theme/           # Material 3 colors, typography, theming
└── res/                     # Drawables, strings, themes

app/src/test/                # JVM unit tests (ViewModels, Repository, models)
app/src/androidTest/         # Instrumented tests (Room DAO)
```

## Data Sources

All civic data is bundled offline as of January 2025 (119th Congress):

| File | Contents |
|---|---|
| `officials.json` | Governor, senators, and capital for all 50 states + DC |
| `districts.json` | U.S. House representatives by district |
| `zip_codes.json` | 349 zip codes mapped to state and congressional district |

Federal officials (President, VP, Speaker, Chief Justice) are hardcoded in `Officials.kt`.

After elections or appointments, update the JSON files and hardcoded values. The USCIS maintains current names at [uscis.gov/citizenship/testupdates](https://www.uscis.gov/citizenship/testupdates).

## License

This project is licensed under the [GNU General Public License v3.0](LICENSE).
