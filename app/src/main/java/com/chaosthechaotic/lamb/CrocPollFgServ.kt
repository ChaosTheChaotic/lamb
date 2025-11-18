package com.chaosthechaotic.lamb

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
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
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning = true
        val notif = createNotif()
        startForeground(startId, notif)
        handler.post(pollRunnable)
        return START_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        handler.removeCallbacks(pollRunnable)
    }
    private fun createNotif(): Notification {
        val cid = "croc_poll_channel"
        return NotificationCompat.Builder(this, cid).setContentTitle("Lamb is polling croc").setContentText("Lamb is polling croc for updates").build()
    }
    private fun pollCroc() {}
}