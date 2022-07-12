package com.example.androidopengles.image

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.androidopengles.R
import com.example.androidopengles.base.BaseActivity
import com.example.androidopengles.filter.ColorFilter
import com.example.androidopengles.filter.ContrastColorFilter


/**
 * Created by XC.Li
 * desc:
 */
class SGLViewActivity: BaseActivity() {

    private lateinit var mGLView: SGLView
    private var isHalf = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        mGLView = findViewById<View>(R.id.glView) as SGLView
    }

    override fun onResume() {
        super.onResume()
        mGLView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mGLView.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mDeal -> {
                isHalf = !isHalf
                if (isHalf) {
                    item.title = "处理一半"
                } else {
                    item.title = "全部处理"
                }
                mGLView.getRender()!!.refresh()
            }
            R.id.mDefault -> mGLView.setFilter(ContrastColorFilter(this, ColorFilter.Filter.NONE))
            R.id.mGray -> mGLView.setFilter(ContrastColorFilter(this, ColorFilter.Filter.GRAY))
            R.id.mCool -> mGLView.setFilter(ContrastColorFilter(this, ColorFilter.Filter.COOL))
            R.id.mWarm -> mGLView.setFilter(ContrastColorFilter(this, ColorFilter.Filter.WARM))
            R.id.mBlur -> mGLView.setFilter(ContrastColorFilter(this, ColorFilter.Filter.BLUR))
            R.id.mMagn -> mGLView.setFilter(ContrastColorFilter(this, ColorFilter.Filter.MAGN))
        }
        mGLView.getRender()!!.getFilter()!!.setHalf(isHalf)
        mGLView.requestRender()
        return super.onOptionsItemSelected(item)
    }

}