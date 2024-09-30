package com.example.challenge.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.challenge.model.Employee

@Database(entities = [Employee::class], version = 1)
abstract class EmployeeDatabase: RoomDatabase() {

    abstract fun getEmployeeDao(): EmployeeDao

    companion object {
        @Volatile
        private var instance: EmployeeDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK) {
            instance ?:
            createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                EmployeeDatabase::class.java,
                "employee_db"
            ).build()
    }
}