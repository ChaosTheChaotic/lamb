package com.chaosthechaotic.lamb

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

interface LambScreen : LambUIElements {
    @Composable
    fun HomeScreen() {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
            ) {
                SettingsButton {  }
            }
        }
    }
}