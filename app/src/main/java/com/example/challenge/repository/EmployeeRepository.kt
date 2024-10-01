package com.example.challenge.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.challenge.api.RetrofitInstance
import com.example.challenge.database.ApiService
import com.example.challenge.database.EmployeeDatabase
import com.example.challenge.model.Employee
import retrofit2.Response
import retrofit2.Retrofit
class EmployeeRepository(private val db: EmployeeDatabase) {
    private var api: ApiService = RetrofitInstance.api


    suspend fun insertEmployee(employee: Employee) {
        try {
            val response = api.insertEmployee(employee)

            if (response.isSuccessful) {
                val empl = response.body()
                db.getEmployeeDao().insertEmployee(employee.copy(id = empl?.id))
            }

        } catch (e: Exception) {
            Log.e("EmployeeRepository", "Insert Error: ${e.message}")
        }
    }
    suspend fun deleteEmployee(employee: Employee) {
        try {
            val response = api.deleteEmployee(employee.id!!)

            if (response.isSuccessful) {
                db.getEmployeeDao().deleteEmployee(employee)
            }
        } catch (e: Exception) {
            Log.e("EmployeeRepository", "Delete Error: ${e.message}")
        }
    }
    suspend fun updateEmployee(employee: Employee) {
        try {
            val response = api.updateEmployee(employee.id!!, employee)

            if (response.isSuccessful) {
                val empl = response.body() ?: employee
                db.getEmployeeDao().updateEmployee(empl)
            } else {
                db.getEmployeeDao().updateEmployee(employee) //Todo: mark as unsync element
            }
        } catch (e: Exception) {
            Log.e("EmployeeRepository", "Update Error: ${e.message}")
        }
    }

    fun getAllEmployees() = db.getEmployeeDao().getAllEmployees()
    //: Response<List<Employee>> {
    //
    //    return api.getEmployees()
    //}
    fun searchEmployee(query: String?) = db.getEmployeeDao().searchEmployee(query)
}

