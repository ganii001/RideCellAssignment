package com.example.ridecellassignment.network.api.apihelper

import com.example.ridecellassignment.network.responses.LoginResponse
import com.example.ridecellassignment.network.responses.Person
import com.example.ridecellassignment.network.responses.VehicleData
import retrofit2.Response
import retrofit2.http.Body

interface ApiHelper {

    suspend fun authenticate(@Body person: Person): Response<LoginResponse>

    suspend fun signUp(@Body person: Person): Response<LoginResponse>

    suspend fun resetPassword(@Body person: Person): Response<LoginResponse>

    suspend fun getVehicles(): Response<List<VehicleData>>

    suspend fun getProfileData(): Response<Person>


}