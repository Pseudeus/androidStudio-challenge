package com.example.challenge.api

import com.example.challenge.model.Employee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitQueries {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://636a9855c07d8f936da2ad92.mockapi.io/api/v1/employees")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun queryAllEmployees() {
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<List<Employee>> = getRetrofit().create(ApiService::class.java).getEmployees()
            val employees = call.body()

            if (call.isSuccessful) {

            } else {
                //Show error
            }
        }
    }
}