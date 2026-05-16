package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.data.local.AuditReportEntity
import com.example.myapplication.data.local.PoleEntity
import com.example.myapplication.ui.AuthViewModel
import com.example.myapplication.ui.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MainViewModel, authViewModel: AuthViewModel) {
    val poles by viewModel.poles.collectAsState()
    var selectedPole by remember { mutableStateOf<PoleEntity?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    var showPoleSelector by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Grameen Light AI",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1D2435)
                    ) 
                },
                actions = {
                    IconButton(onClick = { viewModel.refreshPoles() }) {
                        Icon(Icons.Rounded.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = stringResource(R.string.logout))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF8F9FA)
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showPoleSelector = true },
                icon = { Icon(Icons.Rounded.AddAlert, null) },
                text = { Text("Raise Complaint") },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (poles.isEmpty()) {
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFF00C853))
                        Spacer(Modifier.height(24.dp))
                        Text("Locating Village Streetlights...", color = Color.Gray, fontWeight = FontWeight.Medium)
                        Button(
                            onClick = { viewModel.refreshPoles() },
                            modifier = Modifier.padding(top = 16.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Retry Discovery")
                        }
                    }
                }
            } else {
                // Simple Imaginary Map Container
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .padding(24.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(48.dp),
                    color = Color(0xFFE9ECEF),
                    tonalElevation = 4.dp
                ) {
                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                        val width = maxWidth
                        val height = maxHeight

                        // Background Dots Grid (Visual Texture)
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            repeat(12) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    repeat(8) {
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .background(Color(0xFFCED4DA), CircleShape)
                                        )
                                    }
                                }
                            }
                        }

                        // Interactive Lights (Poles)
                        poles.forEachIndexed { index, pole ->
                            if (index < 15) { // Show up to 15 poles on the map
                                val row = (index / 3) + 1
                                val col = (index % 3) + 1
                                
                                val xPos = (width / 4) * col - 30.dp
                                val yPos = (height / 6) * row - 30.dp

                                LightMarker(
                                    pole = pole,
                                    modifier = Modifier.offset(x = xPos, y = yPos),
                                    onClick = {
                                        selectedPole = pole
                                        showSheet = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
            
            // Legend
            Row(
                modifier = Modifier.padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SimpleLegendItem(Color(0xFF00C853), stringResource(R.string.working))
                SimpleLegendItem(Color(0xFFFF5252), stringResource(R.string.fused))
                SimpleLegendItem(Color(0xFFFFAB40), stringResource(R.string.burning_in_day))
            }
            Spacer(modifier = Modifier.height(64.dp))
        }
    }

    // Modal for selecting a pole from a list (Complaint Raising Option)
    if (showPoleSelector) {
        AlertDialog(
            onDismissRequest = { showPoleSelector = false },
            title = { Text("Lamp Post Registry", fontWeight = FontWeight.Bold) },
            text = {
                if (poles.isEmpty()) {
                    Text("No streetlights discovered yet. Try refreshing.")
                } else {
                    LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                        items(poles) { pole ->
                            ListItem(
                                headlineContent = { Text("Pole ${pole.id}", fontWeight = FontWeight.Bold) },
                                supportingContent = { Text(getLocalizedAreaName(pole.areaName)) },
                                leadingContent = { 
                                    Box(Modifier.size(24.dp).background(getStatusColor(pole.status), CircleShape))
                                },
                                modifier = Modifier.clickable {
                                    selectedPole = pole
                                    showPoleSelector = false
                                    showSheet = true
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPoleSelector = false }) { Text("Close") }
            }
        )
    }

    if (showSheet && selectedPole != null) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            ReportSheetContent(
                pole = selectedPole!!,
                onReport = { status ->
                    viewModel.reportStatus(selectedPole!!.id, status)
                    showSheet = false
                }
            )
        }
    }
}

fun getStatusColor(status: String): Color {
    return when (status) {
        "Working" -> Color(0xFF00C853)
        "Fused" -> Color(0xFFFF5252)
        "Burning in Day" -> Color(0xFFFFAB40)
        else -> Color.Gray
    }
}

@Composable
fun LightMarker(pole: PoleEntity, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val statusColor = getStatusColor(pole.status)
    val hasGlow = pole.status != "Working"

    Box(
        modifier = modifier
            .size(60.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (hasGlow) {
            // Animated or Layered Halos
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(statusColor.copy(alpha = 0.15f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(statusColor.copy(alpha = 0.25f), CircleShape)
            )
        }

        // The Lamp Unit
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(statusColor, CircleShape)
                .then(if (!hasGlow) Modifier.shadow(3.dp, CircleShape) else Modifier)
        )
    }
}

@Composable
fun SimpleLegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(10.dp).background(color, CircleShape))
        Spacer(Modifier.width(8.dp))
        Text(label, color = Color(0xFF6C757D), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ReportSheetContent(pole: PoleEntity, onReport: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .padding(bottom = 64.dp, top = 16.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Report Issue: Pole ${pole.id}",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
            color = Color(0xFF1D2435)
        )
        Text(
            getLocalizedAreaName(pole.areaName),
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF6C757D)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SimpleStatusButton("Working", Color(0xFF00C853), onReport)
            SimpleStatusButton("Fused", Color(0xFFFF5252), onReport)
            SimpleStatusButton("Burning in Day", Color(0xFFFFAB40), onReport)
        }
    }
}

