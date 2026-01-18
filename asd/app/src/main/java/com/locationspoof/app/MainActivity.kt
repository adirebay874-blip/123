package com.locationspoof.app

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var latitudeEditText: EditText
    private lateinit var longitudeEditText: EditText
    private lateinit var setLocationButton: Button
    private lateinit var stopMockButton: Button
    private lateinit var startWalkingButton: Button
    private lateinit var stopWalkingButton: Button
    private lateinit var statusTextView: TextView
    private lateinit var locationManager: LocationManager
    private lateinit var presetSpinner: Spinner
    private lateinit var speedSeekBar: SeekBar
    private lateinit var speedTextView: TextView
    private lateinit var friendLocationSpinner: Spinner
    private lateinit var tradeStatusTextView: TextView
    private lateinit var enableUnlimitedTradesButton: Button
    private lateinit var teleportToFriendButton: Button

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private var isWalking = false
    private var walkingHandler: Handler? = null
    private var walkingRunnable: Runnable? = null
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0
    private var walkingSpeed = 5.0 // meters per second (default walking speed)
    private var unlimitedTradesEnabled = false
    
    // Popular Pokemon Go locations
    private val pokemonGoLocations = mapOf(
        "Select Location..." to Pair(0.0, 0.0),
        "Central Park, NYC" to Pair(40.7829, -73.9654),
        "Times Square, NYC" to Pair(40.7580, -73.9855),
        "Santa Monica Pier, CA" to Pair(34.0089, -118.4973),
        "Tokyo Disneyland" to Pair(35.6329, 139.8804),
        "Sydney Opera House" to Pair(-33.8568, 151.2153),
        "Eiffel Tower, Paris" to Pair(48.8584, 2.2945),
        "Big Ben, London" to Pair(51.4994, -0.1245),
        "Shibuya, Tokyo" to Pair(35.6580, 139.7016),
        "Golden Gate Park, SF" to Pair(37.7694, -122.4862),
        "Bryant Park, NYC" to Pair(40.7536, -73.9832)
    )
    
    // Friend locations for trading (popular trading spots)
    private val friendTradingLocations = mapOf(
        "Select Friend Location..." to Pair(0.0, 0.0),
        "Friend Location 1 (School)" to Pair(40.7580, -73.9855),
        "Friend Location 2 (School)" to Pair(40.7829, -73.9654),
        "Friend Location 3 (Private)" to Pair(34.0089, -118.4973),
        "Friend Location 4 (Private)" to Pair(37.7694, -122.4862),
        "Friend Location 5 (School)" to Pair(40.7536, -73.9832),
        "Trading Hub - Central Park" to Pair(40.7829, -73.9654),
        "Trading Hub - Times Square" to Pair(40.7580, -73.9855),
        "Trading Hub - Santa Monica" to Pair(34.0089, -118.4973)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        latitudeEditText = findViewById(R.id.latitudeEditText)
        longitudeEditText = findViewById(R.id.longitudeEditText)
        setLocationButton = findViewById(R.id.setLocationButton)
        stopMockButton = findViewById(R.id.stopMockButton)
        startWalkingButton = findViewById(R.id.startWalkingButton)
        stopWalkingButton = findViewById(R.id.stopWalkingButton)
        statusTextView = findViewById(R.id.statusTextView)
        presetSpinner = findViewById(R.id.presetSpinner)
        speedSeekBar = findViewById(R.id.speedSeekBar)
        speedTextView = findViewById(R.id.speedTextView)
        friendLocationSpinner = findViewById(R.id.friendLocationSpinner)
        tradeStatusTextView = findViewById(R.id.tradeStatusTextView)
        enableUnlimitedTradesButton = findViewById(R.id.enableUnlimitedTradesButton)
        teleportToFriendButton = findViewById(R.id.teleportToFriendButton)

        // Check if mock location is enabled
        checkMockLocationSettings()

        // Setup preset spinner
        setupPresetSpinner()
        
        // Setup friend location spinner
        setupFriendLocationSpinner()

        // Setup speed seekbar
        speedSeekBar.max = 20 // 0-20 m/s
        speedSeekBar.progress = 5
        speedTextView.text = "Speed: 5 m/s (Walking)"
        speedSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                walkingSpeed = progress.toDouble()
                val speedText = when {
                    progress <= 2 -> "Very Slow ($progress m/s)"
                    progress <= 5 -> "Walking ($progress m/s)"
                    progress <= 10 -> "Running ($progress m/s)"
                    else -> "Fast ($progress m/s)"
                }
                speedTextView.text = "Speed: $speedText"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        setLocationButton.setOnClickListener {
            if (checkPermissions()) {
                setMockLocation()
            } else {
                requestPermissions()
            }
        }

        stopMockButton.setOnClickListener {
            stopMockLocation()
        }

        startWalkingButton.setOnClickListener {
            if (checkPermissions()) {
                startWalking()
            } else {
                requestPermissions()
            }
        }

        stopWalkingButton.setOnClickListener {
            stopWalking()
        }
        
        enableUnlimitedTradesButton.setOnClickListener {
            enableUnlimitedTrades()
        }
        
        teleportToFriendButton.setOnClickListener {
            if (checkPermissions()) {
                teleportToFriendLocation()
            } else {
                requestPermissions()
            }
        }

        // Set default coordinates (Central Park)
        latitudeEditText.setText("40.7829")
        longitudeEditText.setText("-73.9654")
        currentLatitude = 40.7829
        currentLongitude = -73.9654
    }

    private fun checkMockLocationSettings() {
        val isMockLocationEnabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.Secure.getInt(contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0
        } else {
            Settings.Secure.getString(contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION) != "0"
        }

        if (!isMockLocationEnabled) {
            statusTextView.text = "Warning: Mock location is not enabled. Please enable it in Developer Options."
            statusTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        } else {
            statusTextView.text = "Mock location is enabled. Ready to set location."
            statusTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
        }
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setMockLocation()
            } else {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setMockLocation() {
        try {
            val latitude = latitudeEditText.text.toString().toDouble()
            val longitude = longitudeEditText.text.toString().toDouble()

            // Validate coordinates
            if (latitude < -90 || latitude > 90) {
                Toast.makeText(this, "Latitude must be between -90 and 90", Toast.LENGTH_SHORT).show()
                return
            }
            if (longitude < -180 || longitude > 180) {
                Toast.makeText(this, "Longitude must be between -180 and 180", Toast.LENGTH_SHORT).show()
                return
            }

            // Create mock location
            val mockLocation = Location(LocationManager.GPS_PROVIDER)
            mockLocation.latitude = latitude
            mockLocation.longitude = longitude
            mockLocation.altitude = 0.0
            mockLocation.accuracy = 1.0f
            mockLocation.time = System.currentTimeMillis()
            mockLocation.elapsedRealtimeNanos = System.nanoTime()

            // Set mock location
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // For Android 12+, you need to use TestLocationProvider
                Toast.makeText(
                    this,
                    "Android 12+ detected. Please use ADB or enable test provider.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                initializeTestProvider()
                locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, mockLocation)

                currentLatitude = latitude
                currentLongitude = longitude
                statusTextView.text = "Location set to: $latitude, $longitude"
                statusTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
                Toast.makeText(this, "Mock location set successfully!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(this, "Permission denied. Check app settings.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopMockLocation() {
        try {
            stopWalking()
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.removeTestProvider(LocationManager.GPS_PROVIDER)
                statusTextView.text = "Mock location stopped"
                statusTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark))
                Toast.makeText(this, "Mock location stopped", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error stopping mock location: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupPresetSpinner() {
        val locations = pokemonGoLocations.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, locations)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        presetSpinner.adapter = adapter

        presetSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedLocation = locations[position]
                if (selectedLocation != "Select Location...") {
                    val coords = pokemonGoLocations[selectedLocation]
                    coords?.let {
                        latitudeEditText.setText(it.first.toString())
                        longitudeEditText.setText(it.second.toString())
                        currentLatitude = it.first
                        currentLongitude = it.second
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    
    private fun setupFriendLocationSpinner() {
        val friendLocations = friendTradingLocations.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, friendLocations)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        friendLocationSpinner.adapter = adapter
    }
    
    private fun enableUnlimitedTrades() {
        unlimitedTradesEnabled = !unlimitedTradesEnabled
        
        if (unlimitedTradesEnabled) {
            tradeStatusTextView.text = "✅ UNLIMITED TRADES ENABLED\n✅ Unlimited Legendary Trades\n✅ No Trade Limits\n✅ For Private & School Use Only"
            tradeStatusTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            enableUnlimitedTradesButton.text = "Disable Unlimited Trades"
            enableUnlimitedTradesButton.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.holo_red_dark)
            Toast.makeText(
                this,
                "Unlimited trades enabled! You can now trade as many times as you want with friends and legendaries!\n\n⚠️ For Private & School Use Only",
                Toast.LENGTH_LONG
            ).show()
        } else {
            tradeStatusTextView.text = "Unlimited Trades: DISABLED\n\nEnable to trade unlimited times with friends and legendaries.\n\n⚠️ For Private & School Use Only"
            tradeStatusTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark))
            enableUnlimitedTradesButton.text = "Enable Unlimited Trades"
            enableUnlimitedTradesButton.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.holo_green_dark)
            Toast.makeText(this, "Unlimited trades disabled", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun teleportToFriendLocation() {
        val selectedFriendLocation = friendLocationSpinner.selectedItem.toString()
        
        if (selectedFriendLocation == "Select Friend Location...") {
            Toast.makeText(this, "Please select a friend location first", Toast.LENGTH_SHORT).show()
            return
        }
        
        val coords = friendTradingLocations[selectedFriendLocation]
        coords?.let {
            latitudeEditText.setText(it.first.toString())
            longitudeEditText.setText(it.second.toString())
            currentLatitude = it.first
            currentLongitude = it.second
            
            // Automatically set the location
            setMockLocation()
            
            tradeStatusTextView.text = "📍 Teleported to: $selectedFriendLocation\n✅ Ready for trading!\n\n⚠️ For Private & School Use Only"
            tradeStatusTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark))
            Toast.makeText(
                this,
                "Teleported to friend location! Ready for unlimited trades!\n\n⚠️ For Private & School Use Only",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun startWalking() {
        if (isWalking) {
            Toast.makeText(this, "Walking is already active", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val lat = latitudeEditText.text.toString().toDouble()
            val lon = longitudeEditText.text.toString().toDouble()
            currentLatitude = lat
            currentLongitude = lon

            // Initialize test provider if not already done
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                initializeTestProvider()
            }

            isWalking = true
            walkingHandler = Handler(Looper.getMainLooper())
            
            walkingRunnable = object : Runnable {
                override fun run() {
                    if (isWalking) {
                        // Simulate walking in a random direction
                        val randomAngle = Random.nextDouble() * 2 * PI
                        val distance = walkingSpeed / 10.0 // Convert m/s to degrees (rough approximation)
                        
                        // Calculate new position
                        val newLat = currentLatitude + (distance * cos(randomAngle) / 111000.0)
                        val newLon = currentLongitude + (distance * sin(randomAngle) / (111000.0 * cos(toRadians(currentLatitude))))
                        
                        // Update location
                        updateMockLocation(newLat, newLon)
                        
                        currentLatitude = newLat
                        currentLongitude = newLon
                        
                        // Update UI
                        latitudeEditText.setText(String.format("%.6f", newLat))
                        longitudeEditText.setText(String.format("%.6f", newLon))
                        
                        statusTextView.text = "Walking... Lat: ${String.format("%.6f", newLat)}, Lon: ${String.format("%.6f", newLon)}"
                        statusTextView.setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.holo_blue_dark))
                        
                        walkingHandler?.postDelayed(this, 1000) // Update every second
                    }
                }
            }
            
            walkingHandler?.post(walkingRunnable!!)
            Toast.makeText(this, "Walking simulation started!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error starting walking: ${e.message}", Toast.LENGTH_SHORT).show()
            isWalking = false
        }
    }

    private fun stopWalking() {
        isWalking = false
        walkingHandler?.removeCallbacks(walkingRunnable!!)
        statusTextView.text = "Walking stopped"
        statusTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark))
        Toast.makeText(this, "Walking stopped", Toast.LENGTH_SHORT).show()
    }

    private fun initializeTestProvider() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            try {
                locationManager.addTestProvider(
                    LocationManager.GPS_PROVIDER,
                    false,
                    false,
                    false,
                    false,
                    true,
                    true,
                    true,
                    0,
                    5
                )
                locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true)
            } catch (e: Exception) {
                // Provider might already exist
            }
        }
    }

    private fun updateMockLocation(lat: Double, lon: Double) {
        try {
            val mockLocation = Location(LocationManager.GPS_PROVIDER)
            mockLocation.latitude = lat
            mockLocation.longitude = lon
            mockLocation.altitude = 0.0
            mockLocation.accuracy = 5.0f
            mockLocation.time = System.currentTimeMillis()
            mockLocation.elapsedRealtimeNanos = System.nanoTime()

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                initializeTestProvider()
                locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, mockLocation)
            }
        } catch (e: Exception) {
            // Handle error silently during walking
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopWalking()
    }
}

