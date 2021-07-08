package com.example.ridecellassignment.ui.dashboard.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.ridecellassignment.R
import com.example.ridecellassignment.Utils
import com.example.ridecellassignment.databinding.FragmentMapsBinding
import com.example.ridecellassignment.network.responses.VehicleData
import com.example.ridecellassignment.ui.dashboard.DashboardViewModel
import com.example.ridecellassignment.ui.dashboard.LocationLiveData.LocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private val locationViewModel: LocationViewModel by viewModels()
    val viewModel: DashboardViewModel by viewModels()
    lateinit var pDialog: Dialog

    private val callback = OnMapReadyCallback { googleMap ->
        observeLocationUpdates(googleMap)
        observeVehicles(googleMap)
    }

    private fun observeLocationUpdates(googleMap: GoogleMap) {
        locationViewModel.getLocationData.observe(this, Observer {
            if (it.latitude != 0.0 && it.longitude != 0.0) {

                val currentLocation = LatLng(it.latitude, it.longitude)
                val markerOptions1 = MarkerOptions()
                markerOptions1.position(currentLocation)
                val icon: BitmapDescriptor? = Utils.vectorToBitmap(resources, R.drawable.ic_pin)
                markerOptions1.icon(icon)

                googleMap.addMarker(markerOptions1.title("Your Location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))

                googleMap.isMyLocationEnabled = true

                val cameraPosition = CameraPosition.Builder()
                    .target(currentLocation) // Sets the center of the map to Mountain View
                    .zoom(10f) // Sets the zoom
                    .bearing(90f) // Sets the orientation of the camera to east
                    .tilt(12f) // Sets the tilt of the camera to 30 degrees
                    .build() // Creates a CameraPosition from the builder

                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                locationViewModel.stopFetchingLocation()
            } else {
                observeLocationUpdates(googleMap)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        pDialog = Utils.generateProgressDialog(context)!!
        val locationButton =
            (mapFragment?.view?.findViewById<View>("1".toInt())?.parent as View).findViewById<View>(
                "2".toInt()
            )
        val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 30, 30)


        getVehicles()
    }

    private fun observeVehicles(googleMap: GoogleMap) {
        viewModel.vehicleList.observe(requireActivity(), Observer { list ->
            for (data: VehicleData in list) {
                if (data.lat != null && data.lng != null) {
                    val vehicleLocation = LatLng(data.lat, data.lng)
                    val markerOptions1 = MarkerOptions()
                    markerOptions1.position(vehicleLocation)
                    val icon: BitmapDescriptor? =
                        Utils.vectorToBitmap(resources, R.drawable.ic_parkingmarker1)
                    markerOptions1.icon(icon)

                    googleMap.addMarker(markerOptions1.title("License Plate Number : " + data.licensePlateNumber))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(vehicleLocation))
                }
            }

            pDialog.dismiss()
        })
    }

    private fun getVehicles() {
        if (Utils.isDataConnected(context)) {
            viewModel.getVehicles()
            pDialog.show()
        } else {
            Utils.showSNACK_BAR_NO_INTERNET(requireActivity(), activity?.localClassName!!)
        }
    }
}