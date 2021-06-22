package com.jb.FCMNotification.firebase


import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationId = intent?.extras?.getInt("notificationId")
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationId != null) {
            notificationManager.cancel(notificationId)
        }
    }


}