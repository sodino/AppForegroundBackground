package com.sodino.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class FinishActivity : MainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        setTitle("FinishActivity")
        finish()
    }
}
