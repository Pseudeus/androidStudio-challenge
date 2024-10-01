package com.example.challenge.location

interface LocationCallback {
    fun onLocationReceived(latitude: Double, longitude: Double)
    fun onLocationFailed(errorMessage: String)
}