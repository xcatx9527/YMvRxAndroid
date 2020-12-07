package com.xile.script.base.fragment

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chenyang.lloglib.LLog
import com.xile.script.base.ScriptApplication
import com.xile.script.base.ui.view.ItemTypeDialog
import com.xile.script.base.ui.view.floatview.RecordFloatView
import com.xile.script.bean.FileInfo
import com.xile.script.config.Constants
import com.xile.script.config.PlatformConfig
import com.xile.script.http.common.HttpConstants
import com.xile.script.http.helper.other.UploadFileHelper
import com.xile.script.http.helper.other.UploadFileHelper.OnUploadFileListener
import com.xile.script.service.FloatingService
import com.xile.script.utils.AppUtil
import com.xile.script.utils.SortListUtil
import com.xile.script.utils.common.FileHelper
import com.xile.script.utils.script.ExecuteUtil
import com.xile.script.utils.script.ReorganizeUitl
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.databinding.FragmentPlayBinding
import com.yzy.example.repository.model.PlayViewModel
import kotlinx.android.synthetic.main.fragment_play.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*

class PlayFragment : CommFragment<PlayViewModel, FragmentPlayBinding>() {
    private var fileInfoList: MutableList<FileInfo>? = null
    var adapter = object : BaseQuickAdapter<FileInfo, BaseViewHolder>(R.layout.item_play) {
        override fun convert(
            baseViewHolder: BaseViewHolder, fileInfo: FileInfo
        ) {
            baseViewHolder.setText(R.id.play_name, fileInfo.name)
        }
    }

