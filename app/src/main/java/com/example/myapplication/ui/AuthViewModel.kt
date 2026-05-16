package com.example.myapplication.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

enum class UserRole {
    NONE, USER, ADMIN
}

class AuthViewModel : ViewModel() {
    var currentUserRole by mutableStateOf(UserRole.NONE)
        private set

    var currentLanguage by mutableStateOf("en")
        private set

    private val auth: FirebaseAuth? by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    fun login(role: UserRole) {
        // Simulate login
        currentUserRole = role
    }

    fun logout() {
        auth?.signOut()
        currentUserRole = UserRole.NONE
    }

    fun toggleLanguage() {
        currentLanguage = if (currentLanguage == "en") "kn" else "en"
    }
}
