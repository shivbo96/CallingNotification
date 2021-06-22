package com.jb.FCMNotification

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video_calling.*

class VideoCallingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_calling)

        setAction()

        setConditions()
    }

    private fun setConditions() {
        if (intent.getBooleanExtra("isAccept", false)) {
            callConstraint.visibility = View.GONE
            Toast.makeText(this, "call Accepted", Toast.LENGTH_SHORT).show()

        } else {
            callConstraint.visibility = View.VISIBLE
            text_caller_name.text = intent.getStringExtra("isAccept")
        }
    }

    private fun setAction() {
        image_button_accept_call.setOnClickListener {
            Toast.makeText(this, "call Accepted", Toast.LENGTH_SHORT).show()
        }

        image_button_reject_call.setOnClickListener {
            Toast.makeText(this, "Call Rejected", Toast.LENGTH_SHORT).show()

        }
    }
}