package com.example.qurannative.permissions

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager

@Composable
fun NotificationPermissionHandler(
    requestOnLaunch: Boolean = false,
    onPermissionResult: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        onPermissionResult(granted)
    }

    val requestPermission = remember {
        {
            if (Build.VERSION.SDK_INT >= 33) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                onPermissionResult(true)
            }
        }
    }

    LaunchedEffect(requestOnLaunch) {
        if (requestOnLaunch && Build.VERSION.SDK_INT >= 33) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                requestPermission()
            } else {
                onPermissionResult(true)
            }
        }
    }
}
