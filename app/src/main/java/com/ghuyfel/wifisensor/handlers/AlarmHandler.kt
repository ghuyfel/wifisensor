package com.ghuyfel.wifisensor.handlers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ghuyfel.wifisensor.receivers.SensorBroadcastReceiver
import com.ghuyfel.wifisensor.utils.Constants

class AlarmHandler(private val context: Context, private val alarmManager: AlarmManager) {

    fun startSensorReadingService() {
        val intent = PendingIntent.getBroadcast(
            context, Constants.ALARM_REQUEST_ID,
            Intent(context, SensorBroadcastReceiver::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 5000, Constants.WIFI_SCANNING_INTERVAL, intent)
    }

    fun stopSensorReadingService() {
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context, Constants.ALARM_REQUEST_ID,
            Intent(context, SensorBroadcastReceiver::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }
}