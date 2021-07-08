package com.example.ridecellassignment.ui.dashboard.LocationLiveData

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData


class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData =
        LocationLiveData(application)

    val getLocationData: LiveData<Location> = locationData

    fun stopFetchingLocation() {
        locationData.stopLocationUpdates()
    }
}