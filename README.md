# TimeZen for Android — Track Your Time. Reclaim Your Life.

A native Android digital wellbeing app (Kotlin, Jetpack Compose, Material 3).
Original branding and code; inspired by the *type* of app YourHour is, not copied from it.

> ## Status: Phase 1 of 12 — project skeleton
>
> **Phase 1 has NOT been compiled or run yet.** It was written in an environment with no Android SDK
> and no access to Google's Maven repository, so Gradle could not run. Only static checks were done
> (XML is well-formed, brackets balance, every project import resolves, every `libs.*` name exists in
> the version catalog, 20 unit tests are written). The first Gradle sync on your computer is the first
> real build. Send me the full error text if anything fails.
>
> **Everything on the dashboard is SAMPLE DATA** (labeled with an orange banner). No real usage is
> tracked in Phase 1. Real tracking starts in Phase 2.

---

## What Phase 1 contains

- Gradle project: AGP 9.5.0, Gradle 9.6.0, Jetpack Compose (BOM 2026.09.00), Material 3, Navigation Compose
- `minSdk 26`, `targetSdk 36` (Google Play requirement for new apps/updates from 2026-08-31), `compileSdk 37`
- TimeZen brand theme: dark-first, light mode, dark/light/system switch (not saved yet), scalable fonts
- Original launcher icon (adaptive + themed/monochrome)
- Bottom navigation: Home, Apps, Focus, Reports, Settings; secondary screens: App limits, Reels & Shorts, Challenges
- Dashboard screen with an animated circular progress ring, device-activity tiles, most-used apps with
  progress bars, highlights, weekly bar chart, productivity score and habit level (all sample data)
- MVVM: `DashboardViewModel`, `AppViewModel`, state classes, stateless screens with `@Preview`
- 20 unit tests for the logic (time formatting, green/orange/red status, dashboard calculations)
- **No permissions requested** — deliberately

Not in Phase 1 (planned): Hilt, Room, DataStore, WorkManager, charts library, real usage tracking,
blocking, accessibility service, floating timer, focus mode, reports, challenges, widgets.

### Why Hilt/Room are not in Phase 1
Hilt, KSP and Room need exact version pairs that could not be verified from where this code was written.
Adding unverified dependencies to a project that has never been built would risk a failing first sync.
They are added in Phase 2 (when the first repository exists), after your Phase 1 build proves the base works.

---

## Architecture (how the code is organised)

```
com.timezen
├── MainActivity.kt            single Activity; sets the theme and shows TimeZenApp
├── core/util/                 small helpers (formatDuration)
├── domain/model/              plain Kotlin types: UsageStatus, HabitLevel, ThemeMode
├── domain/usecase/            (Phase 2+) business rules, one class per rule
├── data/local/                (Phase 4) Room database
├── data/repository/           (Phase 2+) the only place that talks to Android APIs / database
├── presentation/              screens + ViewModels, one folder per feature
│   ├── dashboard/  common/  settings/
├── navigation/                Screen routes, bottom bar, NavHost
├── ui/theme/                  colors, typography, MaterialTheme
├── ui/components/             reusable composables (cards, ring, bar chart, rows)
├── services/{usage,accessibility,floating}/   (Phases 2, 6, 7) Android services
├── workers/  receivers/       (Phase 5+) WorkManager jobs, boot/time-change receivers
```

**Data flows one way** (this is what "MVVM" means here):

```
Android APIs / Room  →  Repository  →  UseCase  →  ViewModel (StateFlow)  →  Screen (Compose)
                                                         ↑                          │
                                                         └──── user actions ────────┘
```

Rule for the whole project: **a screen never calls Android APIs directly.** That keeps screens easy to
preview and test, and keeps local data separate from any future server data.

### Kotlin concepts used in Phase 1 (beginner notes)

| Concept | Where | Plain meaning |
| --- | --- | --- |
| `val` / `var` | everywhere | `val` can't be reassigned, `var` can. Prefer `val`. |
| `data class` | `DashboardUiState` | A bag of values; Kotlin writes `equals`, `toString`, `copy` for you. |
| `enum class` | `UsageStatus`, `HabitLevel` | A fixed list of allowed values. |
| `sealed class` | `Screen` | A closed family of types; `when` can check you handled all of them. |
| `object` | `DashboardSampleData` | A class with exactly one instance. |
| Null safety `?` | `mostUsedApp: AppUsageUi?` | A value that may be missing; Kotlin forces you to handle it (`?.`, `?:`). |
| Lambda | `onClick = { ... }` | A small function passed as a value. |
| `@Composable` | all UI functions | A function that *describes* UI. Compose re-runs it when state changes. |
| `StateFlow` | ViewModels | An observable value; the UI redraws when it changes. |
| `remember` | `DashboardHeader` | Keep a value between redraws. |
| State hoisting | `DashboardScreen` vs `DashboardContent` | The stateful wrapper owns data; the inner composable only draws. |

---

## Run it (Windows, Android Studio)

### 1. Install the tools
1. Install the **current stable Android Studio** from developer.android.com/studio (it bundles a JDK that is new enough; AGP 9.5 needs JDK 17+).
2. Start it and finish the setup wizard (it installs the Android SDK).
3. **Tools → SDK Manager → SDK Platforms:** tick **API 37 (the newest Android platform listed in SDK Manager)** (needed because `compileSdk = 37`), and also **Android 16 (API 36)**. Apply.
4. **SDK Tools** tab: make sure **Android SDK Build-Tools 36.0.0** (or newer), **Platform-Tools** and **Android Emulator** are installed.

