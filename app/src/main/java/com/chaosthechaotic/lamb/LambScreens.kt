package com.chaosthechaotic.lamb

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
        val ctx = LocalContext.current
        val lambSS = remember { LambSS(ctx) }
        var pwd by rememberSaveable { mutableStateOf("") }
        var storedPwd by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            storedPwd = lambSS.decryptPwd()
            pwd = storedPwd ?: ""
        }

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
                PasswordInput(
                    value = pwd,
                    label = "Croc password (must match password set on computer)",
                    onValueChange = { newPass ->
                        pwd = newPass
                        if (newPass.length > 6) {
                            lambSS.encryptStore(newPass)
                        }
                    },
                    validatePassword = {pwd ->
                        when {
                            pwd.isEmpty() -> null
                            pwd.length <= 6 -> "Password must be greater than 6 characters"
                            else -> null
                        }
                    }
                )
            }
        }
    }
}