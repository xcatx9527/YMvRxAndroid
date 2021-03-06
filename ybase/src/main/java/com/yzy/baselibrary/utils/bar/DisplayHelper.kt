/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yzy.baselibrary.utils.bar

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Point
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import com.yzy.baselibrary.R
import com.yzy.baselibrary.utils.bar.DeviceHelper.isEssentialPhone
import com.yzy.baselibrary.utils.bar.DeviceHelper.isHuawei
import com.yzy.baselibrary.utils.bar.DeviceHelper.isVivo
import com.yzy.baselibrary.utils.bar.DeviceHelper.isXiaomi
import java.util.*

/**
 * @author yzy
 * @date 2016-03-17
 */
object DisplayHelper {
    /**
     * 屏幕密度,系统源码注释不推荐使用
     */
    private val DENSITY = Resources.getSystem().displayMetrics.density

    /**
     * 是否有摄像头
     */
    private var sHasCamera: Boolean? = null
    /**
     * 获取 DisplayMetrics
     *
     * @return
     */
    private fun getDisplayMetrics(context: Context): DisplayMetrics = context.resources.displayMetrics


    /**
     * 把以 dp 为单位的值，转化为以 px 为单位的值
     *
     * @param dpValue 以 dp 为单位的值
     * @return px value
     */
    fun dpToPx(dpValue: Int): Int =(dpValue * DENSITY + 0.5f).toInt()

    /**
     * 把以 px 为单位的值，转化为以 dp 为单位的值
     *
     * @param pxValue 以 px 为单位的值
     * @return dp值
     */
    fun pxToDp(pxValue: Float): Int = (pxValue / DENSITY + 0.5f).toInt()


    private fun getDensity(context: Context): Float = context.resources.displayMetrics.density


    private fun getFontDensity(context: Context): Float = context.resources.displayMetrics.scaledDensity


    /**
     * 获取屏幕宽度
     *
     * @return
     */
    fun getScreenWidth(context: Context): Int = getDisplayMetrics(context).widthPixels

    /**
     * 获取屏幕高度
     *
     * @return
     */
    private fun getScreenHeight(context: Context): Int {
        var screenHeight =
            getDisplayMetrics(context).heightPixels
        if (isXiaomi && xmNavigationGestureEnabled(context)) {
            screenHeight += getResourceNavHeight(context)
        }
        return screenHeight
    }

    /**
     * 获取屏幕的真实宽高
     *
     * @param context
     * @return
     */
    private fun getRealScreenSize(context: Context): IntArray {
        // 切换屏幕导致宽高变化时不能用 cache，先去掉 cache
        return doGetRealScreenSize(context)
    }

    private fun doGetRealScreenSize(context: Context): IntArray {
        val size = IntArray(2)
        var widthPixels: Int
        var heightPixels: Int
        val w =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val d = w.defaultDisplay
        val metrics = DisplayMetrics()
        d.getMetrics(metrics)
        // since SDK_INT = 1;
        widthPixels = metrics.widthPixels
        heightPixels = metrics.heightPixels
        try {
            // used when 17 > SDK_INT >= 14; includes window decorations (statusbar bar/menu bar)
            widthPixels = Display::class.java.getMethod("getRawWidth").invoke(d) as Int
            heightPixels = Display::class.java.getMethod("getRawHeight").invoke(d) as Int
        } catch (ignored: Exception) {
        }
        try {
            // used when SDK_INT >= 17; includes window decorations (statusbar bar/menu bar)
            val realSize = Point()
            d.getRealSize(realSize)
            Display::class.java.getMethod("getRealSize", Point::class.java)
                .invoke(d, realSize)
            widthPixels = realSize.x
            heightPixels = realSize.y
        } catch (ignored: Exception) {
        }
        size[0] = widthPixels
        size[1] = heightPixels
        return size
    }

    /**
     * 剔除挖孔屏等导致的不可用区域后的 width
     */
    fun getUsefulScreenWidth(activity: Activity): Int = getUsefulScreenWidth(activity, NotchHelper.hasNotch(activity))


    fun getUsefulScreenWidth(view: View): Int = getUsefulScreenWidth(view.context, NotchHelper.hasNotch(view))


    private fun getUsefulScreenWidth(context: Context, hasNotch: Boolean): Int {
        var result =
            getRealScreenSize(context)[0]
        val orientation = context.resources.configuration.orientation
        val isLandscape =
            orientation == Configuration.ORIENTATION_LANDSCAPE
        if (!hasNotch) {
            if (isLandscape && isEssentialPhone
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O
            ) {
                // https://arstechnica.com/gadgets/2017/09/essential-phone-review-impressive-for-a-new-company-but-not-competitive/
                // 这里说挖孔屏是状态栏高度的两倍， 但横屏好像小了一点点
                result -= 2 * StatusBarHelper.getStatusBarHeight(context)
            }
            return result
        }
        if (isLandscape) {
            // 华为挖孔屏横屏时，会把整个 window 往后移动，因此，可用区域减小
            if (isHuawei && !huaWeiIsNotchSetToShowInSetting(
                    context
                )
            ) {
                result -= NotchHelper.getNotchSizeInHuawei(context)?.get(1)?:0
            }

            // TODO vivo 设置-系统导航-导航手势样式-显示手势操作区域 打开的情况下，应该减去手势操作区域的高度，但无API
            // TODO vivo 设置-显示与亮度-第三方应用显示比例 选为安全区域显示时，整个 window 会移动，应该减去移动区域，但无API
            // TODO oppo 设置-显示与亮度-应用全屏显示-凹形区域显示控制 关闭是，整个 window 会移动，应该减去移动区域，但无API
        }
        return result
    }

