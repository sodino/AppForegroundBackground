监测App切换回前台或切到后台。
```
interface MyActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {
    fun onAppFirstActivityCreated(activity: Activity, savedInstanceState: Bundle?) : Boolean
    fun onAppForeground(activity: Activity)
    fun onAppBackground(activity: Activity)
}
```
要考虑一些例外的情况，如：
1. TranslucentActivity onResume时，它的上一个Activity并不会调用 onStop
2. 某些Activity在onCreate 或 onStart阶段还未执行onResume就finish掉了