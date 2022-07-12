package com.example.androidopengles.render

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.androidopengles.ChooseActivity
import com.example.androidopengles.R


class FGLViewActivity : AppCompatActivity() {

    private val REQ_CHOOSE = 0x0101

    private var mChange: Button? = null
    private var mGLView: FGLView? = null


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fglview)
        init()
    }

    private fun init() {
        mChange = findViewById<View>(R.id.mChange) as Button
        mGLView = findViewById<View>(R.id.mGLView) as FGLView
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.mChange -> {
                val intent = Intent(this, ChooseActivity::class.java)
                startActivityForResult(intent, REQ_CHOOSE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            mGLView?.setShape(data?.getSerializableExtra("name") as Class<out Shape?>?)
        }
    }

    override fun onResume() {
        super.onResume()
        mGLView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mGLView?.onPause()
    }
}