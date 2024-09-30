package com.example.challenge.repository

import com.example.challenge.database.EmployeeDatabase
import com.example.challenge.model.Employee

class EmployeeRepository(private val db: EmployeeDatabase) {
    suspend fun insertEmployee(employee: Employee) = db.getEmployeeDao().insertEmployee(employee)
    suspend fun deleteEmployee(employee: Employee) = db.getEmployeeDao().deleteEmployee(employee)
    suspend fun updateEmployee(employee: Employee) = db.getEmployeeDao().updateEmployee(employee)

    fun getAllEmployees() = db.getEmployeeDao().getAllEmployees()
    fun searchEmployee(query: String?) = db.getEmployeeDao().searchEmployee(query)
}