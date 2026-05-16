package com.example.myapplication.ui

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.StreetlightRepository
import com.example.myapplication.ui.navigation.Destination
import com.example.myapplication.ui.navigation.topLevelDestinations
import com.example.myapplication.ui.screens.*
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.*

/**
 * MainApp is the root composable that manages navigation, authentication state, and deep linking.
 * 
 * @param deepLinkUri The URI captured from an incoming intent, used for deep link routing.
 * @param onDeepLinkHandled Callback invoked after a deep link has been processed (regardless of validity).
 */
@Composable
fun MainApp(
    deepLinkUri: Uri? = null,
    onDeepLinkHandled: () -> Unit = {}
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { StreetlightRepository(database.streetlightDao()) }
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(repository))
    val authViewModel: AuthViewModel = viewModel()

    // Handle Localization
    val locale = Locale(authViewModel.currentLanguage)
    val config = context.resources.configuration
    config.setLocale(locale)
    val localizedContext = context.createConfigurationContext(config)
    
    CompositionLocalProvider(androidx.compose.ui.platform.LocalContext provides localizedContext) {
        MyApplicationTheme {
            // Navigation stack state
            val backStack = remember { mutableStateListOf<Any>(Destination.Login) }
            
            // Consolidated Logic for Auth and Deep Linking
            LaunchedEffect(authViewModel.currentUserRole, deepLinkUri) {
                val role = authViewModel.currentUserRole
                
                if (role == UserRole.NONE) {
                    // Not authenticated: ensure we are on the Login screen
                    if (!backStack.contains(Destination.Login)) {
                        backStack.clear()
                        backStack.add(Destination.Login)
                    }
                } else {
                    // User is authenticated
                    if (deepLinkUri != null) {
                        handleIncomingDeepLink(
                            uri = deepLinkUri,
                            backStack = backStack,
                            onHandled = onDeepLinkHandled
                        )
                    } else if (backStack.contains(Destination.Login)) {
                        // Redirect to home screen after login if no deep link is pending
                        backStack.clear()
                        backStack.add(Destination.Map)
                    }
                }
            }

            // UI Rendering based on Auth state
            if (authViewModel.currentUserRole == UserRole.NONE) {
                AuthNavigation(backStack, authViewModel)
            } else {
                MainNavigation(backStack, viewModel, authViewModel)
            }
        }
    }
}

/**
 * Handles the logic for navigating to a destination based on a deep link URI.
 */
private fun handleIncomingDeepLink(
    uri: Uri,
    backStack: MutableList<Any>,
    onHandled: () -> Unit
) {
    val destination = getDestinationFromUri(uri)
    if (destination != null && destination != Destination.Login) {
        backStack.clear()
        backStack.add(destination)
    } else if (backStack.contains(Destination.Login)) {
        // Fallback to Map if deep link is invalid but we're stuck on Login
        backStack.clear()
        backStack.add(Destination.Map)
    }
    // Always notify that the deep link has been processed to avoid re-triggering logic
    onHandled()
}

@Composable
private fun AuthNavigation(backStack: MutableList<Any>, authViewModel: AuthViewModel) {
    NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize()
    ) { key ->
        when (key) {
            is Destination.Login -> NavEntry(key) { LoginScreen(authViewModel) }
            else -> NavEntry(Unit) { Text("Auth Required") }
        }
    }
}

@Composable
private fun MainNavigation(
    backStack: MutableList<Any>,
    viewModel: MainViewModel,
    authViewModel: AuthViewModel
) {
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            topLevelDestinations.forEach { destination ->
                if (destination != Destination.Login) {
                    item(
                        selected = backStack.lastOrNull() == destination,
                        onClick = {
                            if (backStack.lastOrNull() != destination) {
                                backStack.clear()
                                backStack.add(destination)
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = stringResource(destination.labelRes)) },
                        label = { Text(stringResource(destination.labelRes)) }
                    )
                }
            }
        }
    ) {
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.fillMaxSize(),
            onBack = { if (backStack.size > 1) backStack.removeLast() }
        ) { key ->
            when (key) {
                is Destination.Map -> NavEntry(key) { MapScreen(viewModel, authViewModel) }
                is Destination.Reports -> NavEntry(key) {
                    if (authViewModel.currentUserRole == UserRole.ADMIN) {
                        AdminDashboardScreen(viewModel, authViewModel)
                    } else {
                        ReportsScreen(viewModel, authViewModel)
                    }
                }
                is Destination.Dashboard -> NavEntry(key) { DashboardScreen(viewModel, authViewModel) }
                else -> NavEntry(Unit) { Text("Unknown") }
            }
        }
    }
}

/**
 * Parses a URI and maps it to a corresponding app [Destination].
 * Supports both App Links (https) and custom schemes.
 */
private fun getDestinationFromUri(uri: Uri): Destination? {
    val host = uri.host?.lowercase(Locale.ROOT)
    val scheme = uri.scheme?.lowercase(Locale.ROOT)
    val pathSegments = uri.pathSegments

    val target = when {
        // Handle App Link: https://grameenlight.example.com[/app]/<target>
        scheme == "https" && host == "grameenlight.example.com" -> {
            when {
                pathSegments.size >= 2 && pathSegments[0].lowercase(Locale.ROOT) == "app" -> pathSegments[1]
                pathSegments.isNotEmpty() -> pathSegments[0]
                else -> "map"
            }
        }
        // Handle Custom Scheme: grameenlight://app/<target>
        scheme == "grameenlight" && host == "app" -> {
            pathSegments.getOrNull(0) ?: "map"
        }
        else -> null
    }

    return when (target?.lowercase(Locale.ROOT)) {
        "reports" -> Destination.Reports
        "dashboard" -> Destination.Dashboard
        "map" -> Destination.Map
        else -> {
            // Default to Map if the URL belongs to us but the path is unknown
            if (host == "grameenlight.example.com" || scheme == "grameenlight") Destination.Map else null
        }
    }
}
