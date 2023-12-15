package com.rhonda.ui.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.DisplayCutout
import android.view.View

object AndroidUtils {

    fun getActivity(context: Context?): Activity? {
        context?.let {
            if (context is ContextWrapper) {
                return if (context is Activity) {
                    context
                } else {
                    getActivity(context.baseContext)
                }
            }
        }
        return null
    }

}
