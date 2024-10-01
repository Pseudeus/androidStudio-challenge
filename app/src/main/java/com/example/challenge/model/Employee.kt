package com.example.challenge.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "employees")
@Parcelize
data class Employee(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val fullName: String,
    val dateOfBirth: String,
    val avatar: String,
    val latitude: Double,
    val longitude: Double,
    val utcDate: String,
    val createdAt: String
): Parcelable
