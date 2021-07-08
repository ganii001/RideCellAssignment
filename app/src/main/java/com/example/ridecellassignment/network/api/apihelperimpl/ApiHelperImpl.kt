package com.example.ridecellassignment.network.api.apihelperimpl

import com.example.ridecellassignment.network.api.apihelper.ApiHelper
import com.example.ridecellassignment.network.api.apiservice.ApiService
import com.example.ridecellassignment.network.responses.LoginResponse
import com.example.ridecellassignment.network.responses.Person
import com.example.ridecellassignment.network.responses.VehicleData
import retrofit2.Response
import retrofit2.http.Body
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) :
    ApiHelper {

    override suspend fun authenticate(@Body person: Person): Response<LoginResponse> =
        apiService.authenticate(person)


    override suspend fun signUp(@Body person: Person): Response<LoginResponse> =
        apiService.signUp(person)

    override suspend fun resetPassword(@Body person: Person): Response<LoginResponse> =
        apiService.resetPassword(person)

    override suspend fun getVehicles(): Response<List<VehicleData>> = apiService.getVehicles()

    override suspend fun getProfileData(): Response<Person> = apiService.getProfileData()

}