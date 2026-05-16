package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poles")
data class PoleEntity(
    @PrimaryKey
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val areaName: String,
    val status: String, // Working, Fused, Burning in Day
    val lastUpdated: Long
)
