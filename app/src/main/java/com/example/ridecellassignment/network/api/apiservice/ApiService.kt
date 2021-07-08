package com.example.ridecellassignment.network.api.apiservice

import com.example.ridecellassignment.network.responses.LoginResponse
import com.example.ridecellassignment.network.responses.Person
import com.example.ridecellassignment.network.responses.VehicleData

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    @POST("/api/v2/people/authenticate")
    suspend fun authenticate(
        @Body person: Person
    ): Response<LoginResponse>

    @POST("/api/v2/people/create")
    suspend fun signUp(
        @Body person: Person
    ): Response<LoginResponse>

    @GET("/api/v2/vehicles")
    suspend fun getVehicles(): Response<List<VehicleData>>

    @GET("/api/v2/people/me")
    suspend fun getProfileData(): Response<Person>

    @POST("/api/v2/people/reset_password")
    suspend fun resetPassword(
        @Body person: Person
    ): Response<LoginResponse>

}