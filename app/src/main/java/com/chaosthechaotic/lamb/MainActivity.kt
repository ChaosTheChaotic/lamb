package com.chaosthechaotic.lamb

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.PermissionChecker
import androidx.activity.compose.setContent

class MainActivity : AppCompatActivity(), LambScreens {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PermissionChecker.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        if (LambDataStore.pollCroc.getValBlocking(this)) {
            val intent = Intent(this, CrocPollFgServ::class.java)
            startForegroundService(intent)
        }

        setContent {
            AppNav()
        }
    }

    companion object {
        // Used to load the 'lamb' library on application startup.
        init {
            System.loadLibrary("lamb")
        }
    }
}