package com.example.ridecellassignment.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ridecellassignment.Utils
import com.example.ridecellassignment.network.api.repository.ApiRepository
import com.example.ridecellassignment.network.responses.Person
import com.example.ridecellassignment.network.responses.VehicleData
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import kotlin.time.ExperimentalTime

class DashboardViewModel @ViewModelInject constructor(
    @ApplicationContext
    val context: Context,
    private val apiRepository: ApiRepository,
) : ViewModel() {

    var isSuccessful: MutableLiveData<Boolean> = MutableLiveData()
    val vehicleList = MutableLiveData<List<VehicleData>>()
    var profileName = ObservableField<String>()
    var profileEmail = ObservableField<String>()
    var accDays = ObservableField<String>()

    fun getVehicles(
    ) {
        viewModelScope.launch {
            apiRepository.getVehicles().let {
                try {
                    if (it.isSuccessful && it.body() != null) {
                        isSuccessful.value = true
                        vehicleList.value = it.body()
                    } else {
                        isSuccessful.value = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    isSuccessful.value = false
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @ExperimentalTime
    fun getProfileData(
    ) {
        viewModelScope.launch {
            apiRepository.getProfileData().let {
                try {
                    if (it.isSuccessful && it.body() != null) {
                        isSuccessful.value = true
                        profileName.set(it.body()!!.displayName)
                        profileEmail.set(it.body()!!.email)

                        val createdDate = it.body()!!.created_at?.split("T")?.get(0)
                        val updatedDate = it.body()!!.updated_at?.split("T")?.get(0)
                        val days = Utils.getDateDiff(
                            SimpleDateFormat("yyyy-MM-dd"),
                            createdDate!!,
                            updatedDate!!
                        )
                        accDays.set(days.toString())
                    } else {
                        isSuccessful.value = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    isSuccessful.value = false
                }
            }
        }
    }
}