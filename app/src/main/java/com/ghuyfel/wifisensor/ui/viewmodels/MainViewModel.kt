package com.ghuyfel.wifisensor.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ghuyfel.wifisensor.handlers.AlarmHandler
import com.ghuyfel.wifisensor.repository.Repository
import com.ghuyfel.wifisensor.ui.events.MainEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val alarmHandler: AlarmHandler): ViewModel() {
    val readingsLiveData = MutableLiveData<String>()
    init {
        repository.readingsReceivedLiveData.observeForever {
            readingsLiveData.value = it // just in case we have to do some work on the data before sending to the ui
        }
    }

    fun setEvent(event: MainEvents) {
        when(event) {
            MainEvents.StartListening -> alarmHandler.startSensorReadingService()
            MainEvents.StopListening -> alarmHandler.stopSensorReadingService()
        }
    }
}