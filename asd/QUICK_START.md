# Quick Start - Build 67.apk

## ⚡ Fastest Way to Get 67.apk

### Method 1: Android Studio (Recommended - Easiest)

1. **Download & Install Android Studio**
   - https://developer.android.com/studio
   - Install with default settings

2. **Open Project**
   - Launch Android Studio
   - Click "Open" → Select this folder
   - Wait for sync (first time takes 5-10 minutes)

3. **Build APK**
   - Menu: `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
   - Wait 1-2 minutes
   - Click "locate" when done, or find: `app/build/outputs/apk/release/67.apk`

4. **Install on Phone**
   - Copy `67.apk` to your phone
   - Enable "Install from Unknown Sources"
   - Tap to install

### Method 2: Command Line (If Android Studio Already Installed)

**Windows:**
```bash
build-67-apk.bat
```

**Mac/Linux:**
```bash
chmod +x build-67-apk.sh
./build-67-apk.sh
```

**Or directly:**
```bash
gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/67.apk`

## 📱 After Installing on Phone

1. Enable Developer Options:
   - Settings → About Phone → Tap "Build Number" 7 times

2. Enable Mock Locations:
   - Settings → Developer Options → Enable "Allow mock locations"

3. Set This App as Mock Location App:
   - Settings → Developer Options → Select mock location app → Choose this app

4. Grant Permissions:
   - Open the app → Grant location permissions when asked

## ✅ You're Ready!

The app is now ready to use:
- Set locations for Pokemon Go
- Enable unlimited trades feature
- Teleport to friend locations
- Use walking simulation

**Remember: For Private & School Use Only!**

