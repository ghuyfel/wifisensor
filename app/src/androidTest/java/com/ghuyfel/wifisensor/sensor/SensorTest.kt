package com.ghuyfel.wifisensor.sensor

import android.net.wifi.WifiManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.ghuyfel.wifisensor.di.AppModule
import com.ghuyfel.wifisensor.exceptions.NoLocationPermissionException
import com.ghuyfel.wifisensor.exceptions.WifiDisabledException
import com.ghuyfel.wifisensor.utils.DataState
import com.ghuyfel.wifisensor.utils.WifiPermissionHelper
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
@UninstallModules(AppModule::class)
class SensorTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var sensor: Sensor
    @Inject
    lateinit var wifiManager: WifiManager

    @Before
    fun setUp() {
        mockkObject(WifiPermissionHelper)
        every { WifiPermissionHelper.isPermissionGranted(any()) } returns false
        hiltAndroidRule.inject()
    }

    @Test
    fun test_locationPermissionNotGranted_ReturnsErrorState() {
        every { WifiPermissionHelper.isPermissionGranted(any()) } returns false

        //when
        val result = sensor.getWifiScanResult()
        //then

        // We should break these assertions in two separate test cases
        // This is for demo purposes only
        assertThat(result).isInstanceOf(DataState.Error::class.java)
        assertThat((result as DataState.Error).e).isInstanceOf(NoLocationPermissionException::class.java)
    }

    @Test
    fun test_locationPermissionGranted_ChecksIfWifiIsEnabled() {
        every { WifiPermissionHelper.isPermissionGranted(any()) } returns true
        every { wifiManager.isWifiEnabled } returns false
        //when
        sensor.getWifiScanResult()
        //then
        verify { wifiManager.isWifiEnabled } //This is used as an assert statement
    }

    @Test
    fun test_locationPermissionGrantedAndIfWifiIsNotEnabled_ReturnsError() {
        every { WifiPermissionHelper.isPermissionGranted(any()) } returns true
        every { wifiManager.isWifiEnabled } returns false
        //when
        val result = sensor.getWifiScanResult()
        //then
        // We should break these assertions in two separate test cases
        // This is for demo purposes only
        assertThat(result).isInstanceOf(DataState.Error::class.java)
        assertThat((result as DataState.Error).e).isInstanceOf(WifiDisabledException::class.java)
    }

    @Test
    fun test_locationPermissionGrantedAndWifiIsEnabled_ReturnsSuccess() {
        every { WifiPermissionHelper.isPermissionGranted(any()) } returns true
        every { wifiManager.isWifiEnabled } returns true
        every { wifiManager.scanResults } returns emptyList() // we can also use fakes here
        //when
        val result = sensor.getWifiScanResult()
        //then
        verify { wifiManager.scanResults }
        // We should break these assertions in two separate test cases
        // This is for demo purposes only
        assertThat(result).isInstanceOf(DataState.Success::class.java)
        assertThat((result as DataState.Success).data).isInstanceOf(List::class.java)
    }
}