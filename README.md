# Expense Tracker

A clean, modern personal finance app for Android to track expenses, manage budgets, and gain insights into your spending — all stored locally on your device.

<a href="https://play.google.com/store/apps/details?id=com.roaa.expensetracker">
  <img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" width="200"/>
</a>

---

## Screenshots

<p float="left">
  <img src="screenshots/Group 17.png" width="19%" />
  <img src="screenshots/Group 16.png" width="19%" />
  <img src="screenshots/Group 15.png" width="19%" />
  <img src="screenshots/Group 14.png" width="19%" />
  <img src="screenshots/Group 13.png" width="19%" />
</p>

---

## Features

- **Transaction Tracking** — Log expenses and income with categories, notes, dates, and payment methods
- **Day / Month View** — Browse transactions by day or scroll through a monthly calendar view
- **Budget Management** — Set budgets for any date range with daily spending limits and visual progress charts
- **Multiple Accounts** — Track balances across bank accounts and cash wallets; log balance corrections
- **Statistics** — Bar charts, donut charts, and category breakdowns over custom date ranges
- **Categories** — Comes with 17 default categories; add and customize your own
- **Home Screen Widget** — Glance-based minimal widget for quick balance overview
- **Daily Reminders** — Optional notification to remind you to log transactions
- **Material You** — Dynamic color theming adapts to your wallpaper on Android 12+
- **Dark / Light / System theme** support
- **Multiple currency** support

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose + Material Design 3 |
| Architecture | MVVM + Repository pattern |
| DI | Hilt |
| Database | Room (SQLite) |
| Async | Coroutines + Flow |
| Preferences | DataStore |
| Background work | WorkManager |
| Widget | Glance |
| Charts | Vico, MPAndroidChart, Charty |
| Animations | Lottie |
| Crash reporting | Firebase Crashlytics |

---

## Requirements

- Android 8.0 (API 26) or higher

---

## Getting Started

```bash
git clone https://github.com/Abhi22-github/SpendTracker.git
cd SpendTracker
```

Then open the project in Android Studio, or build from the command line:

```bash
# Debug APK
./gradlew assembleDebug

# Release AAB
./gradlew bundleRelease
```

---

## License

This project is licensed under the [Apache License 2.0](LICENSE).

```
Copyright 2026 Abhi

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
