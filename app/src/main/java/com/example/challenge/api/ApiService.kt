package com.example.challenge.api

import com.example.challenge.model.Employee
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getEmployees(): Response<List<Employee>>

    @POST("employees")
    suspend fun createEmployee(@Body employee: Employee): Employee

    @PUT("employees/{id}")
    suspend fun updateEmployee(@Path("id") id:Long, @Body employee: Employee)

    @DELETE("employees/{id}")
    suspend fun deleteEmployee(@Path("id") id:Long)
}