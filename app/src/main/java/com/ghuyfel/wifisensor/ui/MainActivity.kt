package com.ghuyfel.wifisensor.ui

import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ghuyfel.wifisensor.R
import com.ghuyfel.wifisensor.databinding.LayoutLogsBinding
import com.ghuyfel.wifisensor.receivers.SensorBroadcastReceiver
import com.ghuyfel.wifisensor.ui.events.MainEvents
import com.ghuyfel.wifisensor.ui.viewmodels.MainViewModel
import com.ghuyfel.wifisensor.utils.WifiPermissionHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ActivityResultCallback<Map<String, Boolean>> {
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<Array<String>>


     val viewModel: MainViewModel by viewModels()

    lateinit var binding: LayoutLogsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutLogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(), this
        )

        updateButtons(false)

        if(!WifiPermissionHelper.isPermissionGranted(this)) {
            WifiPermissionHelper.requestLocationPermissions(locationPermissionLauncher)
        } else if(WifiPermissionHelper.shouldShowRationale(this)) {
            showRationaleDialog()
        } else {
            updateButtons(true)
        }

        binding.btStop.setOnClickListener {
            Toast.makeText(this, R.string.service_stopped, Toast.LENGTH_LONG).show()
            viewModel.setEvent(MainEvents.StopListening)
        }

        binding.btStart.setOnClickListener {
            Toast.makeText(this, R.string.service_started, Toast.LENGTH_LONG).show()
            viewModel.setEvent(MainEvents.StartListening)
        }

        viewModel.readingsLiveData.observeForever {
            val sb = StringBuilder()
            sb.append(it)
            sb.append("\n\n")
            sb.append(binding.tvLogs.text)
            binding.tvLogs.text = sb.toString()
        }
    }

    private fun showRationaleDialog() {
        MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setTitle(R.string.permission_needed)
            .setMessage(R.string.permission_denied_message)
            .setPositiveButton(R.string.request_permission) { dialog, _ ->
                run {
                    WifiPermissionHelper.requestLocationPermissions(locationPermissionLauncher)
                    dialog.cancel()
                }
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                run {
                    dialog.cancel()
                }
            }
            .show()
    }

    override fun onActivityResult(results: Map<String, Boolean>) {
        var startService = true
        for (success in results.values) {
            if (!success) {
                startService = false
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.permission_denied)
                    .setMessage(R.string.permission_denied_message)
                    .setPositiveButton(R.string.request_permission) { dialog, _ ->
                        WifiPermissionHelper.requestLocationPermissions(
                            locationPermissionLauncher
                        )
                        dialog.cancel()
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        updateButtons(false)
                        dialog.cancel()

                    }.show()

                break
            }
        }

        if(startService) updateButtons(true)
    }

    private fun updateButtons(enable: Boolean) {
        binding.btStart.isEnabled  = enable
        binding.btStop.isEnabled = enable
    }

}