package com.ghuyfel.wifisensor.sensor.models

import org.json.JSONObject

data class WifiSensorReading(
    val bssid: String,
    val ssid: String,
    val level: Int,
    val timestamp: Long
) {
    fun toJsonString(): String {
        return toJsonObject().toString()
    }

    private fun toJsonObject(): JSONObject {
        val map = mutableMapOf(
            "bssid" to bssid,
            "ssid" to ssid,
            "level" to level,
            "timestamp" to timestamp
        )
        return JSONObject(map as Map<*, *>)
    }

}