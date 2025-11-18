package com.chaosthechaotic.lamb

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.PermissionChecker
import kotlinx.coroutines.Runnable

class CrocPollFgServ : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private val pollRunnable = object : Runnable {
        override fun run() {
            pollCroc() // TODO: Implement
            if (isRunning) {
                handler.postDelayed(this, 5000)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotifChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning = true
        val notif = NotificationCompat.Builder(this, "croc_poll_channel").setContentTitle("Lamb is polling").setContentText("Lamb is polling croc for updates").setSmallIcon(R.mipmap.ic_launcher).build()
        startForeground(1, notif, ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE)
        handler.post(pollRunnable)
        return START_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        handler.removeCallbacks(pollRunnable)
    }
    private fun createNotifChannel() {
        // Check if channel already exists
        val man = getSystemService(NotificationManager::class.java)
        if (man.getNotificationChannel("croc_poll_channel") == null) {
            val servC = NotificationChannel(
                "croc_poll_channel",
                "Croc Polling Service Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Channel for polling croc"
            }
            man.createNotificationChannel(servC)
        }
    }
    private fun pollCroc() {
        val netPerm = PermissionChecker.checkSelfPermission(this, Manifest.permission.INTERNET)
        val acnetPerm = PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
        if (netPerm != PermissionChecker.PERMISSION_GRANTED && acnetPerm != PermissionChecker.PERMISSION_GRANTED) {
            stopSelf()
            return
        }
    }
}