    /**
     * 剔除挖孔屏等导致的不可用区域后的 height
     */
    fun getUsefulScreenHeight(activity: Activity): Int = getUsefulScreenHeight(activity, NotchHelper.hasNotch(activity))


    fun getUsefulScreenHeight(view: View): Int = getUsefulScreenHeight(view.context, NotchHelper.hasNotch(view))


    private fun getUsefulScreenHeight(
        context: Context,
        hasNotch: Boolean
    ): Int {
        var result =
            getRealScreenSize(context)[1]
        val orientation = context.resources.configuration.orientation
        val isPortrait =
            orientation == Configuration.ORIENTATION_PORTRAIT
        if (!hasNotch) {
            if (isPortrait && isEssentialPhone
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O
            ) {
                // https://arstechnica.com/gadgets/2017/09/essential-phone-review-impressive-for-a-new-company-but-not-competitive/
                // 这里说挖孔屏是状态栏高度的两倍
                result -= 2 * StatusBarHelper.getStatusBarHeight(context)
            }
            return result
        }
        //        if (isPortrait) {
        // TODO vivo 设置-系统导航-导航手势样式-显示手势操作区域 打开的情况下，应该减去手势操作区域的高度，但无API
        // TODO vivo 设置-显示与亮度-第三方应用显示比例 选为安全区域显示时，整个 window 会移动，应该减去移动区域，但无API
        // TODO oppo 设置-显示与亮度-应用全屏显示-凹形区域显示控制 关闭是，整个 window 会移动，应该减去移动区域，但无API
//        }
        return result
    }

    private fun isNavMenuExist(context: Context?): Boolean {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
        val hasBackKey =
            KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)

