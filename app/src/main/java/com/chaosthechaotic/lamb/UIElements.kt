package com.chaosthechaotic.lamb

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class UIElements {
    @Composable
    fun SettingsButton(onClickAction: () -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onClickAction) { }
        }
    }
}