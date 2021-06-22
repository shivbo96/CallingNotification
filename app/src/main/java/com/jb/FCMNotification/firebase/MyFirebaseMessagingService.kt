package com.jb.FCMNotification.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jb.FCMNotification.MainActivity
import com.jb.FCMNotification.R
import com.jb.FCMNotification.VideoCallingActivity
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val notificationChannelId = "102"
    private val notificationChannelName = "shivamFcmChannelName"
    private val notificationId = 102
    private val defaultNotificationChannelId = "default"



    override fun onNewToken(s: String) {
        super.onNewToken(s)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.from)
        Log.d("msg", "onMessageReceived: " + remoteMessage.data["message"])
        var mTitle = ""
        var mBody = ""
        var mType = ""

        remoteMessage.data.let {
            Log.d("#####", "Message data payload: " + remoteMessage.data)
            mType = "${it["Type"]}"
        }
        remoteMessage.notification?.let {
            Log.d("#####", "Message Notification Body: ${it.body}")
            mBody = "${it.body}"
            mTitle = "${it.title}"
        }

//        mShowNotification(mTitle, mBody)
        createNotification()
    }

    companion object {
        const val TYPE = "Type"
        const val STATUS = "Status"
        const val DATA = "Data"
        private val TAG = "###"
        fun subscribeForTopic(topic: String) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener { task: Task<Void?> ->
                    if (!task.isSuccessful) {
                        Log.d(
                            TAG,
                            "Subscription $topic Failed"
                        )
                    } else Log.d(
                        TAG,
                        "Subscription $topic Successful"
                    )
                }
        }

        fun unsubscribeForTopic(topic: String) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener { task: Task<Void?> ->
                    if (!task.isSuccessful) {
                        Log.d(
                            TAG,
                            "UnSubscription $topic Failed"
                        )
                    } else Log.d(
                        TAG,
                        "UnSubscription $topic Successful"
                    )
                }
        }
    }

    private fun mShowNotification(
        mTitle: String,
        mBody: String
    ) {
        val intent = Intent()

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val channelId = notificationChannelId
        val channelName = notificationChannelName
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId).apply {
            setContentTitle(mTitle)
            setContentText(mBody)
            setAutoCancel(true)
            setContentIntent(pendingIntent)
            setSmallIcon(R.mipmap.ic_launcher)
            val bigTextStyle = NotificationCompat.BigTextStyle()
            bigTextStyle.setBigContentTitle(mTitle)
            bigTextStyle.bigText(mBody)
            setStyle(bigTextStyle)
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            channel.setSound(defaultSoundUri, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }


    private fun createNotification() {
        val defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, defaultNotificationChannelId)
        mBuilder.setContentTitle("Incoming Call")
        mBuilder.setContentIntent(getMainIntent())
        mBuilder.addAction(
            NotificationCompat.Action.Builder(
                R.drawable.ic_baseline_call_24,
                HtmlCompat.fromHtml(
                    "<font color=\"" + ContextCompat.getColor(
                        this,
                        R.color.green
                    ) + "\">Accept Call</font>", HtmlCompat.FROM_HTML_MODE_LEGACY
                ),
                getAcceptIntent()
            )
                .build()
        )
        mBuilder.addAction(
            NotificationCompat.Action.Builder(
                R.drawable.ic_baseline_call_end_24,
                HtmlCompat.fromHtml(
                    "<font color=\"" + ContextCompat.getColor(
                        this,
                        R.color.red
                    ) + "\">Reject Call</font>", HtmlCompat.FROM_HTML_MODE_LEGACY
                ),
                getRejectIntent()
            )
                .build()
        )
        mBuilder.setContentText("Shivam Mishra")
        mBuilder.setSmallIcon(R.drawable.ic_baseline_call_24)
        mBuilder.setAutoCancel(true)
        mBuilder.setOngoing(true)
        mBuilder.priority = NotificationCompat.PRIORITY_HIGH
        mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL)
        mBuilder.setSound(defaultRingtoneUri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            val importanceHigh = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                notificationChannelId,
                notificationChannelName,
                importanceHigh
            )
            mBuilder.setChannelId(notificationChannelId)
            notificationChannel.setSound(defaultRingtoneUri, audioAttributes)
            mNotificationManager.createNotificationChannel(notificationChannel)

        }
        mNotificationManager.notify(notificationId, mBuilder.build())
    }

    private fun getRejectIntent(): PendingIntent? {
        val buttonIntent = Intent(this, NotificationBroadcastReceiver::class.java)
        buttonIntent.putExtra("notificationId", notificationId)
        return PendingIntent.getBroadcast(
            this,
            System.currentTimeMillis().toInt(), buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

    }

    private fun getAcceptIntent(): PendingIntent? {

        val buttonIntent = Intent(this, MainActivity::class.java)
        buttonIntent.putExtra("notificationId", notificationId)
        buttonIntent.putExtra("UserName", "Shivam Mishra")
        buttonIntent.putExtra("isAccept", true)
        return PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(), buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getMainIntent(): PendingIntent? {
        val buttonIntent = Intent(this, VideoCallingActivity::class.java)
        buttonIntent.putExtra("notificationId", notificationId)
        buttonIntent.putExtra("UserName", "Shivam Mishra")
        buttonIntent.putExtra("isAccept", false)
        return PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(), buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}