package com.example.ridecellassignment.ui.dashboard

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.ridecellassignment.R
import com.example.ridecellassignment.Utils.MY_PERMISSIONS_REQUEST_LOCATION
import com.example.ridecellassignment.Utils.showMessageOKDialog
import com.example.ridecellassignment.databinding.ActivityDashboardBinding
import com.example.ridecellassignment.ui.dashboard.LocationLiveData.LocationUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private var isGPSEnabled = false
    private val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController =
            Navigation.findNavController(this, R.id.nav_host_fragment_activity_dashboard)
        val appBarConfiguration =
            AppBarConfiguration.Builder(R.id.navigation_maps, R.id.navigation_profile).build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navView, navController)



        if (!isGPSEnabled) {
            turnOnGPS()
        } else {
            startLocationUpdates()
        }

    }

    private fun turnOnGPS() {
        LocationUtil(this).turnGPSOn(object :
            LocationUtil.OnLocationOnListener {

            override fun locationStatus(isLocationOn: Boolean) {
                isGPSEnabled = isLocationOn
            }
        })

        if (isGPSEnabled) {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        if (!isLocationPermissionsGranted(
                this,
                locationPermissions[0],
                locationPermissions[1]
            )
        ) {
            checkLocationPermission(this)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("onActivityResult()", resultCode.toString())
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION ->
                when (resultCode) {
                    RESULT_OK -> {
                        isGPSEnabled = true
                        startLocationUpdates()
                    }
                    RESULT_CANCELED -> {
                        showMessageOKDialog(
                            this,
                            "You need to enable device's GPS for fetching your location address."
                        ) { p0, p1 ->
                            p0?.dismiss()
                            turnOnGPS()
                        }
                    }
                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        CoroutineScope(Dispatchers.Main).launch {
                            startLocationUpdates()
                        }
                    }
                } else {
                    showMessageOKDialog(
                        this,
                        "These permission is mandatory for the location functionality. Please allow access."
                    ) { p0, p1 ->
                        if (ContextCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                        } else {
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                MY_PERMISSIONS_REQUEST_LOCATION
                            )
                        }
                    }
                }
                return
            }
        }
    }


    fun isLocationPermissionsGranted(context: Context, l1: String, l2: String): Boolean {
        return (isGranted(context, l1)
                && isGranted(context, l2))
    }

    private fun isGranted(
        context: Context,
        permission: String
    ) = ActivityCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED


    fun checkLocationPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity, Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }
}