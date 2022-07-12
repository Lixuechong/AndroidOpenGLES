package com.example.androidopengles.image

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.example.androidopengles.filter.AFilter
import java.io.IOException


/**
 * Created by XC.Li
 * desc:
 */
class SGLView(context: Context, attributes: AttributeSet?) : GLSurfaceView(context, attributes) {

    private var render: SGLRender? = null

    init {
        init()
    }

    private fun init() {
        setEGLContextClientVersion(2)
        render = SGLRender(this)
        setRenderer(render)
        renderMode = RENDERMODE_WHEN_DIRTY
        try {
            render!!.setImage(BitmapFactory.decodeStream(resources.assets.open("texture/fengj.png")))
            requestRender()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getRender(): SGLRender? {
        return render
    }

    fun setFilter(filter: AFilter?) {
        render!!.setFilter(filter)
    }

}