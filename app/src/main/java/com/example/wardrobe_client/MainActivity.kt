package com.example.wardrobe_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wardrobe_client.presentation.navigation.AppNavGraph
import com.example.wardrobe_client.presentation.navigation.BottomNavBar
import com.example.wardrobe_client.presentation.navigation.Screen
import com.example.wardrobe_client.presentation.navigation.bottomNavItems
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = currentRoute in bottomNavItems.map { it.route }

                val fabRoute = when (currentRoute) {
                    Screen.Clothing.route -> Screen.AddClothingItem.route
                    Screen.Outfits.route -> Screen.OutfitEditor.createRoute()
                    Screen.Trips.route -> Screen.AddTrip.route
                    else -> null
                }

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(navController = navController)
                        }
                    },
                    floatingActionButton = {
                        if (fabRoute != null) {
                            FloatingActionButton(
                                onClick = { navController.navigate(fabRoute) }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Добавить")
                            }
                        }
                    },
                    floatingActionButtonPosition = androidx.compose.material3.FabPosition.Start
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        AppNavGraph(
                            navController = navController,
                            startDestination = Screen.Login.route
                        )
                    }
                }
            }
        }
    }
}