package com.yzy.example.component.comm

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialDialogs
import com.yzy.baselibrary.base.*
import com.yzy.example.repository.TokenStateManager


abstract class CommFragment<VM : BaseViewModel<*>, DB : ViewDataBinding> : BaseFragment<VM, DB>() {




    abstract fun initContentView()

    /**
     * 填充布局 空布局 loading 网络异常等
     */
//   private lateinit var viewController: ViewController
    override fun initView(savedSate: Bundle?) {
//        if (rootView != null){
//            viewController = ViewController(rootView!!)
//        }

        //注册 UI事件
        registorDefUIChange()
        initContentView()
    }


    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        viewModel.loadingChange.showDialog.observe(viewLifecycleOwner, Observer {
            showLoadingView()
        })
        viewModel.loadingChange.dismissDialog.observe(viewLifecycleOwner, Observer {
            dismissLoadingView()
        })
        viewModel.loadingChange.tokenError.observe(viewLifecycleOwner, Observer {
            TokenStateManager.instance.mNetworkStateCallback.postValue(true)
        })
    }

    open fun handleEvent(i: Int) {}


    //#################################镶嵌在页面中的loading->Start#################################//
    //显示json动画的loading
    fun showLoadingView() {
       // showActionLoading()
    }

    //关闭loadingView
    fun dismissLoadingView() {
      //  dismissActionLoading()
    }
    //#################################镶嵌在页面中的loading<-END#################################//

    private var mActionDialog: MaterialDialogs? = null

    fun showActionLoading(text: String? = null) {
       /* if (mActionDialog == null) {
            mActionDialog = MaterialDialogs()
        }
        mActionDialog?.onDismiss {
            mActionDialog = null
        }
        mActionDialog?.let {
            if (!text.isNullOrBlank()) it.hintText = text
            it.show(requireActivity().supportFragmentManager)
        }*/
    }

    fun dismissActionLoading() {
       // mActionDialog?.dismissAllowingStateLoss()
    }
}