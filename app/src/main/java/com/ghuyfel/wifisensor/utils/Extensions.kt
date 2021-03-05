package com.ghuyfel.wifisensor.utils

import android.net.wifi.ScanResult
import android.util.Log
import com.ghuyfel.wifisensor.sensor.models.WifiSensorReading

fun ScanResult.toWifiSensorReading(): WifiSensorReading =
    WifiSensorReading(
        bssid = this.BSSID,
        ssid = this.SSID,
        level = this.level,
        timestamp = this.timestamp,
    )

 fun List<WifiSensorReading>.toContentText(): String {
     val sb = StringBuilder()
     forEach {
         Log.i("TAG", it.toString())
         sb.append("SSID: ${it.ssid}\nLEVEL: ${it.level}dBm\ntimestamp:${it.timestamp}\n\n")
     }
     return sb.toString()
 }

 fun List<WifiSensorReading>.toJsonString(): String {
     val list = map { it.toJsonString() }
     Log.i("TAG", list.toString())
     return list.toString()
 }
