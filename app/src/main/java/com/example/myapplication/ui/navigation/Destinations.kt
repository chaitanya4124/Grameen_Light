package com.example.myapplication.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Report
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Destination : NavKey {
    abstract val icon: ImageVector
    abstract val labelRes: Int

    @Serializable
    data object Login : Destination() {
        override val icon = Icons.Rounded.Login
        override val labelRes = com.example.myapplication.R.string.login
    }

    @Serializable
    data object Map : Destination() {
        override val icon = Icons.Rounded.Map
        override val labelRes = com.example.myapplication.R.string.map
    }

    @Serializable
    data object Reports : Destination() {
        override val icon = Icons.Rounded.Assignment
        override val labelRes = com.example.myapplication.R.string.reports
    }

    @Serializable
    data object Dashboard : Destination() {
        override val icon = Icons.Rounded.Lightbulb
        override val labelRes = com.example.myapplication.R.string.dashboard
    }
}

val topLevelDestinations = listOf(
    Destination.Map,
    Destination.Reports,
    Destination.Dashboard
)
