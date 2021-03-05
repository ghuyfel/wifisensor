package com.ghuyfel.wifisensor.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ghuyfel.wifisensor.handlers.AlarmHandler
import javax.inject.Inject

class DeviceBootCompletedBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var alarmHandler: AlarmHandler
    override fun onReceive(context: Context?, intent: Intent?) = alarmHandler.startSensorReadingService()
}