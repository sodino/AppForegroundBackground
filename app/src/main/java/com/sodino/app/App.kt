package com.sodino.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.sodino.lifecycle.LifecycleMgr
import com.sodino.lifecycle.MyActivityLifecycleCallback

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        super.registerActivityLifecycleCallbacks(LifecycleMgr.defaultCallback)
        registerActivityLifecycleCallbacks(object: MyActivityLifecycleCallback {
            override fun onAppFirstActivityCreated(activity: Activity, savedInstanceState: Bundle?): Boolean {
                Log.d("Test", "app first activity created")
                return false
            }

            override fun onAppForeground(activity: Activity) {
                Log.d("Test", "app turn to foreground")
            }

            override fun onAppBackground(activity: Activity) {
                Log.d("Test", "app turn to background")
            }

            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            }

        })
    }

    override fun registerActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks) {
        LifecycleMgr.registerActivityLifecycleCallback(callback);
    }

    override fun unregisterActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks) {
        LifecycleMgr.unregisterActivityLifecycleCallback(callback)
    }
}