package com.ghuyfel.wifisensor.handlers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.ghuyfel.wifisensor.R
import com.ghuyfel.wifisensor.notification.SensorNotification
import com.ghuyfel.wifisensor.sensor.models.WifiSensorReading
import com.ghuyfel.wifisensor.utils.toContentText
import com.ghuyfel.wifisensor.utils.Constants

class NotificationHandler constructor(
    private val context: Context,
    private val notificationManager: NotificationManager
) {

    fun notifySuccess(readings: List<WifiSensorReading>) {
        val sensorNotification =
            SensorNotification(context, readings.toContentText())
        showNotification(sensorNotification.notification)
    }

    fun notifyFailure(message: String) {
        val sensorNotification =
            SensorNotification(
                context = context,
                content = message
            )

        showNotification(sensorNotification.notification)
    }

    private fun showNotification(notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Constants.CHANNEL_ID,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.cancel(0)
        notificationManager.notify(0, notification)
    }
}