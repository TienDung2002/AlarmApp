package com.example.alarmapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyService : Service() {
    lateinit var mySound: MediaPlayer
    lateinit var notificationBuilder: NotificationCompat.Builder

    companion object {
        const val CHANNEL_ID = "channel_running"
    }

    override fun onCreate() {
        super.onCreate()
        mySound = MediaPlayer.create(this, R.raw.infinityink_bassbossted)
        mySound.isLooping = true
        notificationBuilder = NotificationCompat.Builder(baseContext, CHANNEL_ID)
        createNotificationChannel()
        showNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP_ALARM") {
            stopPlay()
        }
        showNotification()
        mySound.start()
        return START_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        mySound.release()
    }

    fun stopPlay() {
        mySound.stop()
        stopForeground(STOP_FOREGROUND_REMOVE)  // Dừng foreground
        stopSelf()  // Dừng service
    }

    fun createNotificationChannel() {
        val notifyChannel = NotificationChannel(
            CHANNEL_ID,
            "Chuông đang phát",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notifyChannel)
    }

    private fun showNotification(): Notification {
        // background img
        val bitmapImg = BitmapFactory.decodeResource(resources, R.drawable.background)

        val stopIntent = Intent(this@MyService, MyService::class.java)
        stopIntent.action = "STOP_ALARM"
        val stopPending = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this@MyService, CHANNEL_ID)
            .setContentTitle("Báo thức đang chạy")
            .setContentText("Chuông đang phát")
            .addAction(R.drawable.pause_compat, "Stop", stopPending)
            .setSmallIcon(R.drawable.alarm_icon)
            .setLargeIcon(bitmapImg)
            .setAutoCancel(true)
            .build()
        startForeground(13, notification)

        return notification
    }
}
// Đăng ký và khai báo service, broadcast trong manifest