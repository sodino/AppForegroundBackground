package com.sodino.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

open class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View) {
        var clazz : Class<out Activity> = SecondActivity::class.java
        when(v.id) {
            R.id.btnStartMain -> {
                clazz = MainActivity::class.java
            }
            R.id.btnStartFinish -> {
                clazz = FinishActivity::class.java
            }
            R.id.btnStartTranslucent -> {
                clazz = TranslucentActivity::class.java
            }
            else -> {
//            R.id.btnStartSecond -> {}
                // do nothing
            }
        }

        val intent = Intent(this, clazz)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("MainActivity")

        btnStartMain.setOnClickListener(this)
        btnStartSecond.setOnClickListener(this)
        btnStartFinish.setOnClickListener(this)
        btnStartTranslucent.setOnClickListener(this)
    }
}
