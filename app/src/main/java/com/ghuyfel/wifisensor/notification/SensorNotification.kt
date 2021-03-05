package com.ghuyfel.wifisensor.notification

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import androidx.core.app.NotificationCompat
import com.ghuyfel.wifisensor.R
import com.ghuyfel.wifisensor.ui.MainActivity
import com.ghuyfel.wifisensor.utils.Constants

class SensorNotification(
    private val context: Context,
    private val content: String
) {

    var notification :Notification

    init {
        val notificationBuilder =
            NotificationCompat.Builder(context.applicationContext, Constants.CHANNEL_ID)
        notificationBuilder
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(
                context.getString(
                    R.string.sending_wifi_readings,
                    content
                )
            )
            .setSmallIcon(R.drawable.ic_baseline_perm_scan)
            .setAutoCancel(false)
            .setStyle(NotificationCompat.BigTextStyle())
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .priority = NotificationCompat.PRIORITY_LOW

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_CLEAR_TOP)
        notification = notificationBuilder.build()
    }

}