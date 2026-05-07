package com.smartnotif

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smartnotif.ui.MainViewModel
import com.smartnotif.ui.screens.*
import com.smartnotif.ui.theme.SmartNotifTheme

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Feed      : Screen("feed",      "Feed",      Icons.Default.Notifications)
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.BarChart)
    object NLP       : Screen("nlp",       "NLP",       Icons.Default.Psychology)
    object Settings  : Screen("settings",  "Settings",  Icons.Default.Settings)
}

val screens = listOf(Screen.Feed, Screen.Dashboard, Screen.NLP, Screen.Settings)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {}.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        enableEdgeToEdge()
        setContent {
            SmartNotifTheme {
                SmartNotifApp()
            }
        }
    }
}

@Composable
fun SmartNotifApp() {
    val navController = rememberNavController()
    val vm: MainViewModel = viewModel()
    val prefs by vm.prefsFlow.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                screens.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon     = {
                            // Show a dot on Feed icon when focus mode is active
                            if (screen is Screen.Feed && prefs.focusMode) {
                                BadgedBox(badge = { Badge() }) {
                                    Icon(screen.icon, contentDescription = null)
                                }
                            } else {
                                Icon(screen.icon, contentDescription = null)
                            }
                        },
                        label    = { Text(screen.label) },
                        selected = selected,
                        onClick  = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState    = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.Feed.route,
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Feed.route)      { FeedScreen(vm) }
            composable(Screen.Dashboard.route) { DashboardScreen(vm) }
            composable(Screen.NLP.route)       { NLPScreen(vm) }
            composable(Screen.Settings.route)  { SettingsScreen(vm) }
        }
    }
}
