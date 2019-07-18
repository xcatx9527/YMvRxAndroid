package com.yzy.baselibrary.base.activity

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.blankj.utilcode.util.BarUtils
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.yzy.baselibrary.extention.screenHeight
import com.yzy.baselibrary.extention.screenWidth
import com.yzy.baselibrary.extention.setStatusColor
import com.yzy.baselibrary.extention.uiMode1Normal
import org.kodein.di.*
import org.kodein.di.android.*
import org.kodein.di.android.retainedSubKodein
import org.kodein.di.generic.kcontext

abstract class BaseActivity : RxAppCompatActivity(), KodeinAware {
    override val kodeinTrigger = KodeinTrigger()
    override val kodeinContext: KodeinContext<*> = kcontext(this)
    override val kodein by retainedSubKodein(kodein(), copy = Copy.All) {
        initKodein(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.onCreateBefore()
        this.initStatus()
        initBeforeCreateView(savedInstanceState)
        super.onCreate(savedInstanceState)
        setContentView(layoutResId())
        kodeinTrigger.trigger()
        initView()
        initDate()
    }


    /**
     * 页面内容布局resId
     */
    protected abstract fun layoutResId(): Int


    abstract fun initView();
    abstract fun initDate();
    /**
     * 需要在onCreateView中调用的方法
     */
    protected open fun initBeforeCreateView(savedInstanceState: Bundle?) {

    }

    /** 适配状态栏  */
    protected open fun initStatus() {
        //默认状态栏无背景色
        setStatusColor(Color.TRANSPARENT)
        //布局填充到状态栏
        uiMode1Normal()
    }

    /** 这里可以做一些setContentView之前的操作,如全屏、常亮、设置Navigation颜色、状态栏颜色等  */
    protected open fun onCreateBefore() {}

    protected open fun initKodein(builder: Kodein.MainBuilder) {
    }

}