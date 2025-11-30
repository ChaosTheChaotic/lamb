package com.chaosthechaotic.lamb

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

interface LambScreens : LambUIElements {
    @Composable
    fun AppNav() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "home_screen") {

            composable("home_screen") {
                HomeScreen(navCont = navController)
            }

            composable("settings_screen") {
                SettingsScreen(navCont = navController)
            }
        }
    }
    @Composable
    fun HomeScreen(navCont: NavController) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                SettingsButton { navCont.navigate("settings_screen") }
            }
        }
    }

    @Composable
    fun SettingsScreen(navCont: NavController) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                BackButton { navCont.popBackStack() }
            }
            Box(
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                PasswordInput("Placeholder", "Croc receive password (must match computer password)") {}
            }
        }
    }
}