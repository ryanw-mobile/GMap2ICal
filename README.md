# GMap2iCal - Google Maps Timeline to iCal

[![Build Status](https://app.bitrise.io/app/0667f15b93a99b17/status.svg?token=2nCEgRSOnFG82N7YWNSPBQ&branch=master)](https://app.bitrise.io/app/0667f15b93a99b17) [![codecov](https://codecov.io/gh/ryanwong-uk/GMap2ICal/branch/master/graph/badge.svg?token=4NZUBRYHT0)](https://codecov.io/github/ryanwong-uk/GMap2ICal)

### My first Compose for Desktop App

A small utility written in Kotlin to convert Google Maps Activity Timeline to iCal (.ics) files for calendar import.

This is not an Android App. It can be built and executed using IntelliJ IDEA on the desktop environment.

This is my first non-Android Kotlin project. It is within expectation that everything inside this project can be messy.
I treat this as my playground that allows me to make all kinds of experiments and mistakes. While I thank you for your
visit, I would appreciate if you could take things easy when you see me doing something crazy here. Thank you.

<div style="text-align:center"><img src="screenshot-221226.png" /></div>

## TL;DR - Status and Roadmap

This App is fully functional.

* 167 unit tests written as at 9 Sep 2022 to protect changes
* Unit tests for composable functions (adding, but limited assertions available)
* Dependency Injection: Dagger 2
* Integrate SQLite (`SQLDelight`) as preferences store, and Google Maps Place API caches

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

## Skills covered:

Trying to reuse all my Android development knowledge as possible, otherwise native replacements have been applied.

### High level architecture

* Kotlin
* Kotlin Flow
* Kotlin Coroutines
* Compose Desktop UI
* MVVM architecture with use-cases
* Gradle Version Catalog

### Major libraries used

* [Ktor](https://ktor.io/) - HTTP Client (Option 1, default)
* [Retrofit2](https://square.github.io/retrofit/) - HTTP Client (Option 2)
* [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html) - For JSON parsing. Replacing Moshi
* [Time Zone Map](https://github.com/dustin-johnson/timezonemap) - determine time zone
* [Napier](https://github.com/AAkira/Napier) - Logging library for Kotlin Multiplatform
* [JUnit 5](https://github.com/junit-team/junit5) - Tests
* [KOTest](https://kotest.io/) - Test framework
* [MockK](https://mockk.io/) - Mocking library
* [Bitrise](https://app.bitrise.io/) - CI
* [Kover](https://github.com/Kotlin/kotlinx-kover) - code coverage
* [codecov](https://codecov.io/) - code coverage
* [Ktlint Gradle](https://github.com/jlleitschuh/ktlint-gradle) - ktlint plugin to check and apply code autoformat
* [Mend Renovate](https://www.mend.io/free-developer-tools/renovate/) - automatic dependency updates

## How to export the Google Maps Timeline as the data set for processing

<div style="text-align:center"><img src="preview.png" /></div>.

1. Go to [Google Takeout](https://takeout.google.com/)
2. Make sure you have included **"Location History"** in your export request.
3. Create an export and wait for completion. _This process can take a long time (possibly hours or days) to complete.
   You'll receive an email when your export is done._
4. Unzip the data file.
5. The JSON files under `/Location History/Semantic Location History` are the files we need.
6. Move the JSON files to a directory, for example `./src/jvmMain/resources` of this project.
7. Update, or create yor own configuration file `uk.ryanwong.gmap2ics.app.configs.DefaultConfig` to specify the input
   and
   output paths.
8. If you have created your own configuration file, update the file path in `Main.kt`
9. Run the project on IntelliJ IDEA
