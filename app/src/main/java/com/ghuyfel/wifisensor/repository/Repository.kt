package com.ghuyfel.wifisensor.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ghuyfel.wifisensor.api.ApiService
import com.ghuyfel.wifisensor.exceptions.ApiPostDataFailedException
import com.ghuyfel.wifisensor.repository.models.ApiResponse
import com.ghuyfel.wifisensor.utils.DeviceId
import com.ghuyfel.wifisensor.api.models.params.WifiReadingsRequestBody
import com.ghuyfel.wifisensor.sensor.models.WifiSensorReading
import com.ghuyfel.wifisensor.utils.DataState
import com.ghuyfel.wifisensor.utils.toContentText
import com.ghuyfel.wifisensor.utils.toJsonString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Repository(private val apiService: ApiService, private val deviceId: DeviceId) {

    private val _readingsReceivedLiveData: MutableLiveData<String> = MutableLiveData()
    val readingsReceivedLiveData: LiveData<String>
        get() = _readingsReceivedLiveData

    private val _apiResponse = MutableLiveData<DataState<ApiResponse>>()
    val apiResponse: LiveData<DataState<ApiResponse>>
        get() = _apiResponse

    fun sendReadings(dataState: DataState<List<WifiSensorReading>>) {
        GlobalScope.launch(Dispatchers.IO) {
            when (dataState) {
                is DataState.Error -> {
                    _readingsReceivedLiveData.postValue(dataState.e.toString())
                    val requestBody =
                        WifiReadingsRequestBody(result = false, message = dataState.e.message, userId = deviceId.userId)
                    postData(requestBody)
                }
                is DataState.Success -> {
                    _readingsReceivedLiveData.postValue(dataState.data.toContentText())
                    val requestBody =
                        WifiReadingsRequestBody(result = true, data = dataState.data.toJsonString(), userId = deviceId.userId)
                    postData(requestBody)
                }
            }
        }
    }


    private suspend fun postData(requestBody: WifiReadingsRequestBody) {
        Log.i("TAG", "postData: $requestBody")

        try {
            val response = apiService.post(requestBody)
            if (response.isSuccessful) {
                val postResponse = response.body()
                if (postResponse == null)
                    _apiResponse.postValue(DataState.Error(Exception("Body is empty")))
                else
                    _apiResponse.postValue(
                        DataState.Success(ApiResponse(postResponse.message, postResponse.result))
                    )
            } else {
                Log.e("TAG", response.errorBody().toString())
                //depending on the error, we could cache the data and resend later.
                _apiResponse.postValue( DataState.Error(ApiPostDataFailedException(response.toString())))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _apiResponse.postValue( DataState.Error(ApiPostDataFailedException(e.toString())))
        }
    }
}