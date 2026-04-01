Improvement action items from project review (2026-03-31):

## High Priority

- [ ] **Data staleness recovery**: Add a mechanism for users to get updated federal official names (e.g., remote JSON on GitHub Pages, or deep link to uscis.gov/citizenship/testupdates when staleness warning triggers). Currently Officials.kt hardcodes names with no update path.

- [ ] **Database migration strategy**: Replace `fallbackToDestructiveMigration()` in AppModule.kt with proper Room migrations before any schema change, to avoid silently wiping user progress.

- [ ] **Fix race condition on fast navigation**: `recordAnswer` in PracticeViewModel and TestViewModel fires in `viewModelScope` and can be cancelled if the user navigates away. Use `NonCancellable` or move the write to a repository-scoped coroutine.

- [ ] **Expand zip code coverage**: Only 349 of ~41,000 zip codes are bundled. Expand using Census Bureau ZCTA crosswalk to improve district-level resolution for users outside major cities.

## Medium Priority

- [ ] **Surface JSON parsing failures**: OfficialAssetLoader silently returns empty strings on parse errors. Add validation after loading and show an error state if critical fields are empty.

- [ ] **Add logging**: No logging exists in the data layer. Add Timber with strategic log points in CivicsRepository and OfficialAssetLoader for production debugging.

- [ ] **Accessibility improvements**: Respect `reduceMotion` setting for FlashCard flip animation, add missing `contentDescription` on icons, verify color contrast for pass/fail indicators (colorblind users).

- [ ] **Add Compose UI tests**: No screens are tested at the UI level. Add `compose-ui-test` dependency and cover critical paths: take a test, see results, settings zip lookup.

## Low Priority

- [ ] **Refactor raw SQL in ProgressDao**: Replace handwritten `INSERT...ON CONFLICT` in `recordAnswer` with a Room `@Transaction` using separate `@Query`/`@Update` calls for maintainability.

- [ ] **Improve error messages**: Practice and test screens show generic "Failed to load questions" errors. Add more specific messages to distinguish database vs data loading failures.

- [ ] **Add crash reporting**: Add Firebase Crashlytics or similar for the non-technical target audience who won't report issues manually.

- [ ] **Add onboarding flow**: Prompt for zip code on first launch instead of requiring users to discover the Settings screen to enable location-dependent questions.
