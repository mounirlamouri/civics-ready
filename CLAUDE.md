# Civics Ready Android App

## What this project is
An Android app to help US naturalization applicants study for the 2025 USCIS Civics Test.
The source of truth for all questions is the PDF at the repo root:
`2025-Civics-Test-128-Questions-and-Answers.pdf`

The full Android project lives at: `civicsready/`

---

## What I learned about the 2025 USCIS Civics Test

### Test structure
- 128 total questions organized into sections (American Government, American History, Symbols & Holidays)
- The officer asks **up to 20** of the 128 questions orally
- Applicant must answer **at least 12 correctly** to pass (60%)
- Some questions accept **any one** from a list; others require naming **2, 3, or 5** answers

### 65/20 Special Consideration
- Applicants 65+ with 20+ years as lawful permanent residents get 20 special (asterisked) questions
- Officer asks only **10 of those 20**; need **6 correct** to pass
- Asterisked question IDs: `2, 7, 12, 20, 30, 36, 38, 39, 44, 52, 61, 66, 74, 78, 86, 94, 113, 115, 121, 126`
- These applicants may also take the test in their language of choice

### Dynamic / location-dependent answers
These questions have answers that change with elections or the user's location:
| Q# | Topic | Source |
|---|---|---|
| 23 | US Senator | Bundled JSON by state (either senator accepted) |
| 29 | US Representative | Bundled JSON by congressional district |
| 30 | Speaker of House | Hardcoded; check uscis.gov/citizenship/testupdates |
| 38 | President | Hardcoded; check uscis.gov/citizenship/testupdates |
| 39 | Vice President | Hardcoded; check uscis.gov/citizenship/testupdates |
| 57 | Chief Justice | Hardcoded; check uscis.gov/citizenship/testupdates |
| 61 | Governor | Bundled JSON by state |
| 62 | State capital | Bundled JSON by state |

### Questions requiring multiple answers
| Q# | Minimum | Question |
|---|---|---|
| 10 | 2 | "Name two important ideas from the Declaration and Constitution" |
| 48 | 2 | "What are two Cabinet-level positions?" |
| 65 | 3 | "What are three rights of everyone living in the US?" |
| 67 | 2 | "Name two promises in the Oath of Allegiance" |
| 69 | 2 | "What are two examples of civic participation?" |
| 81 | 5 | "There were 13 original states. Name five." |
| 126 | 3 | "Name three national US holidays" |

---

## What I learned about the data

### Civic officials data (as of January 2025)
Bundled as three JSON files in `app/src/main/assets/`:

**officials.json** — All 50 states + DC mapped to:
`{name, governor, senator1, senator2, capital}`

**districts.json** — 119th Congress (sworn in January 2025):
All 435 House districts mapped as `"ST-N" → "Representative Name"`

**zip_codes.json** — 349 zip codes covering all states (major cities + capitals):
`"zip" → {s: "STATE", d: DISTRICT_NUMBER}`
- D.C. uses district 0 (no voting representative)
- Generated via `gen_zip_codes.py` at repo root
- Can be expanded from the US Census Bureau ZCTA crosswalk

### Federal officials as of January 2025
These are hardcoded in `Officials.kt → FederalOfficials`:
- President: Donald Trump
- Vice President: JD Vance
- Speaker of the House: Mike Johnson
- Chief Justice: John Roberts

**Update location:** `civicsready/app/src/main/kotlin/com/civicsready/domain/model/Officials.kt`

---

## What I learned about the architecture

### Data flow
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

### Key design decisions made
1. **Self-grading flashcard** (not multiple choice) — mirrors the real oral USCIS test
2. **Fully offline** — civic officials bundled as JSON, no API key needed; tradeoff is data goes stale after elections
3. **65/20 mode** included — toggle in Settings, automatically adjusts question pool and passing threshold
4. **Per-question progress** stored in Room from day one — future "practice weak areas" feature only needs a filter in `PracticeViewModel`
5. **Dynamic answers merged at query time** — `CivicsRepository.getQuestions()` injects real names into `CivicsQuestion.acceptableAnswers` before handing to ViewModels

### Hilt graph
- `CivicsDatabase` → `ProgressDao` provided in `AppModule`
- `UserPreferences`, `OfficialAssetLoader`, `CivicsRepository` are `@Singleton` with `@Inject constructor`
- All ViewModels use `@HiltViewModel`

### Navigation
Shared `TestViewModel` between `TestScreen` and `TestResultScreen` via the same back-stack entry (Hilt scopes to the navigation back-stack).

---

## Things to watch out for

