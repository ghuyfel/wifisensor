package com.ghuyfel.wifisensor.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

object WifiPermissionHelper {
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_WIFI_STATE
    )

    fun isPermissionGranted(context: Context) = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CHANGE_WIFI_STATE
    ) == PackageManager.PERMISSION_GRANTED  && ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_WIFI_STATE
    ) == PackageManager.PERMISSION_GRANTED

    fun shouldShowRationale(activity: Activity) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)  ||
        activity.shouldShowRequestPermissionRationale(Manifest.permission.CHANGE_WIFI_STATE) ||
        activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_WIFI_STATE)
    } else {
        false
    }

    fun requestLocationPermissions(launcher: ActivityResultLauncher<Array<String>>) {
        launcher.launch(permissions)
    }

}