package com.example.androidopengles.filter

import android.content.Context
import android.opengl.GLES20


/**
 * Created by XC.Li
 * desc:
 */
class ContrastColorFilter(context: Context, filter: ColorFilter.Filter?) :
        AFilter(context, "filter/half_color_vertex.sh", "filter/half_color_fragment.sh") {

    private var filter: ColorFilter.Filter? = null

    private var hChangeType = 0
    private var hChangeColor = 0

    init {
        this.filter = filter
    }

    override fun onDrawSet() {
        GLES20.glUniform1i(hChangeType, filter!!.type)
        GLES20.glUniform3fv(hChangeColor, 1, filter!!.data(), 0)
    }

    override fun onDrawCreatedSet(mProgram: Int) {
        hChangeType = GLES20.glGetUniformLocation(mProgram, "vChangeType")
        hChangeColor = GLES20.glGetUniformLocation(mProgram, "vChangeColor")
    }
}