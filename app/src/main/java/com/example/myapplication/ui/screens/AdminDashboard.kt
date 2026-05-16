package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.AuthViewModel
import com.example.myapplication.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(viewModel: MainViewModel, authViewModel: AuthViewModel) {
    val reports by viewModel.reports.collectAsState()
    val poles by viewModel.poles.collectAsState()

    val fusedCount = poles.count { it.status == "Fused" }
    val pendingCount = reports.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.admin_dashboard), fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = stringResource(R.string.logout))
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.all_village_reports),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.total_fused),
                        value = "$fusedCount",
                        icon = Icons.Rounded.LightbulbCircle,
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                    AdminStatCard(
                        modifier = Modifier.weight(1f),
                        title = stringResource(R.string.pending_repairs),
                        value = "$pendingCount",
                        icon = Icons.Rounded.PendingActions,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }
            
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }

            items(reports) { report ->
                ReportItem(report) 
            }
        }
    }
}

@Composable
fun AdminStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    containerColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(icon, null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black)
            Text(text = title, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
        }
    }
}
