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
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wardrobe_client.presentation.navigation.AppNavGraph
import com.example.wardrobe_client.presentation.navigation.BottomNavBar
import com.example.wardrobe_client.presentation.navigation.Screen
import com.example.wardrobe_client.presentation.navigation.bottomNavItems
import com.example.wardrobe_client.presentation.theme.ShugaiBluePrimary
import com.example.wardrobe_client.presentation.theme.ShugaiTheme
import com.example.wardrobe_client.presentation.theme.ShugaiTextOnPrimary
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShugaiTheme {
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
                                onClick = { navController.navigate(fabRoute) },
                                containerColor = ShugaiBluePrimary,
                                contentColor = ShugaiTextOnPrimary,
                                elevation = FloatingActionButtonDefaults.elevation()
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Добавить")
                            }
                        }
                    },
                    floatingActionButtonPosition = androidx.compose.material3.FabPosition.Start
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
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