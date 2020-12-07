package com.yzy.example.component

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.SystemClock
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.IntRange
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.ThreadUtils
import com.chenyang.lloglib.LLog
import com.jeremyliao.liveeventbus.LiveEventBus
import com.xile.script.base.ScriptApplication
import com.xile.script.base.fragment.PlayFragment
import com.xile.script.base.fragment.SettingFragment
import com.xile.script.base.fragment.ToolFragment
import com.xile.script.base.ui.promptdialog.PopupDialog
import com.xile.script.base.ui.promptdialog.PromptDialog
import com.xile.script.base.ui.view.floatview.RecordFloatView
import com.xile.script.config.*
import com.xile.script.http.helper.BaseOrderHelper
import com.xile.script.http.helper.brush.bean.BrushOrderInfo
import com.xile.script.http.helper.brush.mina.BIMSocketClient
import com.xile.script.http.helper.brush.mina.BrushTask
import com.xile.script.http.helper.manager.bean.GamesOrderInfo
import com.xile.script.http.helper.manager.mina.IMSocketClient
import com.xile.script.http.helper.manager.mina.NioTask
import com.xile.script.imagereact.ScreenShotFb
import com.xile.script.service.FloatingService
import com.xile.script.utils.AppUtil
import com.xile.script.utils.common.FileHelper
import com.xile.script.utils.common.ImageUtil
import com.xile.script.utils.common.SpUtil
import com.xile.script.utils.common.StringUtil
import com.xile.script.utils.script.CommandUtil
import com.yzy.baselibrary.extention.nav
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.home.HomeFragment
import com.yzy.example.databinding.FragmentMainBinding
import com.yzy.example.repository.model.MainViewModel
import com.yzy.example.utils.MMkvUtils
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_navigation_header.view.*
import script.tools.config.ScriptConstants
import java.io.File

class MainFragment : CommFragment<MainViewModel, FragmentMainBinding>() {
    var fragments = arrayListOf<Fragment>()
    private val homeFragment: HomeFragment by lazy { HomeFragment() }
    private val playFragment: PlayFragment by lazy { PlayFragment() }
    private val toolFragment: ToolFragment by lazy { ToolFragment() }
    private val settingFragment: SettingFragment by lazy { SettingFragment() }
    private var dialog: PromptDialog? = null
    private var editText: EditText? = null
    private var bootType: String? = null

