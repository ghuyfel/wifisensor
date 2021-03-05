package com.ghuyfel.wifisensor.ui.events

sealed class MainEvents {
    object StartListening : MainEvents()
    object StopListening: MainEvents()
}