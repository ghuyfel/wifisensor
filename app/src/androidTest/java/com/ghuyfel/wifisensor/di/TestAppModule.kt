package com.ghuyfel.wifisensor.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.net.wifi.WifiManager
import android.provider.Settings
import com.ghuyfel.wifisensor.api.ApiService
import com.ghuyfel.wifisensor.handlers.AlarmHandler
import com.ghuyfel.wifisensor.handlers.NotificationHandler
import com.ghuyfel.wifisensor.handlers.ReadingsHandler
import com.ghuyfel.wifisensor.receivers.SensorBroadcastReceiver
import com.ghuyfel.wifisensor.repository.Repository
import com.ghuyfel.wifisensor.sensor.Sensor
import com.ghuyfel.wifisensor.utils.DeviceId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import io.mockk.mockkObject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TestAppModule {

    @Provides
    @Singleton
    fun provideWifiManager(): WifiManager = mockk()

    @Provides
    @Singleton
    fun provideNotificationManager(): NotificationManager = mockk()

    @Provides
    @Singleton
    fun provideRepository(apiService: ApiService, deviceId: DeviceId): Repository = Repository(apiService, deviceId)

    @Provides
    @Singleton
    fun provideApiService(): ApiService = mockk()

    @Provides
    @Singleton
    fun provideAlarmManager(): AlarmManager = mockk()

    @Provides
    @Singleton
    fun provideAlarmHandler(
        @ApplicationContext context: Context,
        alarmManager: AlarmManager
    ): AlarmHandler =
        AlarmHandler(context.applicationContext, alarmManager)

    @Provides
    @Singleton
    fun provideReadingsHandler(
        notificationHandler: NotificationHandler,
        repository: Repository
    ): ReadingsHandler {
        val readingsHandler = ReadingsHandler(notificationHandler, repository)
        mockkObject(readingsHandler)
        return readingsHandler
    }

    @Provides
    @Singleton
    fun provideNotificationHandler(@ApplicationContext context: Context,
                                   notificationManager: NotificationManager): NotificationHandler =
        NotificationHandler(context,notificationManager)

    @Provides
    @Singleton
    fun provideDeviceId(@ApplicationContext context: Context): DeviceId {
        val userId: String = Settings.Secure.getString(context.applicationContext.getContentResolver(),
            Settings.Secure.ANDROID_ID)
        return DeviceId(userId)
    }

    @Provides
    @Singleton
    fun provideSensor(@ApplicationContext context: Context, wifiManager: WifiManager): Sensor =
        Sensor(context, wifiManager)


    @Provides
    @Singleton
    fun provideSensorReceiver(sensor: Sensor, readingsHandler: ReadingsHandler): SensorBroadcastReceiver = mockk()
}