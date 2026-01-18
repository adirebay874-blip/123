#!/bin/bash

echo "========================================"
echo "Building 67.apk for Pokemon Go Spoofer"
echo "========================================"
echo ""

# Check if gradlew exists
if [ ! -f "gradlew" ]; then
    echo "Error: gradlew not found."
    echo "Please make sure you're in the project root directory."
    echo ""
    echo "If gradlew doesn't exist, you need to:"
    echo "1. Install Android Studio"
    echo "2. Open this project in Android Studio"
    echo "3. Let it sync and create gradlew files"
    echo ""
    exit 1
fi

# Make gradlew executable
chmod +x gradlew

echo "Cleaning previous builds..."
./gradlew clean

echo ""
echo "Building release APK (this may take a few minutes)..."
./gradlew assembleRelease

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "Build Successful!"
    echo "========================================"
    echo ""
    echo "Your APK file: 67.apk"
    echo "Location: app/build/outputs/apk/release/67.apk"
    echo ""
    echo "Next steps:"
    echo "1. Copy 67.apk to your Android phone"
    echo "2. Enable 'Install from Unknown Sources' in Settings"
    echo "3. Tap the APK file to install"
    echo "4. Enable 'Allow mock locations' in Developer Options"
    echo ""
else
    echo ""
    echo "========================================"
    echo "Build Failed!"
    echo "========================================"
    echo ""
    echo "Please check the error messages above."
    echo "Make sure you have:"
    echo "- Android SDK installed"
    echo "- Java JDK installed"
    echo "- Internet connection (for dependencies)"
    echo ""
    echo "Try opening the project in Android Studio instead."
    echo ""
fi

