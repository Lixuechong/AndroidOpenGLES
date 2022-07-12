package com.example.androidopengles.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by XC.Li
 * desc:
 */
open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "OpenGL Demo"
    }
}