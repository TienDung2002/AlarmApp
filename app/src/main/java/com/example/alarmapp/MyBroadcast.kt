package com.example.alarmapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Gọi service => phát chuông, hiển thị notify
        val serviceIntent = Intent(context, MyService::class.java)
        context?.startService(serviceIntent)
    }
}