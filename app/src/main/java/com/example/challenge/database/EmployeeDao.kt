package com.example.challenge.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.challenge.model.Employee

@Dao
interface EmployeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: Employee)

    @Update
    suspend fun updateEmployee(employee: Employee)

    @Delete
    suspend fun deleteEmployee(employee: Employee)

    @Query("SELECT * FROM EMPLOYEES ORDER BY ID DESC")
    fun getAllEmployees(): LiveData<List<Employee>>

    @Query("SELECT * FROM EMPLOYEES WHERE fullName LIKE :query OR avatar LIKE :query ")
    fun searchEmployee(query: String?): LiveData<List<Employee>>
}