### 2. Open the project
1. Unzip `timezen-android-phase1.zip`.
2. Android Studio → **File → Open** → choose the `timezen-android` folder (the one containing `settings.gradle.kts`).
3. Wait for **Gradle sync** (first time it downloads Gradle 9.6.0 and libraries; needs internet; several minutes).
   Android Studio creates `local.properties` with your SDK path automatically.
4. If a yellow bar offers to install a missing SDK platform or accept licenses, accept.

### 3. Run the unit tests
In the Project panel right-click `app/src/test` → **Run 'Tests in ...'**. Expect 20 passing tests.

### 4. Run on a real phone (recommended)
1. On the phone: **Settings → About phone → tap "Build number" 7 times** to enable Developer options.
2. **Settings → System → Developer options → turn on USB debugging.**
3. Connect by USB cable; accept the "Allow USB debugging?" prompt on the phone.
4. In Android Studio pick your phone in the device drop-down at the top, press the green **Run ▶** button.

(Or use an emulator: **Tools → Device Manager → Create Device**. Real-device testing is mandatory from Phase 2 on,
because emulators don't behave like real phones for usage statistics.)

### 5. Build an APK
- **Debug APK (for your own phone):** Build → Build Bundle(s) / APK(s) → **Build APK(s)**. Android Studio shows a "locate" link;
  the file is `app/build/outputs/apk/debug/app-debug.apk`. Copy it to your phone and open it
  (allow "install unknown apps" for your file manager when asked).
- **Release APK/AAB for Google Play:** needs a signing key; set up in Phase 12. Don't build releases yet.

This project has no `gradlew` script on purpose (it needs a binary file that could not be generated here).
Android Studio uses `gradle/wrapper/gradle-wrapper.properties` and downloads Gradle itself, so the menu steps above work.

---

## Phase 1 manual checklist (after the build works)

- [ ] App installs and opens with the TimeZen icon on your launcher
- [ ] Dashboard shows an **orange "Sample data" banner**
- [ ] The ring animates to ~77% in green, status says "Within target"
- [ ] Scrolling shows device activity, most used apps, highlights, weekly chart, scores, "Explore"
- [ ] Bottom bar switches Home / Apps / Focus / Reports / Settings; "Coming in Phase …" screens appear
- [ ] Tapping a tile under **Explore** opens a screen with a back arrow and **no** bottom bar
- [ ] Settings → switching Dark / Light / Match my device changes the whole app instantly
- [ ] Rotate the phone: nothing resets (ViewModel survives)
- [ ] Phone Settings → Display → Font size → Largest: text scales and nothing is cut off badly
- [ ] App asks for **no permissions** (long-press icon → App info → Permissions shows none)

---

## Troubleshooting

| Symptom | Fix |
| --- | --- |
| Sync error mentioning *Compose compiler* and a Kotlin version (e.g. "requires Kotlin version X but you appear to be using Y") | Set the **same** version in two places: `kotlin = "..."` in `gradle/libs.versions.toml` **and** `kotlin-gradle-plugin:...` in the root `build.gradle.kts`. Use the version number from the error message. |
| `Failed to find target with hash string 'android-37'` / platform missing | Install **API 37 (the newest Android platform listed in SDK Manager)** in SDK Manager. If it does not exist for you, change `compileSdk = 37` to `36` in `app/build.gradle.kts`. |
| "Dependency requires libraries and applications that depend on it to compile against version 37 or later" | Same fix: `compileSdk = 37` plus the API 37 platform installed. |
| `Unsupported Gradle` / needs Gradle 9.6.0 | Don't change the wrapper file; let Android Studio download it (File → Sync Project with Gradle Files). |
| JDK errors | File → Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK → pick *jbr-21* / *Embedded JDK*. |
| Plugin `com.android.application` version not found | Android Studio too old for AGP 9.5: update Studio (Help → Check for Updates). |
| Red errors only inside a `.kt` file | Copy the file name, line and message to me. |

If the *Kotlin version* line keeps fighting you, there is a guaranteed fallback: create a new project with
**File → New → New Project → Empty Activity** in your Android Studio, note the Kotlin/AGP versions in its
`libs.versions.toml`, and put those versions into this project's catalog.

---

## Design

Colors (from the brief): Primary Cyan `#35C9D8`, Secondary Green `#67D391`, Dark Background `#101719`,
Card `#1D2529`, Secondary text `#9CAAB0`, Warning `#F5A623`, Danger `#F05252`.
Status colors: **green** under 80% of the target, **orange** 80–99%, **red** at or above 100%.
In light mode the cyan/green are darkened so text stays readable.

The reference screenshots mentioned in the project brief were **not visible** to the assistant, so this
dashboard follows the written brief. Send the screenshots again (attach them to a chat message) and the
layout will be adjusted to match them in Phase 3.

---

## Honest notes about later phases

- **Usage statistics** come from Android's `UsageStatsManager`, which needs the *Usage Access* permission that
  only the user can grant in system Settings. Events can be missing or delayed on some phones.
- **Blocking** is done by showing TimeZen's own screen over/instead of the app via an Accessibility Service. Android does
  not let ordinary apps disable or uninstall other apps, and TimeZen will never claim to.
- **Reels/Shorts detection** reads accessibility node information, which changes when Instagram, YouTube, etc.
  update their apps and differs by language. It can't be 100% reliable; rules will be configurable, with whole-app
  blocking as a fallback.
- **Google Play** treats Accessibility Service use and "Display over other apps" as sensitive; the app needs a
  clear in-app disclosure and a Play Console declaration. That is planned for Phase 12.
- This version is **local-first**: it does not use or require a server. The Django backend stays an optional
  future layer.
