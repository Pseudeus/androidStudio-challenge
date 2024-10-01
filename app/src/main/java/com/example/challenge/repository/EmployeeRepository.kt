package com.example.challenge.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.challenge.api.RetrofitInstance
import com.example.challenge.database.ApiService
import com.example.challenge.database.EmployeeDatabase
import com.example.challenge.model.Employee
import com.example.notesroompractice.databinding.EmployeeLayoutBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

            var greatestId = db.getEmployeeDao().getEmployeesList()[0].id!!
            val em = employee.copy(id = ++greatestId)

            db.getEmployeeDao().insertEmployee(em)
            println(greatestId)
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
            db.getEmployeeDao().deleteEmployee(employee)
        }
    }
    suspend fun updateEmployee(employee: Employee) {
        try {
            val response = api.updateEmployee(employee.id!!, employee)

            if (response.isSuccessful) {
                val empl = response.body() ?: employee
                db.getEmployeeDao().updateEmployee(empl)
            } else {
                db.getEmployeeDao().updateEmployee(employee)
            }
        } catch (e: Exception) {
            Log.e("EmployeeRepository", "Update Error: ${e.message}")
            db.getEmployeeDao().updateEmployee(employee)
        }
    }

    suspend fun fetchEmployees() {
        try {
            val response = api.getEmployees()
            val localResponse = withContext(Dispatchers.IO) {
                db.getEmployeeDao().getEmployeesList()
            }

            if (response.isSuccessful) {
                val apiEmployees = response.body() ?: emptyList()

                // Delete values in the api, giving priority to the local storage
                // Not safe for multiple users with a single api
                for (employee in apiEmployees) {
                    if (localResponse.none { it.id == employee.id }) {
                        api.deleteEmployee(employee.id!!)
                    }
                }

                // Sync local updated employees to the api
                // !!This is quadratic complexity but is fast made
                for (localEmployee in localResponse) {
                    val apiEmployee = apiEmployees.find { it.id == localEmployee.id }

                    if (apiEmployee != null) {
                        if (apiEmployee != localEmployee) {
                            api.updateEmployee(localEmployee.id!!, localEmployee)
                        }
                    } else {
                        api.insertEmployee(localEmployee)
                    }
                }

            } else {
                Log.e("EmployeeRepository", "Fetch Error: ${response.message()}")
            }
            } catch (e: Exception) {
                Log.e("EmployeeRepository", "Fetch Error: ${e.message}")
            }
    }

    fun getAllEmployees() = db.getEmployeeDao().getAllEmployees()

    suspend fun getEmployeesList() = db.getEmployeeDao().getEmployeesList()
    fun searchEmployee(query: String?) = db.getEmployeeDao().searchEmployee(query)
}

