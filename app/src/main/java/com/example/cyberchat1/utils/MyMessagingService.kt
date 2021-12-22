package com.example.cyberchat1.utils


import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.cyberchat1.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import org.json.JSONException
import org.json.JSONObject


class MyMessagingService :FirebaseMessagingService() {

    private val TAG: String = MyMessagingService::class.java.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage == null) return

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            try {
                val json = JSONObject(remoteMessage.notification as Map<*, *>)
                handleDataMessage(json)
            } catch (e: Exception) {
                Log.e(
                     TAG,
                    "Exception: " + e.message
                )
            }
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification?.body);
        }

        super.onMessageReceived(remoteMessage)
    }



    private fun handleDataMessage(json: JSONObject) {
        Log.e(TAG, "push json: $json")
        try {
            val title = json.getString("title")
            val message = json.getString("message")
            val imageUrl = json.getString("image")
            Log.e(TAG, "title: $title")
            Log.e(TAG, "message: $message")
            Log.e(TAG, "imageUrl: $imageUrl")

            val inboxStyle =
                NotificationCompat.InboxStyle()
            inboxStyle.addLine(message)
            val notification: Notification = NotificationCompat.Builder(this,"noti").setSmallIcon(R.drawable.ic_baseline_insert_emoticon_24).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setSubText(message)
                .setStyle(inboxStyle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_baseline_keyboard_24))
                .setContentText(message)
                .build()
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(100, notification)

        } catch (e: JSONException) {
            Log.e(
               TAG,
                "Json Exception: " + e.message
            )
        } catch (e: java.lang.Exception) {
            Log.e(
                TAG,
                "Exception: " + e.message
            )
        }
    }




}