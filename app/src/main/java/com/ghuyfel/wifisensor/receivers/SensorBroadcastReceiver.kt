package com.ghuyfel.wifisensor.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ghuyfel.wifisensor.handlers.ReadingsHandler
import com.ghuyfel.wifisensor.sensor.Sensor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SensorBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var sensor: Sensor

    @Inject
    lateinit var readingsHandler: ReadingsHandler

    override fun onReceive(context: Context?, intent: Intent?) {
       readingsHandler.handleReadings(sensor.getWifiScanResult())
    }
}