@Composable
fun SimpleStatusButton(status: String, color: Color, onClick: (String) -> Unit) {
    val label = when(status) {
        "Working" -> stringResource(R.string.working)
        "Fused" -> stringResource(R.string.fused)
        "Burning in Day" -> stringResource(R.string.burning_in_day)
        else -> status
    }
    
    Button(
        onClick = { onClick(status) },
        modifier = Modifier.fillMaxWidth().height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(viewModel: MainViewModel, authViewModel: AuthViewModel) {
    val reports by viewModel.reports.collectAsState()

    Scaffold(
        topBar = { 
            CenterAlignedTopAppBar(
                title = { Text("Repair Tracker", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { viewModel.refreshPoles() }) {
                        Icon(Icons.Rounded.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = stringResource(R.string.logout))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF8F9FA)
                )
            ) 
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        if (reports.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No active reports", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(reports) { report ->
                    ReportItem(report)
                }
            }
        }
    }
}

@Composable
fun ReportItem(report: AuditReportEntity) {
    val simulatedStatusKey = remember(report.id) {
        listOf("Pending", "Assigned", "Fixed").random()
    }
    
    val statusLabel = when (simulatedStatusKey) {
        "Pending" -> stringResource(R.string.pending)
        "Assigned" -> stringResource(R.string.assigned)
        "Fixed" -> stringResource(R.string.fixed)
        else -> simulatedStatusKey
    }
    
    val statusColor = when (simulatedStatusKey) {
        "Fixed" -> Color(0xFF00C853)
        "Assigned" -> Color(0xFF2196F3)
        else -> Color(0xFFFFAB40)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = report.complaintId ?: "N/A",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1D2435)
                )
                Text(
                    text = "Pole ${report.poleId}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6C757D)
                )
            }
            
            Surface(
                color = statusColor.copy(alpha = 0.1f),
                contentColor = statusColor,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = statusLabel,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: MainViewModel, authViewModel: AuthViewModel) {
    val reports by viewModel.reports.collectAsState()
    val energySaved = reports.count { it.status == "Burning in Day" } * 5.2

    Scaffold(
        topBar = { 
            CenterAlignedTopAppBar(
                title = { Text("Impact Dashboard", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { viewModel.refreshPoles() }) {
                        Icon(Icons.Rounded.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = stringResource(R.string.logout))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF8F9FA)
                )
            ) 
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                EnergyHeroCard(energySaved)
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ImpactCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.AutoGraph,
                        value = "${reports.size}",
                        label = "Audits",
                        color = Color(0xFF6200EE)
                    )
                    ImpactCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.DoneAll,
                        value = "${reports.count { it.status == "Fused" }}",
                        label = "Fixed",
                        color = Color(0xFF00C853)
                    )
                }
            }
        }
    }
}

@Composable
fun EnergyHeroCard(energySaved: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1D2435))
    ) {
        Column(
            modifier = Modifier.padding(32.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Energy Saved", color = Color.White.copy(alpha = 0.6f), style = MaterialTheme.typography.titleMedium)
            Text(
                text = String.format("%.1f kWh", energySaved),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { (energySaved / 100).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = Color(0xFF00C853),
                trackColor = Color.White.copy(alpha = 0.1f)
            )
        }
    }
}

@Composable
fun ImpactCard(modifier: Modifier = Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(32.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = Color(0xFF1D2435))
            Text(label, style = MaterialTheme.typography.labelMedium, color = Color(0xFF6C757D))
        }
    }
}

@Composable
fun getLocalizedAreaName(areaKey: String): String {
    return when (areaKey) {
        "temple_road" -> stringResource(R.string.temple_road)
        "market_square" -> stringResource(R.string.market_square)
        "bus_stand" -> stringResource(R.string.bus_stand)
        "primary_school" -> stringResource(R.string.primary_school)
        "main_street" -> stringResource(R.string.main_street)
        "west_gate" -> stringResource(R.string.west_gate)
        else -> stringResource(R.string.village_area)
    }
}
