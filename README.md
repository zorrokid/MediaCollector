# MyBasicJetpackComposeApp

Android app to get to familiar with Android app development with [Jetpack Compose](https://developer.android.com/jetpack/compose) and [Kotlin](https://kotlinlang.org/)

The goal of this project is to build a good basic structure of a Jetpack Compose app backed with Firebase services.

The second goal is to use this as a base on my hobby mobile app projects.

I've been experimenting building mobile apps with Flutter earlier and been wanting to get into native Android side of things with Jetpack Compose so here we go.

This is very much inspired by the following article and example app and codelab:
- https://firebase.blog/posts/2022/05/adding-firebase-auth-to-jetpack-compose-app/
- https://firebase.google.com/codelabs/build-android-app-with-firebase-compose

# Authentication and data storeage

Unsing [Firebase](https://firebase.google.com/docs/android/setup) authentication and Firestore database.

# User Interface

Using [Compose Material 3](https://developer.android.com/jetpack/androidx/releases/compose-material3).

# Dependency Injection

Using [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection.

# Dependencies

## Google code scanner

[Google code scanner](https://developers.google.com/ml-kit/vision/barcode-scanning/code-scanner) is used for barcode scanning. No camera permission is needed for this since scanning task is provided by Google Play services and only the scan results are provided for the app. App is configure to download scanner module automatically when app is installed from Play Store. Found a really nice example how to use barcode scanner module [here](https://github.com/akash251/Clean-architecture-barcode-scanner-using-Jetpack-compose)

# Data persistance

Data is stored in Firebase Firestore database.

## User data

### Collection items

Table: collectionItems

Collection items can be promoted as library collection items when selected common data is available for all users. This has not been implemented yet.

When collection item is promoted as library collection item, the original collection item will remain as is, it's only linked to the library collection item that was created. Only the ownder of original collection item can access it and all the other users can access the library collection item.

## Static data

Static data is pre initialized to database and is common for all users.

### Condition classifications

Table: conditionClassifications

This is used to classify collection items. Currently user can decide whether it's an overall classification or for example only for media condition. Later on condition can be specified for each media item, container, sleeve card etc separately.

|name|value|
|-|-|
|Bad|1|
|Poor|2|
|Fair|3|
|Good|4|
|Mint|5|
|New|6|

### Release areas

Table: releaseAreas

|name|countryCodes|
|-|-|
|Nordic countries|FI,SE,DK,NO|
|Finland|FI|
|Sweden|SE|
|...|...|

# Deploy

## Build Variants

Currently there's one variant configured for full feature release: "full"

To build this variant, select "Build" > "Select Build Variant" > "fullDebug" / "fullRelease"

## Build Signed Bundle

Select: "Build" > "Generate Signed Bundle / APK" > APK

To install build apk-package:

default: 
    
    adb install app/release/app-release.apk

full variant:  

    adb install app/full/release/app-full-release.apk

