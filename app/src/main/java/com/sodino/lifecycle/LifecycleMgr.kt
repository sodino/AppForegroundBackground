package com.sodino.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.sodino.app.Test
import java.util.*

class LifecycleMgr {
    companion object {
        val callbacks           = LinkedList<Application.ActivityLifecycleCallbacks>()
        var activityCount       = 0
        var resumeCount         = 0


        open fun isTranslucentActivity(activity : Activity) : Boolean {
//            var result = false
//            try {
//                val targetClassName = "com.android.internal.policy.PhoneWindow";
//                val window = activity.window
//                if (window.javaClass.name.equals(targetClassName)) {
//                    val methodName = "isTranslucent"
//                    val method = window.javaClass.getDeclaredMethod(methodName, null)
//                    result = method.invoke(window, arrayOf<Any>()) as Boolean
//                }
//            } catch(t : Throwable) {
//                t.printStackTrace()
//            }
//            return result
            return Test.isTranslucentActivity(activity)
        }

        val defaultCallback : Application.ActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
            var lastResumeActivity : String = ""

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
                val currentActivityString = activity.toString()
                resumeCount ++

                Log.d("Test", "onActivityResumed -> " + activity.toString() + " resumeCount=" + resumeCount)
                for (callback in callbacks) {
                    if (resumeCount == 1 && callback is MyActivityLifecycleCallback) {
                        callback.onAppForeground(activity)
                    }
                    callback.onActivityResumed(activity)
                }


                // 处理多Activity交叉调用时未完整执行生命周期方法的纠正
                if ((currentActivityString == lastResumeActivity  // 跳转的Activity刚onCreate或onStart还没到onResume就直接finish，则会之前的Activity会再次调用onResume
                            && resumeCount > 1)                   // > 1表示app连接跳转。要排除掉单一Activity回桌面再回前台的场景
                    || isTranslucentActivity(activity)            // 透明的Activity显示后，下层的Activity不会调用onStop()，过滤掉透明的Activity
                ) {
                    resumeCount = Math.max(1, resumeCount - 1)    // 有resume，就至少为1。避免单个TranslucentActivity桌面来回切换导致负数。
                    Log.d("Test", "onActivityResumed -> " + activity.toString() + " correction: resumeCount=" + resumeCount)
                }
                lastResumeActivity = currentActivityString
            }

            override fun onActivityPaused(activity: Activity) {
                Log.d("Test", "onActivityPaused -> " + activity.toString())

                for (callback in callbacks) {
                    callback.onActivityPaused(activity)
                }
            }

            override fun onActivityStopped(activity: Activity) {
                resumeCount --;

                Log.d("Test", "onActivityStopped -> " + activity.toString() + " resumeCount=" + resumeCount)

                for (callback in callbacks) {
                    if (resumeCount == 0 && callback is MyActivityLifecycleCallback) {
                        callback.onAppBackground(activity)
                    }

                    callback.onActivityStopped(activity)
                }
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