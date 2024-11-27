package com.example.trackitupapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.trackitupapp.activities.LoginActivity

class NotificationReceiver : BroadcastReceiver() {
    private val channelId = "notification_channel"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        // Retrieve the custom message passed in the intent
        val message = intent.getStringExtra("message") ?: "Išgerkite vaistą"

        // Create the notification channel
        val channel = NotificationChannel(
            channelId,
            "Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // Create an intent for the activity you want to open when the notification is clicked
        val loginIntent = Intent(context, LoginActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            loginIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification with the custom message
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Track It Up")  // You can customize this title if needed
            .setContentText(message)               // Custom message passed from the intent
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Handle permission for showing notifications (on Android 13 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                with(NotificationManagerCompat.from(context)) {
                    notify(intent.getIntExtra("notificationId", 0), notification)
                }
            }
        } else {
            with(NotificationManagerCompat.from(context)) {
                notify(intent.getIntExtra("notificationId", 0), notification)
            }
        }
    }
}
