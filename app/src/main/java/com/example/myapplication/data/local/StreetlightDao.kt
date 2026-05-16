package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StreetlightDao {
    @Query("SELECT * FROM poles")
    fun getAllPoles(): Flow<List<PoleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoles(poles: List<PoleEntity>)

    @Query("SELECT * FROM poles WHERE id = :poleId")
    suspend fun getPoleById(poleId: String): PoleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditReport(report: AuditReportEntity)

    @Query("SELECT * FROM audit_reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<AuditReportEntity>>

    @Query("SELECT COUNT(*) FROM poles")
    suspend fun getPoleCount(): Int
}
