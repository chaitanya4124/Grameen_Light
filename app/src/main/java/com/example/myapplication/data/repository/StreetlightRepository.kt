package com.example.myapplication.data.repository

import com.example.myapplication.data.local.AuditReportEntity
import com.example.myapplication.data.local.PoleEntity
import com.example.myapplication.data.local.StreetlightDao
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.await

class StreetlightRepository(
    private val streetlightDao: StreetlightDao
) {
    private val firebaseDatabase: FirebaseDatabase? by lazy {
        try {
            FirebaseDatabase.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    private val polesRef by lazy { firebaseDatabase?.getReference("poles") }
    private val reportsRef by lazy { firebaseDatabase?.getReference("reports") }

    fun getPoles(): Flow<List<PoleEntity>> = streetlightDao.getAllPoles()

    suspend fun syncPoles() {
        try {
            val ref = polesRef
            if (ref != null) {
                val snapshot = ref.get().await()
                val poles = mutableListOf<PoleEntity>()
                snapshot.children.forEach { child ->
                    val id = child.key ?: return@forEach
                    val lat = child.child("latitude").getValue(Double::class.java) ?: 0.0
                    val lng = child.child("longitude").getValue(Double::class.java) ?: 0.0
                    val area = child.child("areaName").getValue(String::class.java) ?: "village_area"
                    val status = child.child("status").getValue(String::class.java) ?: "Working"
                    val lastUpdated = child.child("lastUpdated").getValue(Long::class.java) ?: 0L
                    poles.add(PoleEntity(id, lat, lng, area, status, lastUpdated))
                }
                if (poles.isNotEmpty()) {
                      streetlightDao.insertPoles(poles)
                     return
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        // Simulation Fallback: Always ensure at least 15 poles are in Room
        if (streetlightDao.getPoleCount() < 5) {
            val areas = listOf("temple_road", "market_square", "bus_stand", "primary_school", "main_street", "west_gate")
            val statuses = listOf("Working", "Working", "Working", "Fused", "Burning in Day")
            val defaults = (1..15).map { i ->
                PoleEntity(
                    id = "P$i",
                    latitude = 12.9716 + (i * 0.001),
                    longitude = 77.5946 + (i * 0.001),
                    areaName = areas.random(),
                    status = statuses.random(),
                    lastUpdated = System.currentTimeMillis()
                )
            }
            streetlightDao.insertPoles(defaults)
        }
    }

    suspend fun submitReport(report: AuditReportEntity) {
        // 1. Save locally
        streetlightDao.insertAuditReport(report)
        
        // 2. Update local pole status for immediate UI feedback
        val currentPole = streetlightDao.getPoleById(report.poleId)
        if (currentPole != null) {
            streetlightDao.insertPoles(listOf(currentPole.copy(status = report.status, lastUpdated = report.timestamp)))
        }
        
        // 3. Sync to Firebase if available
        try {
            val rRef = reportsRef
            val pRef = polesRef
            if (rRef != null && pRef != null) {
                val reportMap = mapOf(
                    "poleId" to report.poleId,
                    "status" to report.status,
                    "timestamp" to report.timestamp,
                    "complaintId" to report.complaintId
                )
                rRef.child(report.id).setValue(reportMap).await()
                pRef.child(report.poleId).child("status").setValue(report.status).await()
                pRef.child(report.poleId).child("lastUpdated").setValue(report.timestamp).await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getReports(): Flow<List<AuditReportEntity>> = streetlightDao.getAllReports()
    
    fun observePolesFromFirebase(): Flow<List<PoleEntity>> {
        val ref = polesRef ?: return emptyFlow()
        return callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val poles = mutableListOf<PoleEntity>()
                    snapshot.children.forEach { child ->
                        val id = child.key ?: return@forEach
                        val lat = child.child("latitude").getValue(Double::class.java) ?: 0.0
                        val lng = child.child("longitude").getValue(Double::class.java) ?: 0.0
                        val area = child.child("areaName").getValue(String::class.java) ?: "village_area"
                        val status = child.child("status").getValue(String::class.java) ?: "Working"
                        val lastUpdated = child.child("lastUpdated").getValue(Long::class.java) ?: 0L
                        poles.add(PoleEntity(id, lat, lng, area, status, lastUpdated))
                    }
                    trySend(poles)
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }
            ref.addValueEventListener(listener)
            awaitClose { ref.removeEventListener(listener) }
        }
    }
}
