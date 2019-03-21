package com.sodino.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle

interface MyActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {
    fun onAppFirstActivityCreated(activity: Activity, savedInstanceState: Bundle?) : Boolean
    fun onAppForeground(activity: Activity)
    fun onAppBackground(activity: Activity)
}