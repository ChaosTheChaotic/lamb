package com.chaosthechaotic.lamb

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

interface LambUIElements {
    @Composable
    fun SettingsButton(onClickAction: () -> Unit) {
        IconButton(onClick = onClickAction) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings menu",
                tint = Color.White,
            )
        }
    }

    @Composable
    fun BackButton(onClickAction: () -> Unit) {
        IconButton(onClick = onClickAction) {
            Icon(
                imageVector = Icons.Filled.ArrowCircleLeft,
                contentDescription = "Back to previous",
                tint = Color.White,
            )
        }
    }

    @Composable
    fun PasswordInput(
        value: String,
        label: String?,
        onValueChange: (String) -> Unit,
        validatePassword: ((String) -> String?)?
    ) {
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        var errMsg by rememberSaveable { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = { newValue: String ->
                    onValueChange(newValue)
                    errMsg = validatePassword?.invoke(newValue)
                },
                label = { Text(label ?: "Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val img =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val desc = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = img, contentDescription = desc)
                    }
                },
                isError = errMsg != null,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (errMsg != null) {
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
                    text = errMsg!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }

    @Composable
    fun SettingsSwitch(value: Boolean, label: String, onValueChange: (Boolean) -> Unit) {
        Row {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = value,
                onCheckedChange = { newState -> onValueChange(newState) }
            )
        }
    }
}