### Data staleness
`officials.json` and `districts.json` need updating after:
- Gubernatorial elections (every 4 years, staggered)
- US Senate elections (every 6 years per senator, staggered)
- House elections (every 2 years — entire House)
- Presidential elections (every 4 years)
- Appointments (Chief Justice, Speaker)

The USCIS itself maintains a page at `uscis.gov/citizenship/testupdates` for the most current names.

### Zip code coverage
Only 349 zip codes are bundled. If a user's zip is not found, the app shows an error and asks them to verify manually. To expand coverage, populate `zip_codes.json` from the Census Bureau's ZCTA-to-congressional-district crosswalk.

### Multi-answer questions in flashcard mode
The `FlashCard` composable shows all acceptable answers and a note like "Name 2 of the following:". Because the test is self-graded, the user decides for themselves if they named enough correct answers. This is intentional — it matches how the real oral exam works.

### District 0 (DC)
DC has no voting House representative and no senators. `OfficialAssetLoader` returns the official USCIS-approved phrasing for these cases from `officials.json`.

---

## What I learned about the test suite

### Running tests
- **Unit tests (JVM):** `./gradlew test` — fast, no device needed
- **Instrumented tests (Android):** `./gradlew connectedAndroidTest` — requires a connected device or emulator

### Test dependencies
Added to `gradle/libs.versions.toml` and `app/build.gradle.kts`:
- **MockK** (`io.mockk:mockk`) — Kotlin-idiomatic mocking for classes and interfaces
- **kotlinx-coroutines-test** — `runTest`, `advanceUntilIdle()`, `StandardTestDispatcher`
- **JUnit 4** — test runner (standard for Android)
- **androidx.room:room-testing** — in-memory Room database for DAO tests
- **androidx.test.ext:junit** and **androidx.test:runner** — Android instrumented test support

### Test structure
```
app/src/test/kotlin/com/civicsready/          ← JVM unit tests
    util/
        MainDispatcherRule.kt                 ← replaces Dispatchers.Main in ViewModel tests
    domain/
        model/
            QuestionProgressTest.kt           ← successRate computed property
            TestResultTest.kt                 ← passed/threshold/correctCount
            OfficialsTest.kt                  ← isResolved + FederalOfficials constants
        QuestionsDataTest.kt                  ← 128 question integrity, 6520 pool, dynamic types
    data/
        repository/
            CivicsRepositoryTest.kt           ← dynamic answer injection, recordAnswer, flows
    ui/
        test/TestViewModelTest.kt             ← 20/10 question load, scoring, restart
        practice/PracticeViewModelTest.kt     ← counts, finish(), restart
        settings/SettingsViewModelTest.kt     ← zip validation, lookupZip states, toggle
        home/HomeViewModelTest.kt             ← uiState combine, accuracy, toggleOrderedMode

app/src/androidTest/kotlin/com/civicsready/   ← Instrumented tests (need device/emulator)
    data/
        local/
            ProgressDaoTest.kt                ← in-memory Room DB: upsert/replace/clearAll
```

### Conventions used in tests
- **ViewModels** are constructed directly (no Hilt) with a `mockk<CivicsRepository>()` dependency
- **`MainDispatcherRule`** (`@get:Rule`) must be applied to any test class that creates a ViewModel, so `viewModelScope` coroutines run on the test dispatcher
- Call `advanceUntilIdle()` after constructing a ViewModel (or triggering a `viewModelScope.launch`) to let the `init {}` coroutine complete before asserting
- **`CivicsRepository` unit tests** mock `ProgressDao`, `UserPreferences`, and `OfficialAssetLoader` directly; the repository is re-constructed per test via a `buildRepo()` helper so that flow mocks set before construction are captured correctly
- Use `coEvery` / `coVerify` for suspend functions; `every` / `verify` for regular functions and Flow properties
- Set `isOrderedMode = flowOf(true)` in ViewModel tests that care about question ordering to avoid non-determinism from shuffling
- `ProgressDaoTest` uses `Room.inMemoryDatabaseBuilder` with `.allowMainThreadQueries()` so no `Dispatchers` wiring is needed

### What is NOT tested (and why)
- **Compose UI / screens** — requires Compose test infrastructure (`compose-ui-test`); add separately if needed
- **`OfficialAssetLoader`** JSON parsing — requires a real Android `Context` and asset files; covered indirectly by `CivicsRepositoryTest` via mocked results
- **`UserPreferences`** DataStore — requires Android `Context`; its contract is verified through `CivicsRepositoryTest` mock interactions
- **Navigation** — integration-level; best covered with a dedicated Compose navigation test