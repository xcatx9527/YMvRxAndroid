package com.xile.script.base.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xile.script.base.ScriptApplication
import com.xile.script.base.activity.AppInfoActivity
import com.xile.script.base.activity.IMEInfoActivity
import com.xile.script.base.ui.promptdialog.PopupDialog
import com.xile.script.base.ui.view.floatview.RecordFloatView
import com.xile.script.bean.AccountInfoBean
import com.xile.script.config.CollectEnum
import com.xile.script.config.Constants
import com.xile.script.service.FloatingService
import com.xile.script.utils.AppUtil
import com.xile.script.utils.appupdate.util.UpdateUtils
import com.xile.script.utils.common.FileHelper
import com.xile.script.utils.script.ScriptUtil
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.databinding.FragmentPlayBinding
import com.yzy.example.repository.model.PlayViewModel
import kotlinx.android.synthetic.main.fragment_play.rv_list
import kotlinx.android.synthetic.main.layout_comm_title.*

/**
 * date: 2017/6/20 21:03
 */
class ToolFragment : CommFragment<PlayViewModel, FragmentPlayBinding>() {
    private var mListBean: MutableList<AccountInfoBean>? = arrayListOf()
    var mAdapter = object : BaseQuickAdapter<AccountInfoBean, BaseViewHolder>(R.layout.item_tool) {
        override fun convert(baseViewHolder: BaseViewHolder, accountInfoBean: AccountInfoBean) {
            baseViewHolder.setText(R.id.tv_tool_content, accountInfoBean.sectionTitle)
            baseViewHolder.setImageResource(R.id.img_tool, accountInfoBean.resId)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_tool
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    override fun initContentView() {
        main_toolbar.title = "工具"
        initAccountData()
        rv_list!!.layoutManager = LinearLayoutManager(context)
        rv_list.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, view, position ->
            if (position == 0) {
                AppUtil.startActivity(activity, AppInfoActivity::class.java)
            } else if (position == 1) {
                AppUtil.startActivity(activity, IMEInfoActivity::class.java)
            } else if (position == 3) {
            } else if (position == 2) {
                AppUtil.runToBackground(activity)
                activity?.startService(Intent(activity, FloatingService::class.java))
                RecordFloatView.bigFloatState = RecordFloatView.COLLECT
                if (Build.VERSION.SDK_INT >= 21) {
                    Constants.COLLECT_STATE = CollectEnum.COLLECT_PHOTO
                } else {
                    Toast.makeText(activity, "当前系统版本低于5.1无法进行取图功能", Toast.LENGTH_SHORT).show()
                }
            } else if (position == 3) { //卸载手机应用
                val dialog = PopupDialog(ScriptApplication.getInstance(), 2, false)
                dialog.setTitle("是否要卸载手机应用?")
                dialog.setOneButtonText("确定")
                dialog.setTwoButtonText("取消")
                dialog.window!!.setType(WindowManager.LayoutParams.TYPE_PHONE)
                dialog.setOnPositiveListener {
                    ScriptApplication.getService().execute {
                        dialog.dismiss()
                        val operaDownload = "/storage/emulated/0/Download/"
                        FileHelper.deleteFolderFile(operaDownload)
                        ScriptUtil.dealWithUninstallOtherApp()
                        RecordFloatView.updateMessage("卸载手机应用成功!")
                    }
                }
                dialog.setOnNegativeListener { dialog.dismiss() }
                dialog.show()
            } else if (position == 8) { //检查版本更新
                UpdateUtils.changeUpdateVersion(activity, "script")
            } else if (position == 9) { //清除手机缓存
                val dialog = PopupDialog(ScriptApplication.getInstance(), 2, false)
                dialog.setTitle("是否清除手机缓存")
                dialog.setOneButtonText("确定")
                dialog.setTwoButtonText("取消")
                dialog.window!!.setType(WindowManager.LayoutParams.TYPE_PHONE)
                dialog.setOnPositiveListener {
                    ScriptApplication.getService().execute {
                        dialog.dismiss()
                        FileHelper.deletCacheFile()
                        FileHelper.deleteFileWithoutFolder(Constants.SCRIPT_FOLDER_PATH)
                        FileHelper.deleteFileWithoutFolder(Constants.SCRIPT_MATCH_PATH)
                        FileHelper.deleteFileWithoutFolder(Constants.SCRIPT_FOLDER_ROOT_POP_IMG_PATH)
                        RecordFloatView.updateMessage("清除缓存成功!")
                    }
                }
                dialog.setOnNegativeListener { dialog.dismiss() }
                dialog.show()
            }
        }
    }


    //初始化数据
    private fun initAccountData() {
        mListBean!!.clear()
        var bean = AccountInfoBean()
        bean.viewType = AccountInfoBean.ACTION_VIEWTYPE_1
        bean.sectionTitle = "查看应用包名"
        bean.resId = R.drawable.findbundle_id
        mListBean!!.add(bean)
        bean = AccountInfoBean()
        bean.viewType = AccountInfoBean.ACTION_VIEWTYPE_2
        bean.sectionTitle = "查看输入法"
        bean.resId = R.drawable.findbundle_id
        mListBean!!.add(bean)
        bean = AccountInfoBean()
        bean.viewType = AccountInfoBean.ACTION_VIEWTYPE_3
        bean.sectionTitle = "录图取图"
        bean.resId = R.drawable.find_image
        mListBean!!.add(bean)
        bean = AccountInfoBean()
        bean.viewType = AccountInfoBean.ACTION_VIEWTYPE_4
        bean.sectionTitle = "上传图片"
        bean.resId = R.drawable.upload_image
        mListBean!!.add(bean)
        bean = AccountInfoBean()
        bean.viewType = AccountInfoBean.ACTION_VIEWTYPE_7
        bean.sectionTitle = "卸载手机应用"
        bean.resId = R.drawable.collect_role
        mListBean!!.add(bean)
        bean = AccountInfoBean()
        bean.viewType = AccountInfoBean.ACTION_VIEWTYPE_9
        try {
            bean.sectionTitle = "检查版本更新" + " (v" + context?.packageManager?.getPackageInfo(
                context?.packageName.toString(),
                0
            )?.versionName + ")"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        bean.resId = R.drawable.collect_role
        mListBean!!.add(bean)
        bean = AccountInfoBean()
        bean.viewType = AccountInfoBean.ACTION_VIEWTYPE_10
        bean.sectionTitle = "清除手机缓存"
        bean.resId = R.drawable.upload_image
        mListBean!!.add(bean)
        mAdapter.setNewInstance(mListBean)
    }

}