package com.example.androidopengles.filter

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import android.opengl.Matrix
import com.example.androidopengles.util.ShaderUtils.createProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * Created by XC.Li
 * desc:
 */
abstract class AFilter(context: Context, vertex: String, fragment: String) {
    private var mContext: Context? = null
    private var mProgram = 0
    private var glHPosition = 0
    private var glHTexture = 0
    private var glHCoordinate = 0
    private var glHMatrix = 0
    private var hIsHalf = 0
    private var glHUxy = 0
    private var mBitmap: Bitmap? = null

    private var bPos: FloatBuffer? = null
    private var bCoord: FloatBuffer? = null

    private var textureId = 0
    private var isHalf = false

    private var uXY = 0f

    private var vertex: String? = null
    private var fragment: String? = null
    private val mViewMatrix = FloatArray(16)
    private val mProjectMatrix = FloatArray(16)
    private val mMVPMatrix = FloatArray(16)

    private val sPos = floatArrayOf(
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f
    )

    private val sCoord = floatArrayOf(
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f)

    init {
        mContext = context
        this.vertex = vertex
        this.fragment = fragment
        val bb = ByteBuffer.allocateDirect(sPos.size * 4)
        bb.order(ByteOrder.nativeOrder())
        bPos = bb.asFloatBuffer()
        bPos!!.put(sPos)
        bPos!!.position(0)
        val cc = ByteBuffer.allocateDirect(sCoord.size * 4)
        cc.order(ByteOrder.nativeOrder())
        bCoord = cc.asFloatBuffer()
        bCoord!!.put(sCoord)
        bCoord!!.position(0)
    }

    open fun setHalf(half: Boolean) {
        isHalf = half
    }

    open fun setImageBuffer(buffer: IntArray?, width: Int, height: Int) {
        mBitmap = Bitmap.createBitmap(buffer!!, width, height, Bitmap.Config.RGB_565)
    }

    open fun setBitmap(bitmap: Bitmap?) {
        mBitmap = bitmap
    }

    open fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glEnable(GLES20.GL_TEXTURE_2D)
        mProgram = createProgram(mContext!!.resources, vertex, fragment)
        glHPosition = GLES20.glGetAttribLocation(mProgram, "vPosition")
        glHCoordinate = GLES20.glGetAttribLocation(mProgram, "vCoordinate")
        glHTexture = GLES20.glGetUniformLocation(mProgram, "vTexture")
        glHMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix")
        hIsHalf = GLES20.glGetUniformLocation(mProgram, "vIsHalf")
        glHUxy = GLES20.glGetUniformLocation(mProgram, "uXY")
        onDrawCreatedSet(mProgram)
    }

    open fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val w = mBitmap!!.width
        val h = mBitmap!!.height
        val sWH = w / h.toFloat()
        val sWidthHeight = width / height.toFloat()
        uXY = sWidthHeight
        if (width > height) {
            if (sWH > sWidthHeight) {
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight * sWH, sWidthHeight * sWH, -1f, 1f, 3f, 5f)
            } else {
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight / sWH, sWidthHeight / sWH, -1f, 1f, 3f, 5f)
            }
        } else {
            if (sWH > sWidthHeight) {
                Matrix.orthoM(mProjectMatrix, 0, -1f, 1f, -1 / sWidthHeight * sWH, 1 / sWidthHeight * sWH, 3f, 5f)
            } else {
                Matrix.orthoM(mProjectMatrix, 0, -1f, 1f, -sWH / sWidthHeight, sWH / sWidthHeight, 3f, 5f)
            }
        }
        //??????????????????
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 5.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        //??????????????????
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
    }

    open fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        GLES20.glUseProgram(mProgram)
        onDrawSet()
        GLES20.glUniform1i(hIsHalf, if (isHalf) 1 else 0)
        GLES20.glUniform1f(glHUxy, uXY)
        GLES20.glUniformMatrix4fv(glHMatrix, 1, false, mMVPMatrix, 0)
        GLES20.glEnableVertexAttribArray(glHPosition)
        GLES20.glEnableVertexAttribArray(glHCoordinate)
        GLES20.glUniform1i(glHTexture, 0)
        textureId = createTexture()
        GLES20.glVertexAttribPointer(glHPosition, 2, GLES20.GL_FLOAT, false, 0, bPos)
        GLES20.glVertexAttribPointer(glHCoordinate, 2, GLES20.GL_FLOAT, false, 0, bCoord)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    abstract fun onDrawSet()
    abstract fun onDrawCreatedSet(mProgram: Int)

    private fun createTexture(): Int {
        val texture = IntArray(1)
        if (mBitmap != null && !mBitmap!!.isRecycled) {
            //????????????
            GLES20.glGenTextures(1, texture, 0)
            //????????????
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0])
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST.toFloat())
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())
            //??????????????????S????????????????????????[1/2n,1-1/2n]???????????????????????????border??????
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE.toFloat())
            //??????????????????T????????????????????????[1/2n,1-1/2n]???????????????????????????border??????
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE.toFloat())
            //??????????????????????????????????????????2D??????
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0)
            return texture[0]
        }
        return 0
    }
}