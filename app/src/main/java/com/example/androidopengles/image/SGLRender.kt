package com.example.androidopengles.image

import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.view.View
import com.example.androidopengles.filter.AFilter
import com.example.androidopengles.filter.ColorFilter
import com.example.androidopengles.filter.ContrastColorFilter
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * Created by XC.Li
 * desc:
 */
class SGLRender(private val mView: View) : GLSurfaceView.Renderer {

    private lateinit var mFilter: AFilter
    private var bitmap: Bitmap? = null
    private var width = 0
    private  var height:Int = 0
    private var refreshFlag = false
    private var config: EGLConfig? = null

    init {
        mFilter = ContrastColorFilter(mView.context, ColorFilter.Filter.NONE)
    }

    fun setFilter(filter: AFilter?) {
        refreshFlag = true
        mFilter = filter!!
        if (bitmap != null) {
            mFilter.setBitmap(bitmap)
        }
    }

    fun setImageBuffer(buffer: IntArray?, width: Int, height: Int) {
        bitmap = Bitmap.createBitmap(buffer!!, width, height, Bitmap.Config.RGB_565)
        mFilter.setBitmap(bitmap)
    }

    fun refresh() {
        refreshFlag = true
    }

    fun getFilter(): AFilter? {
        return mFilter
    }

    fun setImage(bitmap: Bitmap?) {
        this.bitmap = bitmap
        mFilter.setBitmap(bitmap)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        this.config = config
        mFilter.onSurfaceCreated(gl, config)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
        mFilter.onSurfaceChanged(gl, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        if (refreshFlag && width != 0 && height != 0) {
            mFilter.onSurfaceCreated(gl, config)
            mFilter.onSurfaceChanged(gl, width, height)
            refreshFlag = false
        }
        mFilter.onDrawFrame(gl)
    }

}