package com.example.challenge

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.challenge.database.EmployeeDatabase
import com.example.challenge.location.LocationCallback
import com.example.challenge.network.NetworkMonitor
import com.example.challenge.repository.EmployeeRepository
import com.example.challenge.viewmodel.EmployeeViewModel
import com.example.challenge.viewmodel.EmployeeViewModelFactory
import com.example.notesroompractice.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {
    lateinit var employeeViewModel: EmployeeViewModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        networkMonitor = NetworkMonitor(this) {
            // Called when network is available
            onNetworkReestablished()
        }

        setupViewModel()

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                } else -> {
                // No location access granted.
            }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        networkMonitor.startMonitoring()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkMonitor.stopMonitoring()
    }

    private fun onNetworkReestablished() {
        employeeViewModel.fetchEmployees()
        Toast.makeText(this, "Back online", Toast.LENGTH_SHORT).show()
    }


    private fun setupViewModel() {
        val employeeRepository = EmployeeRepository(EmployeeDatabase(this))
        val viewModelProviderFactory = EmployeeViewModelFactory(application, employeeRepository)
        employeeViewModel = ViewModelProvider(this, viewModelProviderFactory)[EmployeeViewModel::class.java]
    }

    fun getLastLocation(callback: LocationCallback) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            callback.onLocationFailed("Location permissions are not granted.")
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener(this) { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // Use the latitude and longitude as needed
                    callback.onLocationReceived(latitude, longitude)

                } else {
                    callback.onLocationFailed("Location is null.")
                }
            }
            .addOnFailureListener { e ->
                // Handle failure
                callback.onLocationFailed("Failded to get location: ${e.message}")
            }
    }
}


