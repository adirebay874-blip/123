# How to Build 67.apk

## Quick Build Instructions

### Option 1: Using Android Studio (Easiest)

1. **Install Android Studio** (if not already installed)
   - Download from: https://developer.android.com/studio
   - Install Android SDK and build tools

2. **Open Project**
   - Open Android Studio
   - Click "Open" and select this project folder
   - Wait for Gradle sync to complete

3. **Build APK**
   - Go to: `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
   - Wait for build to complete
   - The APK will be named **67.apk** and located at:
     - `app/build/outputs/apk/release/67.apk`

4. **Transfer to Phone**
   - Copy `67.apk` to your Android phone
   - Enable "Install from Unknown Sources" in phone settings
   - Tap the APK file to install

### Option 2: Using Command Line

**Windows:**
```bash
gradlew.bat assembleRelease
```

**Mac/Linux:**
```bash
./gradlew assembleRelease
```

The APK will be at: `app/build/outputs/apk/release/67.apk`

### Option 3: Using the Build Script

**Windows:**
```bash
build-apk.bat
```

This will automatically build and show you where the APK is located.

## Requirements

- Java JDK 8 or higher
- Android SDK (comes with Android Studio)
- Internet connection (for downloading dependencies)

## Troubleshooting

**If Gradle sync fails:**
- Make sure you have internet connection
- Check that Android SDK is installed
- Try: `File` → `Invalidate Caches / Restart` in Android Studio

**If build fails:**
- Make sure all files are present
- Check that Java JDK is installed
- Verify Android SDK path is correct

## After Building

1. Transfer `67.apk` to your Android phone
2. Enable "Install from Unknown Sources" in Settings → Security
3. Tap the APK file to install
4. Enable "Allow mock locations" in Developer Options
5. Grant location permissions when prompted

## Note

The APK will be named **67.apk** as requested. Make sure to enable Developer Options and "Allow mock locations" on your Android device before using the app.

