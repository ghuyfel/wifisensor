package com.ghuyfel.wifisensor.exceptions

class ApiPostDataFailedException(error: String): Exception("Failed to send readings: $error")