package com.ghuyfel.wifisensor.handlers

import com.ghuyfel.wifisensor.repository.Repository
import com.ghuyfel.wifisensor.sensor.models.WifiSensorReading
import com.ghuyfel.wifisensor.utils.DataState

class ReadingsHandler(
    private val notificationHandler: NotificationHandler,
    private val repository: Repository
) {

    fun handleReadings(readingsState: DataState<List<WifiSensorReading>>) {

        repository.sendReadings(readingsState)

        when(readingsState) {
            is DataState.Error -> readingsState.e.message?.let {
                notificationHandler.notifyFailure(
                    it
                )
            }

            is DataState.Success -> {
                notificationHandler.notifySuccess(readingsState.data)
            }

        }
    }
}