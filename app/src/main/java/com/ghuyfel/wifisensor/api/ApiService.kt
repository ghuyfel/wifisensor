package com.ghuyfel.wifisensor.api

import com.ghuyfel.wifisensor.api.models.responses.PostWifiReadingsResponse
import com.ghuyfel.wifisensor.api.models.params.WifiReadingsRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("readings")
    suspend fun post(@Body readings: WifiReadingsRequestBody): Response<PostWifiReadingsResponse>
}