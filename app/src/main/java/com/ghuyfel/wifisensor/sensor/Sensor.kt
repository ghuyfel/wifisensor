package com.ghuyfel.wifisensor.sensor

import android.content.Context
import android.net.wifi.WifiManager
import com.ghuyfel.wifisensor.exceptions.NoLocationPermissionException
import com.ghuyfel.wifisensor.exceptions.WifiDisabledException
import com.ghuyfel.wifisensor.sensor.models.WifiSensorReading
import com.ghuyfel.wifisensor.utils.WifiPermissionHelper
import com.ghuyfel.wifisensor.utils.toWifiSensorReading
import com.ghuyfel.wifisensor.utils.DataState

class Sensor(
    private val context: Context,
    private val wifiManager: WifiManager
) {

    fun getWifiScanResult(): DataState<List<WifiSensorReading>> =
        when {
            !WifiPermissionHelper.isPermissionGranted(context.applicationContext) -> {
                DataState.Error(NoLocationPermissionException())
            }

            wifiManager.isWifiEnabled -> {
                val scanResult = wifiManager.scanResults
                DataState.Success(scanResult.map { it.toWifiSensorReading() })
            }
            else -> {
                DataState.Error(WifiDisabledException())
            }
        }

}