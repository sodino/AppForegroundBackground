package com.sodino.app

import android.app.Activity
import android.view.Window
import android.widget.Toast

import java.lang.reflect.Method

object Test {
    fun isTranslucentActivity(activity: Activity): Boolean {
        var result = false
        try {
            val targetClassName = "com.android.internal.policy.PhoneWindow"
            val window = activity.window
            if (window.javaClass.name == targetClassName) {
                // 第一种方法：
                val methodName = "isTranslucent"
                val method = window.javaClass.getDeclaredMethod(methodName, *arrayOf())
                result = method.invoke(window, *arrayOf()) as Boolean


//                // 第二种方法：
//                val fieldName = "mIsTranslucent"
//                val field = window.javaClass.getDeclaredField(fieldName)
//                field.isAccessible = true
//                result = field.get(window) as Boolean

                // 系统自己的实现： com.android.internal.policy.PhoneWindow
//                TypedArray a = getWindowStyle(
//                        mWindowStyle = mContext.obtainStyledAttributes(com.android.internal.R.styleable.Window);
//                        return mWindowStyle;
//                );
//
//                mIsTranslucent = a.getBoolean(R.styleable.Window_windowIsTranslucent, false);
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }

        return result
    }
}
