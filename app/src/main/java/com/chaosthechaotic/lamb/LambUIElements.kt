package com.chaosthechaotic.lamb

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

interface LambUIElements {
    @Composable
    fun SettingsButton(onClickAction: () -> Unit) {
        IconButton(onClick = onClickAction) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings menu",
            )
        }
    }
}