package com.chaosthechaotic.lamb

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.Runnable

class CrocPollFgServ : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private var shouldPoll = false
    private lateinit var lambSS: LambSS
    private lateinit var prefHelper: PrefHelper
    private val pollRunnable = object : Runnable {
        override fun run() {
            if (shouldPoll) {
                pollCroc()
            }
            if (isRunning && shouldPoll) {
                handler.postDelayed(this, 5000)
            } else {
                stopSelf()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotifChannel()
        lambSS = LambSS(this)
        prefHelper = PrefHelper(this)

        shouldPoll = LambDataStore.pollCroc.getValBlocking(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        shouldPoll = LambDataStore.pollCroc.getValBlocking(this)
        if (!shouldPoll) {
            stopSelf()
            return START_NOT_STICKY
        }
        isRunning = true
        val notif = NotificationCompat.Builder(this, "croc_poll_channel")
            .setContentTitle("Lamb is polling")
            .setContentText("Lamb is polling croc for updates")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .build()

        startForeground(1, notif, ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE)
        handler.post(pollRunnable)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        shouldPoll = false
        handler.removeCallbacks(pollRunnable)
        prefHelper.cancelAllAsyncOps()
    }
    private fun createNotifChannel() {
        // Check if channel already exists
        val man = getSystemService(NotificationManager::class.java)
        if (man.getNotificationChannel("croc_poll_channel") == null) {
            val servC = NotificationChannel(
                "croc_poll_channel",
                "Croc Polling Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for polling croc"
            }
            man.createNotificationChannel(servC)
        }
    }

    external fun recvCroc(code: String): String
    external fun sndCroc(msg: String, code: String): String

    private fun pollCroc() {
        try {
            val stored = lambSS.decryptPwd()

            if (stored != null && stored.isNotEmpty()) {
                val res = recvCroc(stored)
                Log.d("CrocPoller", "Result with password: $stored: $res")
            } else {
                Log.d("CrocPoller", "No password found")
            }
        } catch (e: Exception) {
            Log.e("CrocPoller", "Error polling: ${e.message}", e)
        }
    }

    fun pollOnce(context: Context, password: String? = null) {
        try {
            val stored = password ?: LambSS(context).decryptPwd()

            if (stored != null && stored.isNotEmpty()) {
                val res = recvCroc(stored)
                Log.d("CrocPoller", "One time poll result with password: $stored: $res")
            } else {
                Log.d("CrocPoller", "No password found")
            }
        } catch (e: Exception) {
            Log.e("CrocPoller", "Error polling: ${e.message}", e)
        }
    }
}