package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.AuthViewModel
import com.example.myapplication.ui.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(authViewModel: AuthViewModel) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Branded Logo/Icon
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    tonalElevation = 8.dp
                ) {
                    Icon(
                        Icons.Rounded.Lightbulb,
                        contentDescription = null,
                        modifier = Modifier.padding(24.dp).fillMaxSize(),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Brightening Village Lives",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
                
                Spacer(modifier = Modifier.height(64.dp))
                
                LoginCard(
                    title = stringResource(R.string.user_login),
                    description = "Report and track streetlight status",
                    icon = Icons.Rounded.Person,
                    onClick = { authViewModel.login(UserRole.USER) }
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                LoginCard(
                    title = stringResource(R.string.admin_login),
                    description = "Manage all village lighting reports",
                    icon = Icons.Rounded.AdminPanelSettings,
                    onClick = { authViewModel.login(UserRole.ADMIN) },
                    isOutlined = true
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                TextButton(
                    onClick = { authViewModel.toggleLanguage() },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Rounded.Language, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${stringResource(R.string.language)}: ${if (authViewModel.currentLanguage == "en") "English" else "ಕನ್ನಡ"}",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
fun LoginCard(title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit, isOutlined: Boolean = false) {
    if (isOutlined) {
        OutlinedCard(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().height(100.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            LoginCardContent(title, description, icon)
        }
    } else {
        Card(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().height(100.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            LoginCardContent(title, description, icon)
        }
    }
}

@Composable
fun LoginCardContent(title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier.padding(20.dp).fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
