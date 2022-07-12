package com.example.androidopengles

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidopengles.base.BaseActivity
import com.example.androidopengles.image.SGLViewActivity
import com.example.androidopengles.render.FGLViewActivity


class MainActivity : BaseActivity(), View.OnClickListener {

    private var mList: RecyclerView? = null
    private var data: ArrayList<MenuBean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mList = findViewById(R.id.mList)
        mList?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        data = ArrayList()
        add("绘制形体", FGLViewActivity::class.java)
        add("图片处理", SGLViewActivity::class.java)
//        add("图形变换", VaryActivity::class.java)
//        add("相机", CameraActivity::class.java)
//        add("相机2 动画", Camera2Activity::class.java)
//        add("相机3 美颜", Camera3Activity::class.java)
//        add("压缩纹理动画", ZipActivity::class.java)
//        add("FBO使用", FBOActivity::class.java)
//        add("EGL后台处理", EGLBackEnvActivity::class.java)
//        add("3D obj模型", ObjLoadActivity::class.java)
//        add("obj+mtl模型", ObjLoadActivity2::class.java)
//        add("VR效果", VrContextActivity::class.java)
//        add("颜色混合", BlendActivity::class.java)
//        add("光照", LightActivity::class.java)
        mList?.adapter = MenuAdapter()
    }

    private fun add(name: String, clazz: Class<*>) {
        val bean = MenuBean()
        bean.name = name
        bean.clazz = clazz
        data!!.add(bean)
    }

    private class MenuBean {
        var name: String? = null
        var clazz: Class<*>? = null
    }

    private inner class MenuAdapter : RecyclerView.Adapter<MenuAdapter.MenuHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHolder {
            return MenuHolder(LayoutInflater.from(this@MainActivity).inflate(R.layout.item_button, parent, false))
        }

        override fun onBindViewHolder(holder: MenuHolder, position: Int) {
            holder.position = position
        }

        override fun getItemCount(): Int {
            return data?.size ?: 0
        }

        inner class MenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val mBtn: Button
            fun setPosition(position: Int) {
                val bean: MenuBean = data!![position]
                mBtn.text = bean.name
                mBtn.tag = position
            }

            init {
                mBtn = itemView.findViewById(R.id.mBtn)
                mBtn.setOnClickListener(this@MainActivity)
            }
        }
    }

    override fun onClick(view: View) {
        val position = view.tag as Int
        val bean = data!![position]
        startActivity(Intent(this, bean.clazz))
    }

}