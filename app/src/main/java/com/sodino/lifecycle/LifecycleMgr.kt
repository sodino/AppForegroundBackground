package com.sodino.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.*

class LifecycleMgr {
    data class Result(val change : Boolean , val size : Int)

    companion object {
        val callbacks                               = LinkedList<Application.ActivityLifecycleCallbacks>()
        var activityCount                           = 0
        var listResumeActivity                      = LinkedList<String>()

        val defaultCallback : Application.ActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Log.d("Test", "onActivityCreated -> " + activity.toString())

                activityCount ++
                for (callback in callbacks) {
                    var consume = false;
                    if (activityCount == 1 && callback is MyActivityLifecycleCallback) {
                        consume = callback.onAppFirstActivityCreated(activity, savedInstanceState)
                    }
                    if (!consume) {
                        callback.onActivityCreated(activity, savedInstanceState);
                    }
                }
            }

            override fun onActivityStarted(activity: Activity) {
                Log.d("Test", "onActivityStarted -> " + activity.toString())

                for (callback in callbacks) {
                    callback.onActivityStarted(activity)
                }
            }

            override fun onActivityResumed(activity: Activity) {
                val activityString = activity.toString()
                val (change, size) = recordActivityResume(activityString)
                Log.d("Test", "onActivityResumed -> " + activityString + " change=" + change + " resumeCount=" + size)
                for (callback in callbacks) {
                    if (change && size == 1 && callback is MyActivityLifecycleCallback) {
                        callback.onAppForeground(activity)
                    }
                    callback.onActivityResumed(activity)
                }
            }

            private fun recordActivityResume(activityString: String) : Result {
                var change = false
                if (!listResumeActivity.contains(activityString)) {
                    listResumeActivity.add(activityString)
                    change = true;
                }
                return Result(change, listResumeActivity.size)
            }

            override fun onActivityPaused(activity: Activity) {
                Log.d("Test", "onActivityPaused -> " + activity.toString())

                for (callback in callbacks) {
                    callback.onActivityPaused(activity)
                }
            }

            override fun onActivityStopped(activity: Activity) {
                val activityString = activity.toString()
                val (change, size) = wipeActivityResume(activityString)
                Log.d("Test", "onActivityStopped -> " + activityString + " change=" + change + " resumeCount=" + size)

                for (callback in callbacks) {
                    if (change && size == 0 && callback is MyActivityLifecycleCallback) {
                        callback.onAppBackground(activity)
                    }

                    callback.onActivityStopped(activity)
                }
            }

            private fun wipeActivityResume(activityString: String) : Result {
                var change = listResumeActivity.remove(activityString)
                return Result(change, listResumeActivity.size)
            }

            override fun onActivityDestroyed(activity: Activity) {
                Log.d("Test", "onActivityDestroyed -> " + activity.toString())

                for (callback in callbacks) {
                    callback.onActivityDestroyed(activity)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
                for (callback in callbacks) {
                    callback.onActivitySaveInstanceState(activity, outState)
                }
            }

        }


        public fun registerActivityLifecycleCallback(callback : Application.ActivityLifecycleCallbacks) {
            if (Thread.currentThread() == Looper.getMainLooper().thread) {
                callbacks.add(callback)
            } else {
                uiThreadAction(true, callback)
            }
        }

        public fun unregisterActivityLifecycleCallback(callback : Application.ActivityLifecycleCallbacks) {
            if (Thread.currentThread() == Looper.getMainLooper().thread) {
                callbacks.remove(callback)
            } else {
                uiThreadAction(false, callback)
            }
        }


        private fun uiThreadAction(isRegister : Boolean, callback : Application.ActivityLifecycleCallbacks) {
            Handler(Looper.getMainLooper()).post {
                if (isRegister) {
                    callbacks.add(callback)
                } else {
                    callbacks.remove(callback)
                }
            }
        }
    }

}