package com.sodino.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView

// 使用android:theme="@android:style/Theme.Translucent.NoTitleBar"要对应系统的android.app.Activity
class TranslucentActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val txtView = TextView(this)
        txtView.text = "TranslucentActivity"
        txtView.textSize = 100.0f
        setContentView(txtView)
    }

    override fun onResume() {
        super.onResume()
        finish()

        startActivity(Intent(this, MainActivity::class.java))
    }
}
