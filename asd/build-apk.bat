@echo off
echo Building Location Spoofer APK...
echo.

REM Check if gradlew exists
if not exist "gradlew.bat" (
    echo Error: gradlew.bat not found. Please run this from the project root directory.
    pause
    exit /b 1
)

echo Running Gradle build...
call gradlew.bat assembleRelease

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build successful!
    echo APK location: app\build\outputs\apk\release\app-release.apk
) else (
    echo.
    echo Build failed. Please check the error messages above.
)

pause