    init {
        fragments.apply {
            add(homeFragment)
            add(playFragment)
            add(toolFragment)
            add(settingFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!AppUtil.isAppForeground(ScriptApplication.getInstance())) {
            return
        }
        initData()
        if (RecordFloatView.getInstance(context).isShow) {
            RecordFloatView.getInstance(context).hide()
            RecordFloatView.getInstance(context).reset()
        }
        Constants.RECORD_STATE = RecordEnum.STOP_RECORD
        Constants.PLAY_STATE = PlayEnum.STOP_PLAY
        Constants.EXEC_STATE = ExecEnum.EXEC_STOP
        Constants.execServerScript = false
        BaseOrderHelper.resetData()
        IMSocketClient.destroy()
        BIMSocketClient.destroy()
        NioTask.release()
        BrushTask.release()
        if ((Constants.RECORD_STATE == RecordEnum.STOP_RECORD || RecordFloatView.bigFloatState === RecordFloatView.COLLECT) && Constants.needSave) {
            // 弹窗
            if (dialog != null && dialog!!.isShowing()) {
                return
            }
            dialog = PromptDialog(context)
            editText = dialog!!.getEditText() as EditText //方法在CustomDialog中实现
            if (RecordFloatView.bigFloatState === RecordFloatView.RECORD) {
            } else if (RecordFloatView.bigFloatState === RecordFloatView.COLLECT && Constants.COLLECT_STATE == CollectEnum.COLLECT_PHOTO) {
                dialog!!.setTitleText(Html.fromHtml(resources.getString(R.string.dialog_pic_title)))
            }
            dialog!!.setOnPositiveListener(View.OnClickListener { v: View? ->
                if (StringUtil.isEmpty(editText!!.getText().toString())) {
                    Toast.makeText(
                        ScriptApplication.getInstance(),
                        "文件名不能为空，无法进行保存。",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnClickListener
                } else {
                    if (RecordFloatView.bigFloatState === RecordFloatView.RECORD) {
                        checkScriptExits()
                    } else if (RecordFloatView.bigFloatState === RecordFloatView.COLLECT && Constants.COLLECT_STATE == CollectEnum.COLLECT_PHOTO) {
                        checkPictureExits()
                    }
                }
            })
            dialog!!.show()
        }
        if (dialog == null || dialog != null && !dialog!!.isShowing()) {
            clearCollections()
        }
        if (checkFromBoot()) {
            //执行优化脚本
            Thread(Runnable {
                SystemClock.sleep(5000)
                if (!TextUtils.isEmpty(bootType)) {
                    if (ScriptConstants.PLATFORM_MANAGER == bootType) {
                        Constants.currentPlatform =
                            PlatformConfig.getPlatform(PlatformConfig.ZHUXIAN_PACKAGE_NAME)
                    } else if (ScriptConstants.PLATFORM_BRUSH == bootType) {
                        Constants.currentPlatform =
                            PlatformConfig.getPlatform(PlatformConfig.ASHES_PACKAGE_NAME)
                    }
                    RecordFloatView.bigFloatState = RecordFloatView.EXEC
                    Constants.execServerScript = true
                    AppUtil.runToBackground(activity)
                    context?.startService(
                        Intent(
                            context,
                            FloatingService::class.java
                        )
                    )
                    SystemClock.sleep(3000)
                    if (Constants.currentPlatform === PlatformConfig.getPlatform(
                            PlatformConfig.ZHUXIAN_PACKAGE_NAME
                        )
                    ) {
                        try {
                            val jsonStr =
                                FileHelper.getFileString(Constants.MANAGER_ORDER_FOLDER_TEMP_PATH)
                            if (!StringUtil.isEmpty(jsonStr)) {
                                Constants.EXEC_STATE = ExecEnum.EXEC_START
                                Constants.PLAY_STATE = PlayEnum.START_PLAY
                                Constants.sGamesOrderInfo =
                                    ScriptApplication.getGson().fromJson(
                                        jsonStr,
                                        GamesOrderInfo::class.java
                                    )
                                NioTask.getInstance().sendMessage(
                                    NioTask.MSG_TYPE_DEAL_WITH_ORDER,
                                    Constants.sGamesOrderInfo,
                                    1000
                                )
                            } else {
                                getNewOrder()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else if (Constants.currentPlatform === PlatformConfig.getPlatform(
                            PlatformConfig.ASHES_PACKAGE_NAME
                        )
                    ) {
                        try {
                            if (SpUtil.getKeyBoolean(PlatformConfig.CURRENT_NEED_REBOOT, false)) {
                                val jsonStr =
                                    FileHelper.getFileString(Constants.BRUSH_ORDER_FOLDER_TEMP_PATH)
                                if (!StringUtil.isEmpty(jsonStr)) {
                                    Constants.EXEC_STATE =
                                        ExecEnum.EXEC_START
                                    Constants.PLAY_STATE =
                                        PlayEnum.START_PLAY
                                    Constants.sBrushOrderInfo =
                                        ScriptApplication.getGson().fromJson(
                                            jsonStr,
                                            BrushOrderInfo::class.java
                                        )
                                    BrushTask.getInstance().sendMessageObject(
                                        BrushTask.B_MSG_TYPE_DEAL_WITH_ORDER,
                                        Constants.sBrushOrderInfo,
                                        1000
                                    )
                                } else {
                                    getNewOrder()
                                }
                            } else {
                                getNewOrder()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }).start()
        }
    }

    /**
     * 检测脚本是否已经存在
     */
    private fun checkScriptExits() {
        if (File(
                Constants.SCRIPT_FOLDER_PATH + "/" + editText?.getText()
                    .toString() + ".txt"
            ).exists()
        ) {
            dialog?.hide()
            val popupDialog = PopupDialog(context, 2, false)
            popupDialog.setTitle("该脚本已存在,是否覆盖?")
            popupDialog.setOnNegativeListener { view: View? ->
                popupDialog.dismiss()
                dialog?.show()
            }
            popupDialog.setOnPositiveListener { view: View? ->
                CommandUtil.saveScript(editText?.getText().toString(), true)
                dialog?.dismiss()
                popupDialog.dismiss()
            }
            popupDialog.show()
        } else {
            CommandUtil.saveScript(editText?.getText().toString(), true)
        }
    }

    /**
     * 检测图片是否已经存在
     */
    private fun checkPictureExits() {
        if (File(
                Constants.SCRIPT_TAKE_SMALL_PHOTO_PATH + "/" + editText?.getText()
                    .toString() + ".png"
            ).exists()
        ) {
            dialog?.hide()
            val popupDialog = PopupDialog(context, 2, false)
            popupDialog.setTitle("该文件已存在,是否覆盖?")
            popupDialog.setOnNegativeListener { view: View? ->
                popupDialog.dismiss()
                dialog?.show()
            }
            popupDialog.setOnPositiveListener { view: View? ->
                ImageUtil.saveBitmap(
                    ScriptApplication.bitmapTemp,
                    Constants.SCRIPT_TAKE_SMALL_PHOTO_PATH + "/" + editText?.getText()
                        .toString() + ".png",
                    Bitmap.CompressFormat.PNG
                )
                dialog?.dismiss()
                clearCollections()
                popupDialog.dismiss()
                Toast.makeText(context, "保存成功!", Toast.LENGTH_SHORT).show()
            }
            popupDialog.show()
        } else {
            ImageUtil.saveBitmap(
                ScriptApplication.bitmapTemp,
                Constants.SCRIPT_TAKE_SMALL_PHOTO_PATH + "/" + editText?.getText()
                    .toString() + ".png",
                Bitmap.CompressFormat.PNG
            )
            dialog?.dismiss()
            clearCollections()
            Toast.makeText(context, "保存成功!", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 检测是不是来自重启
     *
     * @return
     */
    fun checkFromBoot(): Boolean {
        val intent: Intent? = activity?.getIntent()
        if (intent != null) {
            val bundle = intent.extras
            if (bundle != null) {
                val fromBoot = bundle.getBoolean("fromBoot")
                bootType = bundle.getString("bootType")
                activity?.setIntent(null)
                LLog.e("fromBoot===$fromBoot")
                return fromBoot
            }
        }
        return false
    }

    /**
     * 获取新单子
     */
    fun getNewOrder() {
        ThreadUtils.runOnUiThread {
            RecordFloatView.getInstance(ScriptApplication.getInstance()).initGetOrder()
        }
    }


    /**
     * 清空数据集
     */
    fun clearCollections() {
        Constants.needSave = false
        ScriptApplication.instructInfoList.clear()
        ScriptApplication.scriptInfoRecord.clear()
        ScriptApplication.floatPointList.clear()
        ScriptApplication.areaOrRoleScriptList.clear()
    }

    private var mMediaProjectionManager: MediaProjectionManager? = null
    private val REQUEST_MEDIA_PROJECTION = 1

    /**
     * 初始化截图工具
     */
    protected fun initData() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (mMediaProjectionManager == null) {
                mMediaProjectionManager =
                    context?.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            }
            if (ScreenShotFb.getInstance(ScriptApplication.getInstance())
                    .result == 0 || ScreenShotFb.getInstance(ScriptApplication.getInstance())
                    .intent == null
            ) {
                startActivityForResult(
                    mMediaProjectionManager!!.createScreenCaptureIntent(),
                    REQUEST_MEDIA_PROJECTION
                )
                ScreenShotFb.getInstance(ScriptApplication.getInstance()).mediaProjectionManager =
                    mMediaProjectionManager
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    ScreenShotFb.getInstance(ScriptApplication.getInstance()).result = resultCode
                    ScreenShotFb.getInstance(ScriptApplication.getInstance()).intent = data
                }
            }
        }
    }

    override fun initContentView() {
        LiveEventBus.get("target", Array<Int>::class.java).observe(viewLifecycleOwner, Observer {
            main_bottom_bar.run {
                when (it[1]) {
                    R.id.floatbtn ->
                        if (it[0] > 0) {
                            if (translationY + it[0] < 400) {
                                translationY = translationY + it[0]
                            } else {
                                translationY = 400f
                            }
                        } else {
                            if (translationY + it[0] > 0) {
                                translationY = translationY + it[0]
                            } else {
                                translationY = 0f
                            }
                        }
                }
            }
            LLog.e("target${it[0]},${it[1]}")
        })
        if (viewModel.loadPosition() == -1) {
            selectFragment(0)
        } else {
            selectFragment(viewModel.loadPosition())
        }
        val view = navigationDraw.getHeaderView(0)
        MMkvUtils.instance.getPersonalBean()?.let {
            viewModel.name.postValue(if (it.nickname.isEmpty()) it.username else it.nickname)
        }
        viewModel.meData.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                viewModel.info.postValue("id：${it?.data?.userId}　排名：${it?.data?.rank}")
                viewModel.integral.postValue(it?.data?.coinCount)
            } else {
                if (it.errCode == -1001) {
                  /*  initLoginDialog(childFragmentManager) {
                        mainToLogin = {
                            nav().navigate(MainFragmentDirections.loginFragment())
                        }
                    }*/
                }
            }
        })
        viewModel.getIntegral()
        viewModel.name.observe(viewLifecycleOwner, Observer {
            view.me_name.text = it
        })

        viewModel.info.observe(viewLifecycleOwner, Observer {
            view.me_info.text = it
        })
        mainNavigation.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_main -> selectFragment(0)
                    R.id.menu_project -> selectFragment(1)
                    R.id.menu_system -> selectFragment(2)
                    R.id.menu_navigation -> selectFragment(3)
                }
                true
            }
        }
        navigationDraw.itemIconTintList = null

        navigationDraw.setNavigationItemSelectedListener {
            // 关闭侧边栏
            drawer.close()
            when (it.itemId) {
                R.id.nav_menu_rank -> {
                }
                R.id.nav_menu_square -> {
                }
                R.id.nav_menu_question -> {
                }
                R.id.nav_menu_add -> {
                }
                R.id.nav_menu_setting -> {
                    nav().navigate(MainFragmentDirections.settingFragment())
                }
                R.id.nav_menu_logout -> {
                }
            }
            true
        }
    }

    //当前页面
    private var currentFragment: Fragment? = null
    private var currentPosition: Int = 0

    //设置选中的fragment
    private fun selectFragment(@IntRange(from = 0, to = 4) index: Int) {
        //需要显示的fragment
        val fragment = fragments[index]
        //和当前选中的一样，则不再处理
        if (currentFragment == fragment) return
        currentPosition = index
        drawer.setDrawerLockMode(if (index == 0) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        //先关闭之前显示的
        currentFragment?.let { FragmentUtils.hide(it) }
        //设置现在需要显示的
        currentFragment = fragment
        if (!fragment.isAdded) { //没有添加，则添加并显示
            val tag = fragment::class.java.simpleName
            FragmentUtils.add(childFragmentManager, fragment, mainContainer.id, tag, false)
        } else { //添加了就直接显示
            FragmentUtils.show(fragment)
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_main
    override fun onDestroyView() {
        viewModel.setValue(currentPosition)
        super.onDestroyView()
    }

    private var nowTime: Long = 0

    override fun onBackPressed() {
        if (System.currentTimeMillis() - nowTime > 2000) {
            Toast.makeText(
                context,
                resources.getString(R.string.text_quit_tip),
                Toast.LENGTH_SHORT
            ).show()
            nowTime = System.currentTimeMillis()
            return
        }
        context?.stopService(Intent(context, FloatingService::class.java))
        super.onBackPressed()
    }


    override fun onDestroy() {
        ScreenShotFb.getInstance(ScriptApplication.getInstance()).closeObject()
        super.onDestroy()
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}