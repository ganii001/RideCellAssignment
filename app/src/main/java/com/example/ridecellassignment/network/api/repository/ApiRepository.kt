package com.example.ridecellassignment.network.api.repository

import com.example.ridecellassignment.network.api.apihelper.ApiHelper
import com.example.ridecellassignment.network.responses.Person
import retrofit2.http.Body
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun authenticate(@Body person: Person) =
        apiHelper.authenticate(person)

    suspend fun signUp(@Body person: Person) = apiHelper.signUp(person)

    suspend fun resetPassword(@Body person: Person) = apiHelper.resetPassword(person)

    suspend fun getVehicles() = apiHelper.getVehicles()

    suspend fun getProfileData() = apiHelper.getProfileData()
}