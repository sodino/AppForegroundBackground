package com.sodino.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SecondActivity : MainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        setTitle("SecondActivity")
    }
}
