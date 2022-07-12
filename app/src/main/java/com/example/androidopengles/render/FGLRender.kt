package com.example.androidopengles.render

import android.opengl.GLES20
import android.util.Log
import android.view.View
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * Created by XC.Li
 * desc:
 */
class FGLRender(view: View): Shape(view) {

    private var shape: Shape? = null
    private var clazz: Class<out Shape> = Cube::class.java

    fun setShape(shape: Class<out Shape?>?) {
        clazz = shape!!
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        Log.e("wuwang", "onSurfaceCreated")
        try {
            val constructor = clazz.getDeclaredConstructor(View::class.java)
            constructor.isAccessible = true
            shape = constructor.newInstance(mView)
        } catch (e: Exception) {
            e.printStackTrace()
            shape = Cube(mView!!)
        }
        shape!!.onSurfaceCreated(gl, config)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        Log.e("wuwang", "onSurfaceChanged")
        GLES20.glViewport(0, 0, width, height)
        shape!!.onSurfaceChanged(gl, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        Log.e("wuwang", "onDrawFrame")
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        shape!!.onDrawFrame(gl)
    }


}