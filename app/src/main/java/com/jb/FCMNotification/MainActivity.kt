package com.jb.FCMNotification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jb.FCMNotification.firebase.MyFirebaseMessagingService.Companion.subscribeForTopic

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        subscribeForTopic("shivam_test")
    }
}