        // 做任何你需要做的,这个设备有一个导航栏
        return !hasMenuKey && !hasBackKey
    }

    /**
     * 单位转换: dp -> px
     *
     * @param dp
     * @return
     */
    @JvmStatic
    fun dp2px(context: Context, dp: Int): Int =(getDensity(context) * dp + 0.5).toInt()
    /**
     * 单位转换: sp -> px
     *
     * @param sp
     * @return
     */
    fun sp2px(context: Context, sp: Int): Int =(getFontDensity(context) * sp + 0.5).toInt()

    /**
     * 单位转换:px -> dp
     *
     * @param px
     * @return
     */
    fun px2dp(context: Context, px: Int): Int =(px / getDensity(context) + 0.5).toInt()

    /**
     * 单位转换:px -> sp
     *
     * @param px
     * @return
     */
    fun px2sp(context: Context, px: Int): Int = (px / getFontDensity(context) + 0.5).toInt()

    /**
     * 判断是否有状态栏
     *
     * @param context
     * @return
     */
    fun hasStatusBar(context: Context): Boolean {
        if (context is Activity) {
            return context.window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN != WindowManager.LayoutParams.FLAG_FULLSCREEN
        }
        return true
    }

    /**
     * 获取ActionBar高度
     *
     * @param context
     * @return
     */
    fun getActionBarHeight(context: Context): Int {
        var actionBarHeight = 0
        val tv = TypedValue()
        if (context.theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(
                tv.data,
                context.resources.displayMetrics
            )
        }
        return actionBarHeight
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    @JvmStatic
    @SuppressLint("PrivateApi")
    fun getStatusBarHeight(context: Context): Int {
        if (isXiaomi) {
            val resourceId =
                context.resources.getIdentifier("status_bar_height", "dimen", "android")
            return if (resourceId > 0) {
                context.resources.getDimensionPixelSize(resourceId)
            } else 0
        }
        try {
            val c = Class.forName("com.android.internal.R\$dimen")
            val obj = c.newInstance()
            val field = c.getField("status_bar_height")
            val x =
                Objects.requireNonNull(field[obj]).toString().toInt()
            if (x > 0) {
                return context.resources.getDimensionPixelSize(x)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 获取虚拟菜单的高度,若无则返回0
     *
     * @param context
     * @return
     */
    fun getNavMenuHeight(context: Context): Int {
        if (!isNavMenuExist(context)) {
            return 0
        }
        val resourceNavHeight =
            getResourceNavHeight(context)
        return if (resourceNavHeight >= 0) {
            resourceNavHeight
        } else getRealScreenSize(context)[1] - getScreenHeight(context)

        // 小米 MIX 有nav bar, 而 getRealScreenSize(context)[1] - getScreenHeight(context) = 0
    }

    private fun getResourceNavHeight(context: Context): Int {
        // 小米4没有nav bar, 而 navigation_bar_height 有值
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else -1
    }

    fun hasCamera(context: Context): Boolean {
        if (sHasCamera == null) {
            val pckMgr = context.packageManager
            val flag = pckMgr
                .hasSystemFeature("android.hardware.camera.front")
            val flag1 = pckMgr.hasSystemFeature("android.hardware.camera")
            val flag2: Boolean
            flag2 = flag || flag1
            sHasCamera = flag2
        }
        return sHasCamera?:false
    }

    /**
     * 是否有硬件menu
     *
     * @param context
     * @return
     */
    fun hasHardwareMenuKey(context: Context?): Boolean {
        return ViewConfiguration.get(context).hasPermanentMenuKey()
    }

    /**
     * 是否有网络功能
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    fun hasInternet(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return Objects.requireNonNull(cm).activeNetworkInfo != null
    }

    /**
     * 判断是否存在pckName包
     *
     * @param pckName
     * @return
     */
    fun isPackageExist(context: Context, pckName: String): Boolean {
        try {
            val pckInfo = context.packageManager.getPackageInfo(pckName, 0)
            if (pckInfo != null) return true
        } catch (ignored: PackageManager.NameNotFoundException) {
        }
        return false
    }

    /**
     * 判断 SD Card 是否 ready
     *
     * @return
     */
    val isSdcardReady: Boolean = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    /**
     * 获取当前国家的语言
     *
     * @param context
     * @return
     */
    fun getCurCountryLan(context: Context): String {
        val config = context.resources.configuration
        val sysLocale: Locale
        sysLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales[0]
        } else {
            config.locale
        }
        return (sysLocale.language
                + "-"
                + sysLocale.country)
    }

    /**
     * 判断是否为中文环境
     *
     * @param context
     * @return
     */
    fun isZhCN(context: Context): Boolean {
        val config = context.resources.configuration
        val sysLocale: Locale
        sysLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales[0]
        } else {
            config.locale
        }
        val lang = sysLocale.country
        return lang.equals("CN", ignoreCase = true)
    }

    /**
     * 设置全屏
     *
     * @param activity
     */
    fun setFullScreen(activity: Activity) {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 取消全屏
     *
     * @param activity
     */
    fun cancelFullScreen(activity: Activity) {
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    /**
     * 判断是否全屏
     *
     * @param activity
     * @return
     */
    fun isFullScreen(activity: Activity): Boolean = activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == WindowManager.LayoutParams.FLAG_FULLSCREEN


    val isElevationSupported: Boolean = Build.VERSION.SDK_INT >= 21

    fun hasNavigationBar(context: Context): Boolean {
        val hasNav = deviceHasNavigationBar()
        if (!hasNav) {
            return false
        }
        return if (isVivo) {
            viVoNavigationGestureEnabled(context)
        } else true
    }

    /**
     * 判断设备是否存在NavigationBar
     *
     * @return true 存在, false 不存在
     */
    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    private fun deviceHasNavigationBar(): Boolean {
        var haveNav = false
        try {
            //1.通过WindowManagerGlobal获取windowManagerService
            // 反射方法：IWindowManager windowManagerService = WindowManagerGlobal.getWindowManagerService();
            val windowManagerGlobalClass =
                Class.forName("android.view.WindowManagerGlobal")
            val getWmServiceMethod =
                windowManagerGlobalClass.getDeclaredMethod("getWindowManagerService")
            getWmServiceMethod.isAccessible = true
            //getWindowManagerService是静态方法，所以invoke null
            val iWindowManager = getWmServiceMethod.invoke(null)

            //2.获取windowMangerService的hasNavigationBar方法返回值
            // 反射方法：haveNav = windowManagerService.hasNavigationBar();
            val iWindowManagerClass: Class<*> = iWindowManager.javaClass
            val hasNavBarMethod =
                iWindowManagerClass.getDeclaredMethod("hasNavigationBar")
            hasNavBarMethod.isAccessible = true
            haveNav = hasNavBarMethod.invoke(iWindowManager) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return haveNav
    }


    /**
     * 获取vivo手机设置中的"navigation_gesture_on"值，判断当前系统是使用导航键还是手势导航操作
     *
     * @param context app Context
     * @return false 表示使用的是虚拟导航键(NavigationBar)， true 表示使用的是手势， 默认是false
     *   0: 默认 1: 隐藏显示区域
     */
    private fun viVoNavigationGestureEnabled(context: Context): Boolean  = Settings.Global.getInt(context.contentResolver, "navigation_gesture_on", 0) != 0

    private fun xmNavigationGestureEnabled(context: Context): Boolean = Settings.Global.getInt(context.contentResolver, "force_fsg_nav_bar", 0) != 0

    @JvmStatic
    fun huaWeiIsNotchSetToShowInSetting(context: Context): Boolean = Settings.Global.getInt(context.contentResolver, "display_notch_status", 0) == 0

    @TargetApi(17)
    fun xmIsNotchSetToShowInSetting(context: Context): Boolean = Settings.Global.getInt(context.contentResolver, "force_black", 0) == 0
}