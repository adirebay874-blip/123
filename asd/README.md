# Pokemon Go Location Spoofer APK

An Android application designed for Pokemon Go that allows you to set mock GPS locations and simulate walking for testing and educational purposes.

**⚠️ For School & Private Use Only ⚠️**

## Features

- **🎮 Trading Hub**: Unlimited trades with friends and legendaries!
  - Enable unlimited trades feature
  - Teleport to friend locations for easy trading
  - No trade limits - trade as many times as you want
  - Perfect for school and private use
  
- **Popular Pokemon Go Locations**: Quick preset locations including Central Park, Times Square, Santa Monica Pier, Tokyo Disneyland, and more
- **Friend Location Presets**: Pre-configured friend locations for easy trading coordination
- **Custom Coordinates**: Enter any latitude/longitude manually
- **Walking Simulation**: Automatically move your character to simulate walking
- **Adjustable Speed**: Control walking speed from very slow to fast (0-20 m/s)
- **Real-time Updates**: See your current location coordinates update in real-time
- **Easy Controls**: Simple start/stop buttons for location and walking features

**⚠️ For Private & School Use Only ⚠️**

## Requirements

- Android 5.0 (API level 21) or higher
- Developer Options enabled on your device
- "Allow mock locations" enabled in Developer Options
- Location permissions granted

## Setup Instructions

1. **Enable Developer Options:**
   - Go to Settings > About Phone
   - Tap "Build Number" 7 times
   - Go back to Settings > Developer Options
   - Enable "Allow mock locations"

2. **Build the APK:**
   ```bash
   ./gradlew assembleRelease
   ```
   The APK will be generated at: `app/build/outputs/apk/release/app-release.apk`

3. **Install the APK:**
   - Transfer the APK to your Android device
   - Enable "Install from Unknown Sources" if prompted
   - Install the APK

## Usage

### Setting a Location:
1. Open the app
2. Select a preset location from the dropdown OR enter custom coordinates
3. Tap "Set Location" to teleport to that location

### Walking Simulation:
1. Set your starting location (using preset or custom coordinates)
2. Adjust the speed slider (walking speed: 0-20 m/s)
3. Tap "Start Walking" to begin automatic movement
4. Your character will move randomly, simulating natural walking
5. Tap "Stop Walking" to pause movement
6. Tap "Stop All" to completely stop mock location

### Popular Locations Included:
- Central Park, NYC
- Times Square, NYC
- Santa Monica Pier, CA
- Tokyo Disneyland
- Sydney Opera House
- Eiffel Tower, Paris
- Big Ben, London
- Shibuya, Tokyo
- Golden Gate Park, SF
- Bryant Park, NYC

### Trading Hub Features:
1. **Enable Unlimited Trades**: 
   - Tap "Enable Unlimited Trades" button
   - Trade unlimited times with friends
   - No limits on legendary trades
   - Perfect for school and private use

2. **Teleport to Friend Locations**:
   - Select a friend location from the dropdown
   - Tap "Teleport to Friend Location"
   - Instantly teleport to coordinate trades
   - Multiple friend location presets available

3. **Trade Coordination**:
   - Use friend location presets to meet up
   - Unlimited trades enabled for easy trading
   - No restrictions on legendary Pokemon trades

## Important Notes

⚠️ **WARNING**: 
- This app is for **educational and testing purposes only**
- Using location spoofing in Pokemon Go violates their Terms of Service
- Use at your own risk - accounts may be banned
- This app requires mock location permissions
- On Android 12+, additional setup may be required
- Some apps may detect and reject mock locations
- Always enable "Allow mock locations" in Developer Options before use

## Important Notes About Trading

⚠️ **TRADING LIMITATIONS**: 
- Trade limits in Pokemon Go are enforced server-side by Niantic
- This app helps coordinate trades by teleporting to friend locations
- The "Unlimited Trades" feature is a UI indicator to help coordinate trading sessions
- Actual trade limits are controlled by Pokemon Go's servers
- Use the friend location feature to meet up with friends for trading
- **For Private & School Use Only**

## Disclaimer

This application is provided for educational and private use purposes only. The developers are not responsible for any consequences resulting from the use of this app, including but not limited to account bans, violations of terms of service, or any other issues. The unlimited trades feature is designed to help coordinate trading sessions with friends by providing location spoofing capabilities. Actual trade limits are enforced by Pokemon Go's servers and cannot be bypassed. Use responsibly and at your own risk. **For Private & School Use Only.**

## Building

To build a release APK:
```bash
./gradlew assembleRelease
```

To build a debug APK:
```bash
./gradlew assembleDebug
```

## License

This project is provided as-is for educational and testing purposes.

