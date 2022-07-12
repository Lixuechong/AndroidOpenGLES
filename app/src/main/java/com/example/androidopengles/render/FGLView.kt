package com.example.androidopengles.render

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

/**
 * Created by XC.Li
 * desc:
 */
class FGLView(context: Context, attributes: AttributeSet?) : GLSurfaceView(context, attributes) {

    private var renderer: FGLRender? = null

    init {
        setEGLContextClientVersion(2)
        setRenderer(FGLRender(this).also { renderer = it })
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    fun setShape(clazz: Class<out Shape?>?) {
        try {
            renderer?.setShape(clazz)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}