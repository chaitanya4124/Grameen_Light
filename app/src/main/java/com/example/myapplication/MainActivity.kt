package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myapplication.ui.MainApp

/**
 * MainActivity serves as the entry point for the application and handles deep link intents.
 */
class MainActivity : ComponentActivity() {
    
    // State to hold the current deep link URI, allowing MainApp to react to changes.
    // Using mutableStateOf allows Compose to recompose MainApp when a new deep link arrives.
    private var deepLinkUri by mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Capture initial deep link if the activity is started via a deep link URL.
        deepLinkUri = intent?.data
        
        setContent {
            MainApp(
                deepLinkUri = deepLinkUri,
                onDeepLinkHandled = { deepLinkUri = null }
            )
        }
    }

    /**
     * Handles new intents when the activity is already running (e.g., launchMode="singleTop").
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Set the new intent and update the deepLinkUri state to trigger navigation in MainApp.
        setIntent(intent)
        deepLinkUri = intent.data
    }
}