    override fun initContentView() {
        fileInfoList = ArrayList()

        adapter.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
            val fileInfo = adapter!!.getItem(position) as FileInfo
            Log.e(
                "",
                "fileInfo:" + Constants.SCRIPT_FOLDER_PATH + fileInfo.name
            )
            FileHelper.copyFile(
                Constants.SCRIPT_FOLDER_PATH + fileInfo.name,
                Constants.SCRIPT_FOLDER_TEMP_PATH
            )
            FileHelper.copyFile(
                Constants.SCRIPT_FOLDER_PATH + fileInfo.name
                    .substring(0, fileInfo.name.length - 4) + ".jpg",
                Constants.SCRIPT_FOLDER_TEMP_PNG_PATH
            )
            Constants.execServerScript = false
            Constants.currentPlatform = ""
            AppUtil.runToBackground(activity)
            ScriptApplication.getService().execute {
                ExecuteUtil.execLocalScript(
                    loopNum,
                    Constants.SCRIPT_FOLDER_TEMP_PATH
                )
            }
        }
        adapter.setOnItemLongClickListener { adapter: BaseQuickAdapter<*, *>, view: View?, position: Int ->
            val item =
                adapter.getItem(position) as FileInfo
            val itemTypeDialog = ItemTypeDialog(
                activity,
                "管理后台",
                "优化平台",
                "删除脚本",
                "转成竖屏",
                "转成横屏",
                object : ItemTypeDialog.OnInputMileageChanged {
                    override fun onConfirm1() {  //上传管理后台
                        uploadScriptFile(
                            item.name,
                            HttpConstants.uploadManagerPicAndShellUrl
                        )
                    }

                    override fun onConfirm2() { //上传优化平台
                        uploadScriptFile(
                            item.name,
                            HttpConstants.uploadCenterControlPicAndShellUrl
                        )
                    }

                    override fun onConfirm3() { //删除脚本
                        FileHelper.deleteFile(Constants.SCRIPT_FOLDER_PATH + item.name)
                        Handler(Looper.getMainLooper()).postDelayed(
                            { initAdapter() },
                            1000
                        )
                    }

                    override fun onConfirm4() { //转成竖屏
                        val strings =
                            FileHelper.readFile(Constants.SCRIPT_FOLDER_PATH + item.name)
                        changeMothoed(strings, item.name, true)
                    }

                    override fun onConfirm5() { //转成横屏
                        val strings =
                            FileHelper.readFile(Constants.SCRIPT_FOLDER_PATH + item.name)
                        changeMothoed(strings, item.name, false)
                    }
                })
            itemTypeDialog.show()
            true
        }
        rv_list!!.layoutManager = LinearLayoutManager(context)
        rv_list!!.adapter = adapter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_play
    }

    /**
     * 上传指定路径的脚本文件
     *
     * @param name
     */
    private fun uploadScriptFile(name: String, url: String) {
        val file =
            File(Constants.SCRIPT_FOLDER_PATH + name)
        if (file.exists()) {
            UploadFileHelper().uploadFileAction(
                activity,
                url,
                file,
                object : OnUploadFileListener {
                    override fun onSuccess(o: Any) {
                        try {
                            val jsonObject = JSONObject(o.toString())
                            val code = jsonObject.optInt("code")
                            val message = jsonObject.optString("message")
                            if (code == 200 && message == "ok") {
                                LLog.e("脚本上传成功!")
                                RecordFloatView.updateMessage("脚本上传成功!")
                            } else {
                                LLog.e("脚本上传失败!code=$code,message=$message")
                                RecordFloatView.updateMessage("脚本上传失败!code=$code,message=$message")
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(errorCode: Int, msg: String) {
                        RecordFloatView.updateMessage("脚本上传失败!errorCode=$errorCode,msg=$msg")
                    }
                })
        } else {
            Toast.makeText(activity, "脚本不存在!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        initAdapter()
    }

    fun initAdapter() {
        ScriptApplication.getService().execute {
            try {
                fileInfoList = FileHelper.getFileLastModified(Constants.SCRIPT_FOLDER_PATH)
                fileInfoList = SortListUtil.sort(
                    fileInfoList,
                    "time",
                    SortListUtil.DESC
                ) as MutableList<FileInfo>?
                Handler(Looper.getMainLooper()).post {
                    if (adapter != null) {
                        adapter!!.setNewInstance(fileInfoList as MutableList<FileInfo>?)
                        adapter!!.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onClick(v: View) {
        var fileInfoList = FileHelper.getFileLastModified(Constants.SCRIPT_FOLDER_PATH)
        when (v.id) {
            R.id.img_sort_way -> if (img_sort_way!!.drawable.current
                    .constantState === resources.getDrawable(R.drawable.date_sort)
                    .constantState
            ) { //从时间排序变成名称排序
                //当ImageView的src属性为R.drawable.A时，设置ImageView的src属性为R.drawable.B
                img_sort_way!!.setImageResource(R.drawable.name_sort)
                fileInfoList = SortListUtil.sort(
                    fileInfoList,
                    "name",
                    SortListUtil.ASC
                ) as List<FileInfo?>
                adapter!!.setNewInstance(fileInfoList)
                adapter.notifyDataSetChanged()
            } else {
                //否则设置ImageView的src属性为R.drawable.A
                img_sort_way!!.setImageResource(R.drawable.date_sort)
                fileInfoList = SortListUtil.sort(
                    fileInfoList,
                    "time",
                    SortListUtil.DESC
                ) as List<FileInfo?>
                adapter!!.setNewInstance(fileInfoList)
                adapter.notifyDataSetChanged()
            }
            R.id.ashes_server_script -> try {
                Constants.currentPlatform =
                    PlatformConfig.getPlatform(PlatformConfig.ASHES_PACKAGE_NAME)
                RecordFloatView.bigFloatState = RecordFloatView.EXEC
                Constants.execServerScript = true
                AppUtil.runToBackground(activity)
                requireActivity().startService(
                    Intent(
                        ScriptApplication.getInstance(),
                        FloatingService::class.java
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private var loopNum = 1

    /**
     * 立即播放
     */
    fun playRightNow() {
        if (loopNum == -1) {
            return
        } else if (loopNum == 0) {  //无限循环
            loopNum = Int.MAX_VALUE
        }
        ScriptApplication.getService().execute {
            ExecuteUtil.execLocalScript(
                loopNum,
                Constants.SCRIPT_FOLDER_TEMP_PATH
            )
        }
    }

    private fun changeMothoed(
        strings: List<String>?,
        name: String,
        flag: Boolean
    ) {
        if (strings != null && strings.size > 0) {
            var instruct = ""
            for (i in strings.indices) {
                instruct = if (flag) {
                    """
     ${ReorganizeUitl.landscapeToPortraitInChainess(strings[i])}
     
     """.trimIndent()
                } else {
                    """
     ${ReorganizeUitl.portraitToLandscapeInChainess(strings[i])}
     
     """.trimIndent()
                }
                FileHelper.addFileContent2(
                    Constants.SCRIPT_FOLDER_PATH + if (flag) "竖屏_$name" else "横屏_$name",
                    if (TextUtils.isEmpty(instruct)) "\n" else instruct
                )
            }
        } else {
            Toast.makeText(context, "转换出错,此文件内容有问题,请查询后再转", Toast.LENGTH_LONG).show()
        }
        Handler()
            .postDelayed({ initAdapter() }, 1000)
    }


}