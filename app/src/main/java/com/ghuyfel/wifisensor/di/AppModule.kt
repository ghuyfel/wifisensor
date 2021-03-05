package com.ghuyfel.wifisensor.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.net.wifi.WifiManager
import com.ghuyfel.wifisensor.api.ApiService
import com.ghuyfel.wifisensor.handlers.AlarmHandler
import com.ghuyfel.wifisensor.handlers.NotificationHandler
import com.ghuyfel.wifisensor.handlers.ReadingsHandler
import com.ghuyfel.wifisensor.receivers.SensorBroadcastReceiver
import com.ghuyfel.wifisensor.repository.Repository
import com.ghuyfel.wifisensor.sensor.Sensor
import com.ghuyfel.wifisensor.utils.Constants
import com.ghuyfel.wifisensor.utils.DeviceId
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideWifiManager(@ApplicationContext context: Context): WifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager =
        context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager =
        context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @Singleton
    fun provideNotificationHandler(
        @ApplicationContext context: Context,
        notificationManager: NotificationManager
    ): NotificationHandler =
        NotificationHandler(context = context.applicationContext, notificationManager)

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
    ): ReadingsHandler =
        ReadingsHandler(notificationHandler, repository)

    @Provides
    @Singleton
    fun provideRepository(apiService: ApiService, deviceId: DeviceId): Repository = Repository(apiService, deviceId)

    @Provides
    @Singleton
    fun provideSensor(@ApplicationContext context: Context, wifiManager: WifiManager) : Sensor =
        Sensor(context.applicationContext, wifiManager)

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Provides
    @Singleton
    fun provideApiService(retrofitBuilder: Retrofit.Builder): ApiService {
        return retrofitBuilder
            .baseUrl(Constants.BASE_URL_API_SERVICE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDeviceId(): DeviceId {
        //For demo purpose. This should be unique and stored. Or use Ads API to get a unique ID.
        return DeviceId(UUID.randomUUID().toString())
    }

    @Provides
    @Singleton
    fun provideSensorBroadCastReceiver(sensor: Sensor, readingsHandler: ReadingsHandler): SensorBroadcastReceiver {
        val sensorBroadcastReceiver = SensorBroadcastReceiver()
        sensorBroadcastReceiver.readingsHandler = readingsHandler
        sensorBroadcastReceiver.sensor = sensor
        return sensorBroadcastReceiver
    }
}