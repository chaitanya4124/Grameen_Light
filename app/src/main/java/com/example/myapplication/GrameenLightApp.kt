package com.example.myapplication

import android.app.Application
import com.google.firebase.FirebaseApp

class GrameenLightApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Explicitly initialize Firebase to prevent "not initialized" errors
        FirebaseApp.initializeApp(this)
    }
}
