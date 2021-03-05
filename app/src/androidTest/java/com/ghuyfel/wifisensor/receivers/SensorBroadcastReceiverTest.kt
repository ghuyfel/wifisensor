package com.ghuyfel.wifisensor.receivers

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.ghuyfel.wifisensor.di.TestAppModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.mockkObject
import io.mockk.verifyAll
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@HiltAndroidTest
@SmallTest
@UninstallModules(TestAppModule::class)
class SensorBroadcastReceiverTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var sensorBroadcastReceiver: SensorBroadcastReceiver


    @Before
    fun setUp() {
        hiltRule.inject()
    }

    //We assume that the Receiver will receive the broadcast
    //from AlarmManager. This would have been interesting if we were passing extras in the intent.
    @Test
    fun test_onReceive() {
        val intent = Intent()
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        mockkObject(sensorBroadcastReceiver.readingsHandler)
        mockkObject(sensorBroadcastReceiver.sensor)

        sensorBroadcastReceiver.onReceive(context, intent)

        verifyAll {
            sensorBroadcastReceiver.sensor.getWifiScanResult()
            sensorBroadcastReceiver.readingsHandler.handleReadings(any())
        }
    }
}