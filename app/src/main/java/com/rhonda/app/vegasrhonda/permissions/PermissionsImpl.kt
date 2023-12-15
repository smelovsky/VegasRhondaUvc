package com.rhonda.app.vegasrhonda.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rhonda.app.vegasrhonda.mainViewModel


val basePermissions = arrayOf(
    Manifest.permission.INTERNET,
    Manifest.permission.CAMERA,
    Manifest.permission.WAKE_LOCK,
)

data class PermissionsViewState(
    val INTERNET: Boolean = false,
    val CAMERA: Boolean = false,
    val WAKE_LOCK: Boolean = false,

    val permissionsGranted: Boolean = false,
)

class PermissionsImpl(val context: Context): PermissionsApi {

    private fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }

    override fun hasAllPermissions(activity: Activity): Boolean{

        var result = true

        if (!hasBasePermissions(activity)) {
            result = false
        }

        mainViewModel.permissionsViewState.value =
            mainViewModel.permissionsViewState.value.copy(permissionsGranted = result)

        return result
    }

    override fun hasBasePermissions(activity: Activity): Boolean{
        var result = true
        basePermissions.forEach {

            val permission = ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            if ( !permission)
            {
                result = false
            }
            when (it) {

                Manifest.permission.INTERNET -> mainViewModel.permissionsViewState.value =
                    mainViewModel.permissionsViewState.value.copy(INTERNET = permission)

                Manifest.permission.WAKE_LOCK -> mainViewModel.permissionsViewState.value =
                    mainViewModel.permissionsViewState.value.copy(WAKE_LOCK = permission)

                Manifest.permission.CAMERA -> mainViewModel.permissionsViewState.value =
                    mainViewModel.permissionsViewState.value.copy(CAMERA = permission)

            }
        }

        return result
    }

    override fun requestBasePermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity, basePermissions,101)
    }

}