package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audit_reports")
data class AuditReportEntity(
    @PrimaryKey
    val id: String,
    val poleId: String,
    val status: String,
    val timestamp: Long,
    val complaintId: String? = null
)
