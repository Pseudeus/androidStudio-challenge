package com.example.challenge.database

import com.example.challenge.model.Employee
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("employees")
    suspend fun getEmployees(): Response<List<Employee>>

    @POST("employees")
    suspend fun insertEmployee(@Body employee: Employee): Response<Employee>

    @Headers("Content-Type: application/json")
    @PUT("employees/{id}")
    suspend fun updateEmployee(@Path("id") id:Long, @Body employee: Employee): Response<Employee>

    @Headers("Content-Type: application/json")
    @DELETE("employees/{id}")
    suspend fun deleteEmployee(@Path("id") id:Long): Response<Unit>
}