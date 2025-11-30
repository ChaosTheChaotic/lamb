package com.chaosthechaotic.lamb

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

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

    @Composable
    fun BackButton(onClickAction: () -> Unit) {
        IconButton(onClick = onClickAction) {
            Icon(
                imageVector = Icons.Filled.ArrowCircleLeft,
                contentDescription = "Back to previous",
            )
        }
    }

    @Composable
    fun PasswordInput(value: String, label: String?, onValueChange: (String) -> Unit) {
        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label ?: "Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val img = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val desc = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = img, contentDescription = desc)
                }
            },
        )
    }
}