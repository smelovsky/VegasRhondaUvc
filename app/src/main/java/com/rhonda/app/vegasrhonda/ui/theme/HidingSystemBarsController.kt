package com.rhonda.ui.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.rhonda.ui.common.AndroidUtils.getActivity

object HidingSystemBarsController {

    private val classTag = this::class.java.simpleName

    var fullscreenMode by mutableStateOf(false)
        private set

    var barsVisible by mutableStateOf(true)
        private set

    @Volatile
    private var savedValues: SavedValues? = null


    fun enterFullscreen(activityContext: Context, view: View, withHide: Boolean = true) {
        if (fullscreenMode) return
        getActivity(activityContext)?.let { activity ->
            savedValues = getSavedValues(activity, view)
            enter(activity, view)
            if (withHide) {
                hideBars(activity, view)
            }
            fullscreenMode = true
            Log.d(classTag, "enter fullscreen")
        }
    }

    fun exitFullscreen(activityContext: Context, view: View) {
        if (!fullscreenMode) return
        getActivity(activityContext)?.let { activity ->
            showBars(activity, view)
            exit(activity, view)
            savedValues?.let { values ->
                restoreSavedValues(activity, view, values)
            }
            fullscreenMode = false
            Log.d(classTag, "exit fullscreen")
        }
    }

    fun showBars(activityContext: Context, view: View) {
        getActivity(activityContext)?.let { activity ->
            show(activity, view)
            barsVisible = true
            Log.d(classTag, "show system bars")
        }
    }

    fun hideBars(activityContext: Context, view: View) {
        getActivity(activityContext)?.let { activity ->
            hide(activity, view)
            barsVisible = false
            Log.d(classTag, "hide system bars")
        }
    }
}

data class SavedValues(val systemBarsBehavior: Int, val systemUiVisibility: Int)

@Suppress("DEPRECATION")
private val BASE_VISIBILITY = View.SYSTEM_UI_FLAG_LAYOUT_STABLE

@Suppress("DEPRECATION")
private val SHOW_VISIBILITY = BASE_VISIBILITY or
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

@Suppress("DEPRECATION")
private val HIDE_VISIBILITY = SHOW_VISIBILITY or
        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LOW_PROFILE or
        View.SYSTEM_UI_FLAG_IMMERSIVE or
        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

@Suppress("DEPRECATION")
private fun getSavedValues(activity: Activity, view: View): SavedValues {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        SavedValues(view.windowInsetsController?.systemBarsBehavior ?: 0, 0)
    } else {
        SavedValues(0, activity.window.decorView.systemUiVisibility)
    }
}

@Suppress("DEPRECATION")
private fun restoreSavedValues(activity: Activity, view: View, savedValues: SavedValues) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        view.windowInsetsController?.apply {
            systemBarsBehavior = savedValues.systemBarsBehavior
        }
    } else {
        activity.window.decorView.systemUiVisibility = savedValues.systemUiVisibility
    }
}

@Suppress("DEPRECATION")
private fun enter(activity: Activity, view: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        view.windowInsetsController?.apply {
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        activity.window.decorView.systemUiVisibility = SHOW_VISIBILITY
    }
}

@Suppress("DEPRECATION")
private fun exit(activity: Activity, view: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        view.windowInsetsController?.apply {
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_TOUCH
        }
    } else {
        activity.window.decorView.systemUiVisibility = 0
    }
}

@Suppress("DEPRECATION")
private fun hide(activity: Activity, view: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        view.windowInsetsController?.hide(WindowInsets.Type.systemBars())
    } else {
        activity.window.decorView.systemUiVisibility = HIDE_VISIBILITY
    }
}

@Suppress("DEPRECATION")
private fun show(activity: Activity, view: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        view.windowInsetsController?.show(WindowInsets.Type.systemBars())
    } else {
        activity.window.decorView.systemUiVisibility = SHOW_VISIBILITY
    }
}
