package com.chaosthechaotic.lamb

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

class LambScreens {
    @Suppress("PropertyName")
    val UIElements = LambUIElements()
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
        val ctx = LocalContext.current
        val storedPwd by produceState<String?>(initialValue = null) {
            value = LambSS(ctx).decryptPwd()
        }
        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            UIElements.SettingsButton {navCont.navigate("settings_screen") }

            UIElements.GenericTextButton(
                label = "Poll Croc Now",
                onClickAction = {
                    scope.launch {
                        CrocPollFgServ().pollOnce(ctx, storedPwd)
                    }
                },
                enabled = storedPwd != null,
            )

            if (storedPwd == null) {
                Text(
                    text = "Please set the password correctly to poll croc",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
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

            if (storedPwd == null) {
                LambDataStore.pollCroc.setValAutoCo(ctx, false)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            UIElements.BackButton { navCont.popBackStack() }

            Box {
                UIElements.PasswordInput(
                    value = pwd,
                    label = "Croc password (must match computer)",
                    onValueChange = { newPass ->
                        pwd = newPass
                        if (newPass.isNotEmpty() && newPass.length > 6) {
                            lambSS.encryptStore(newPass)
                        }
                    },
                    validatePassword = { pwd ->
                        when {
                            pwd.isEmpty() -> "Password must not be empty"
                            pwd.length <= 6 -> "Password must be greater than 6 characters"
                            else -> null
                        }
                    }
                )
            }
            Column (
                modifier = Modifier.fillMaxWidth()
            ) {
                val ctx = LocalContext.current
                val scope = rememberCoroutineScope()

                // Collect Flow as state for getting current value
                val pollCroc by LambDataStore.pollCroc.getVal(ctx).collectAsStateWithLifecycle(initialValue = LambDataStore.pollCroc.default)

                UIElements.SettingsSwitch (
                    value = if (storedPwd == null) false else pollCroc,
                    label = "Poll Croc",
                    enabled = storedPwd != null,
                    onValueChange = { newValue ->
                        scope.launch {
                            LambDataStore.pollCroc.setVal(ctx, newValue)

                            val intent = Intent(ctx, CrocPollFgServ::class.java)
                            if (newValue) {
                                ctx.startForegroundService(intent)
                            } else {
                                ctx.stopService(intent)
                            }
                        }
                    }
                )
                if (storedPwd == null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Please enter a valid password to use this",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}