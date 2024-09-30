package com.example.challenge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge.model.Employee
import com.example.challenge.repository.EmployeeRepository
import kotlinx.coroutines.launch

class EmployeeViewModel(app: Application, private val employeeRepository: EmployeeRepository): AndroidViewModel(app) {
    fun addEmployee(employee: Employee) =
        viewModelScope.launch {
            employeeRepository.insertEmployee(employee)
        }
    fun updateEmployee(employee: Employee) =
        viewModelScope.launch {
            employeeRepository.updateEmployee(employee)
        }

    fun deleteEmployee(employee: Employee) =
        viewModelScope.launch {
            employeeRepository.deleteEmployee(employee)
        }

    fun getAllEmployees() = employeeRepository.getAllEmployees()

    fun searchEmployee(query: String?) =
        employeeRepository.searchEmployee(query)
}