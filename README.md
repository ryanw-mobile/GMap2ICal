# GMap2iCal - Google Maps Timeline to iCal <br/>![Gradle Build](https://github.com/ryanw-mobile/GMap2ICal/actions/workflows/main_build.yml/badge.svg) [![codecov](https://codecov.io/github/ryanw-mobile/GMap2ICal/graph/badge.svg?token=4NZUBRYHT0)](https://codecov.io/github/ryanw-mobile/GMap2ICal)

### My first Kotlin MultiPlatform / Compose MultiPlatform (Desktop) App

<p><img src="cover.webp" style="width: 100%; max-width: 1000px; height: auto;" alt="cover image" style="width: 100%; max-width: 1000px; height: auto;"></p>

A small utility written in Kotlin to convert Google Maps Activity Timeline to iCal (.ics) files for calendar import.

This is a Kotlin Multiplatform App but with only the Desktop implementation. It can be built and executed using IntelliJ
IDEA on the desktop environment.

This is my first non-Android Kotlin project. It is within expectation that everything inside this project can be messy.
I treat this as my playground that allows me to make all kinds of experiments and mistakes. While I thank you for your
visit, I would appreciate if you could take things easy when you see me doing something crazy here. Thank you.

<div style="text-align:center"><img src="screenshot-221226.png" style="width: 100%; max-width: 1000px; height: auto;" /></div>

## TL;DR - Status and Roadmap

This App is fully functional.

* 169 unit tests written as at 1 Jan 2023 to protect changes
* Dark theme auto detect and switching
* Reapplied JetBrain's official KMP template to make the project structure up-to-date

### TODO:

* Planned enhancements are
  now [logged as issues](https://github.com/ryanw-mobile/GMap2ICal/issues?q=is%3Aopen+is%3Aissue+label%3Arefactor%2Cfeature%2Cfix%2Ctest).

## Background

I travelled quite a lot before the pandemic. Very often my daily itinerary changes at the last minute. This made me a
headache recalling where I have been to previously.

I use Google Maps for navigation heavily, and I have it tracked my movement for quite many years.

I had an idea to extract my activity timeline from the Google Maps, but unfortunately there is no public API available.
It might be possible to achieve that using some web scrapping techniques, but this approach is not likely to be an
elegant one.

That is why I am trying another way round: By setting Google Maps to regularly export my activity timeline as JSON
files, I can then process them using this Kotlin utility, and generate iCal (.ICS) files which I can then import to my
calendars.

By doing so, I can keep a full record for the places I have actually been, and also the rough mileages I have spent on
the road.

## Skills covered

Trying to reuse all my Android development knowledge as possible, otherwise native replacements have been applied.

### High level architecture

* Kotlin
* Kotlin Flow
* Kotlin Coroutines
* Compose Desktop UI
* MVVM architecture with use-cases
* Gradle Version Catalog

### Major libraries used

* [Koin](https://github.com/InsertKoinIO/koin) - Dependency injection
* [Ktor](https://ktor.io/) - HTTP Client (Option 1, default)
* [Retrofit2](https://square.github.io/retrofit/) - HTTP Client (Option 2)
* [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html) - For JSON parsing. Replacing Moshi
* [Time Zone Map](https://github.com/dustin-johnson/timezonemap) - determine time zone
* [jSystemThemeDetector](https://github.com/Dansoftowner/jSystemThemeDetector) - detect system dark theme setting
* [Napier](https://github.com/AAkira/Napier) - Logging library for Kotlin Multiplatform
* [MoKo MVVM](https://github.com/icerockdev/moko-mvvm) - Mobile Kotlin Model-View-ViewModel architecture components
* [JUnit 5](https://github.com/junit-team/junit5) - Tests
* [KOTest](https://kotest.io/) - Test framework
* [MockK](https://mockk.io/) - Mocking library
* [Bitrise](https://app.bitrise.io/) - CI
* [Kover](https://github.com/Kotlin/kotlinx-kover) - code coverage
* [codecov](https://codecov.io/) - code coverage
* [Ktlint Gradle](https://github.com/jlleitschuh/ktlint-gradle) - ktlint plugin to check and apply code autoformat
* [Mend Renovate](https://www.mend.io/free-developer-tools/renovate/) - automatic dependency updates

## How to export the Google Maps Timeline as the data set for processing

<div style="text-align:center"><img src="preview.png" style="width: 100%; max-width: 1000px; height: auto;" /></div>.

1. Go to [Google Takeout](https://takeout.google.com/)
2. Make sure you have included **"Location History"** in your export request.
3. Create an export and wait for completion. _This process can take a long time (possibly hours or days) to complete.
   You'll receive an email when your export is done._
4. Unzip the data file.
5. The JSON files under `/Location History/Semantic Location History` are the files we need.
6. Move the JSON files to a directory, for example `./composeApp/src/commonMain/resources` of this project.
7. Update, or create yor own configuration file `uk.ryanwong.gmap2ics.app.configs.DefaultConfig` to specify the input
   and
   output paths.
8. If you have created your own configuration file, update the file path in `Main.kt`
9. Run the project on IntelliJ IDEA

### Running the app

There is a plan to deprecate the configuration file automate the app build process. However, before that happened,
you still have to properly maintain your own configuration file to build and run the app.

To start the app from command line: `./gradlew runReleaseDistributable`
