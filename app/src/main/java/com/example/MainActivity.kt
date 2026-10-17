package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.DealersScreen
import com.example.ui.screens.HunterScreen
import com.example.ui.screens.RigHubScreen
import com.example.ui.screens.ShoppingSlipScreen
import com.example.ui.screens.SpecCheckerScreen
import com.example.ui.screens.StudioScreen
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.ElectricVioletContainer
import com.example.ui.theme.EmeraldLowestPrice
import com.example.ui.theme.FrostLavenderCanvas
import com.example.ui.theme.GreenBg
import com.example.ui.theme.GreenLowest
import com.example.ui.theme.InkTextPrimary
import com.example.ui.theme.InkTextSecondary
import com.example.ui.theme.NammaRigTheme
import com.example.viewmodel.AppTab
import com.example.viewmodel.RigViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: RigViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NammaRigTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                // Hardware back press navigation: returns to RIG_HUB if on a secondary tab
                BackHandler(enabled = uiState.currentTab != AppTab.RIG_HUB) {
                    viewModel.switchTab(AppTab.RIG_HUB)
                }

                androidx.compose.runtime.LaunchedEffect(uiState.toastMessage) {
                    uiState.toastMessage?.let { msg ->
                        android.widget.Toast.makeText(this@MainActivity, msg, android.widget.Toast.LENGTH_SHORT).show()
                        viewModel.clearToast()
                    }
                }

                NammaRigApp(
                    viewModel = viewModel,
                    uiState = uiState
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NammaRigApp(
    viewModel: RigViewModel,
    uiState: com.example.viewmodel.RigUiState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Surface(
                color = FrostLavenderCanvas,
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ElectricViolet),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeveloperBoard,
                            contentDescription = "NammaRig Icon",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Namma",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 19.sp
                                ),
                                color = InkTextPrimary
                            )
                            Text(
                                text = "Rig",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 19.sp
                                ),
                                color = ElectricViolet
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = GreenBg,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "CHENNAI",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = GreenLowest,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Ritchie Street & Mount Road Hardware Hub",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                            color = InkTextSecondary
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                // 1. Rig Hub Tab
                NavigationBarItem(
                    selected = uiState.currentTab == AppTab.RIG_HUB,
                    onClick = { viewModel.switchTab(AppTab.RIG_HUB) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Rig Hub"
                        )
                    },
                    label = { Text("Rig Hub", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ElectricViolet,
                        selectedTextColor = ElectricViolet,
                        indicatorColor = ElectricVioletContainer
                    ),
                    modifier = Modifier.testTag("nav_item_hub")
                )

                // 2. CAD Studio Tab
                NavigationBarItem(
                    selected = uiState.currentTab == AppTab.CAD_STUDIO,
                    onClick = { viewModel.switchTab(AppTab.CAD_STUDIO) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = "CAD Studio"
                        )
                    },
                    label = { Text("CAD Studio", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ElectricViolet,
                        selectedTextColor = ElectricViolet,
                        indicatorColor = ElectricVioletContainer
                    ),
                    modifier = Modifier.testTag("nav_item_studio")
                )

                // 3. Blueprint (Center Highlighted with Fit Badge)
                NavigationBarItem(
                    selected = uiState.currentTab == AppTab.BLUEPRINT,
                    onClick = { viewModel.switchTab(AppTab.BLUEPRINT) },
                    icon = {
                        BadgedBox(
                            badge = {
                                Surface(
                                    color = if (uiState.clearanceReport.compatibilityScore >= 85) GreenLowest else Color(0xFFD97706),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.offset(y = (-4).dp)
                                ) {
                                    Text(
                                        text = "${uiState.clearanceReport.compatibilityScore}% Fit",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 8.5.sp,
                                            fontWeight = FontWeight.Black
                                        ),
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(if (uiState.currentTab == AppTab.BLUEPRINT) ElectricViolet else ElectricViolet.copy(alpha = 0.85f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Layers,
                                    contentDescription = "Blueprint",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    label = { Text("Blueprint", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ElectricViolet,
                        selectedTextColor = ElectricViolet,
                        indicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.testTag("nav_item_blueprint")
                )

                // 4. Hunter Tab
                NavigationBarItem(
                    selected = uiState.currentTab == AppTab.HUNTER,
                    onClick = { viewModel.switchTab(AppTab.HUNTER) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Hunter Tab"
                        )
                    },
                    label = { Text("Hunter", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ElectricViolet,
                        selectedTextColor = ElectricViolet,
                        indicatorColor = ElectricVioletContainer
                    ),
                    modifier = Modifier.testTag("nav_item_hunter")
                )

                // 5. Price Pulse Tab
                NavigationBarItem(
                    selected = uiState.currentTab == AppTab.PRICE_PULSE,
                    onClick = { viewModel.switchTab(AppTab.PRICE_PULSE) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.TrendingDown,
                            contentDescription = "Price Pulse Tab"
                        )
                    },
                    label = { Text("Price Pulse", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ElectricViolet,
                        selectedTextColor = ElectricViolet,
                        indicatorColor = ElectricVioletContainer
                    ),
                    modifier = Modifier.testTag("nav_item_pulse")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                AppTab.RIG_HUB -> RigHubScreen(
                    viewModel = viewModel,
                    uiState = uiState
                )
                AppTab.CAD_STUDIO -> StudioScreen(
                    viewModel = viewModel,
                    uiState = uiState
                )
                AppTab.BLUEPRINT -> SpecCheckerScreen(
                    viewModel = viewModel,
                    uiState = uiState
                )
                AppTab.HUNTER -> HunterScreen(
                    viewModel = viewModel,
                    uiState = uiState
                )
                AppTab.PRICE_PULSE -> ShoppingSlipScreen(
                    viewModel = viewModel,
                    uiState = uiState
                )
            }
        }
    }
}
