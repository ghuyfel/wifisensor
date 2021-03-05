package com.ghuyfel.wifisensor.api.models.params

data class WifiReadingsRequestBody(
    val result: Boolean,
    val data: String? = null,
    val message: String? = "Reading succeeded.",
    val userId: String
)