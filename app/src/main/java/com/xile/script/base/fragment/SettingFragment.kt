package com.xile.script.base.fragment

import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chenyang.lloglib.LLog
import com.xile.script.base.ScriptApplication
import com.xile.script.bean.DeviceInfo
import com.xile.script.config.Constants
import com.xile.script.config.PlatformConfig
import com.xile.script.utils.CMDUtil
import com.xile.script.utils.FileUtil
import com.xile.script.utils.common.DeviceUtil
import com.xile.script.utils.common.RandomUtil
import com.xile.script.utils.common.SpUtil
import com.xile.script.utils.common.StringUtil
import com.xile.script.utils.script.ScriptUtil
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.databinding.FragmentPlayBinding
import com.yzy.example.repository.model.PlayViewModel
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.layout_comm_title.*
import script.tools.config.DeviceConfig

/**
 * Home [Fragment] subclass.
 */
class SettingFragment : CommFragment<PlayViewModel, FragmentPlayBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_setting
    }
    override fun initContentView() {
        main_toolbar.title = "设置"
        btn_save.setOnClickListener(this)
        btn_change.setOnClickListener(this)
        edt_admin!!.setText(SpUtil.getKeyString(PlatformConfig.CURRENT_ADMIN, ""))
        edt_pwd!!.setText(SpUtil.getKeyString(PlatformConfig.CURRENT_PASSWORD, ""))
        phone_name!!.setText(SpUtil.getKeyString(PlatformConfig.PHONE_NAME, ""))
        edt_brand!!.setText(FileUtil.readFile(DeviceConfig.DEVICE_PATH))
        vpn_userName!!.setText(SpUtil.getKeyString(PlatformConfig.VPN_USERNAME, ""))
        vpn_passWord!!.setText(SpUtil.getKeyString(PlatformConfig.VPN_PASSWORD, ""))
        cb_need_reboot!!.isChecked = SpUtil.getKeyBoolean(PlatformConfig.CURRENT_NEED_REBOOT, false)
        cb_boolean_recharge!!.isChecked = SpUtil.getKeyBoolean(
            PlatformConfig.CURRENT_BOOLEAN_RECHARGE,
            false
        )
        cb_adb_cmd!!.isChecked = SpUtil.getKeyBoolean(PlatformConfig.CURRENT_ADB_CMD, false)
        cb_screen_on!!.isChecked = SpUtil.getKeyBoolean(PlatformConfig.CURRENT_SCREEN_ON, true)
        cb_change_api_level!!.isChecked = "false" != FileUtil.readFile(Constants.LOCAL_HOOK_API)
        cb_change_finger!!.isChecked = "true" == FileUtil.readFile(Constants.LOCAL_HOOK_FINGER)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_save -> {
                //         ArrayList<Map> ime = IMEUtil.getInstance().getIME(getActivity());
                if (StringUtil.isEmpty(edt_brand!!.text.toString())) {
                    Toast.makeText(ScriptApplication.getInstance(), "请输入手机品牌!", Toast.LENGTH_SHORT)
                        .show()
                }
                SpUtil.putKeyString(PlatformConfig.PHONE_NAME, phone_name!!.text.toString())
                SpUtil.putKeyString(
                    PlatformConfig.CURRENT_ADMIN,
                    edt_admin!!.text.toString().trim { it <= ' ' }
                )
                SpUtil.putKeyString(
                    PlatformConfig.CURRENT_PASSWORD,
                    edt_pwd!!.text.toString().trim { it <= ' ' }
                )
                FileUtil.saveFile(
                    edt_brand!!.text.toString().trim { it <= ' ' },
                    DeviceConfig.DEVICE_PATH
                )
                FileUtil.saveFile(
                    phone_name!!.text.toString().trim { it <= ' ' },
                    Constants.PHONE_NAME_PATH
                )
                SpUtil.putKeyString(PlatformConfig.VPN_USERNAME, vpn_userName!!.text.toString())
                SpUtil.putKeyString(PlatformConfig.VPN_PASSWORD, vpn_passWord!!.text.toString())
                SpUtil.putKeyBoolean(PlatformConfig.CURRENT_NEED_REBOOT, cb_need_reboot!!.isChecked)
                SpUtil.putKeyBoolean(
                    PlatformConfig.CURRENT_BOOLEAN_RECHARGE,
                    cb_boolean_recharge!!.isChecked
                )
                SpUtil.putKeyBoolean(PlatformConfig.CURRENT_ADB_CMD, cb_adb_cmd!!.isChecked)
                SpUtil.putKeyBoolean(PlatformConfig.CURRENT_SCREEN_ON, cb_screen_on!!.isChecked)
                FileUtil.saveFile(
                    cb_change_api_level!!.isChecked.toString(),
                    Constants.LOCAL_HOOK_API
                )
                FileUtil.saveFile(
                    cb_change_finger!!.isChecked.toString(),
                    Constants.LOCAL_HOOK_FINGER
                )
                Toast.makeText(ScriptApplication.getInstance(), "保存成功!", Toast.LENGTH_SHORT).show()
                CMDUtil.checkRootPermission()
            }
            R.id.btn_change -> changeDevice()
        }
    }

    companion object {
        private fun changeDevice() {
            ScriptApplication.getService().execute(Runnable {
                val imei_1 = DeviceUtil.getIMEI(ScriptApplication.getInstance())
                ScriptUtil.dealWithChangeDeviceInfo()
                SystemClock.sleep(3000)
                val imei_2 = DeviceUtil.getIMEI(ScriptApplication.getInstance())
                LLog.i("修改前imei_1==========: $imei_1")
                LLog.i("修改后imei_2==========: $imei_2")
                if (TextUtils.isEmpty(imei_1) || TextUtils.isEmpty(imei_2) || imei_1 == imei_2) {
                    LLog.e("修改imei失败")
                    return@Runnable
                }
                CMDUtil.execShell("chmod 777 /system")
                SystemClock.sleep(1000)
                CMDUtil.execShell("chmod 777 /system/build.prop")
                SystemClock.sleep(1000)
                CMDUtil.execShell("mount -o rw,remount -t rootfs /system")
                SystemClock.sleep(1000)
                var hardwareInfo = FileUtil.readFile("/system/build.prop")
                val deviceStr =
                    SpUtil.getKeyString(PlatformConfig.CURRENT_DEVICE_INFO, "") //取当前设备信息
                val deviceInfo = ScriptApplication.getGson()
                    .fromJson(deviceStr, DeviceInfo::class.java)
                //写build.prop
                val buildId =
                    getHardwareInfo(
                        hardwareInfo,
                        "ro.build.id="
                    )
                val displayId =
                    getHardwareInfo(
                        hardwareInfo,
                        "ro.build.display.id="
                    )
                val brand =
                    getHardwareInfo(
                        hardwareInfo,
                        "ro.product.brand="
                    )
                val model =
                    getHardwareInfo(
                        hardwareInfo,
                        "ro.product.model="
                    )
                val device =
                    getHardwareInfo(
                        hardwareInfo,
                        "ro.product.device="
                    )
                val name =
                    getHardwareInfo(
                        hardwareInfo,
                        "ro.product.name="
                    )
                val product =
                    getHardwareInfo(
                        hardwareInfo,
                        "ro.build.product="
                    )
                val manufacturer =
                    getHardwareInfo(
                        hardwareInfo,
                        "ro.product.manufacturer="
                    )
                val description =
                    getHardwareInfo(
                        hardwareInfo,
                        "ro.build.description="
                    )
                val fingerprint =
                    getHardwareInfo(
                        hardwareInfo,
                        "ro.build.fingerprint="
                    )
                val randomId =
                    RandomUtil.getRandomNum(5, 9).toString() + "." + RandomUtil.getRandomNum(
                        0,
                        9
                    ) + "." + RandomUtil.getRandomNum(0, 9)
                hardwareInfo = hardwareInfo.replace(
                    "ro.build.id=$buildId",
                    "ro.build.id=" + deviceInfo.display
                ).replace(
                    "ro.build.display.id=$displayId",
                    "ro.build.display.id=" + deviceInfo.display
                )
                    .replace(
                        "ro.product.brand=$brand",
                        "ro.product.brand=" + deviceInfo.brand
                    ).replace(
                        "ro.product.model=$model",
                        "ro.product.model=" + deviceInfo.model
                    )
                    .replace(
                        "ro.product.device=$device",
                        "ro.product.device=" + deviceInfo.brand
                    ).replace(
                        "ro.product.name=$name",
                        "ro.product.name=" + deviceInfo.brand
                    )
                    .replace(
                        "ro.build.product=$product",
                        "ro.build.product=" + deviceInfo.brand
                    )
                    .replace(
                        "ro.product.manufacturer=$manufacturer",
                        "ro.product.manufacturer=" + deviceInfo.manufacturer
                    )
                    .replace(
                        "ro.build.description=$description",
                        "ro.build.description=" + deviceInfo.brand + "-user " + deviceInfo.release + " " + deviceInfo.display + " " + randomId + " release-keys"
                    )
                    .replace(
                        "ro.build.fingerprint=$fingerprint",
                        "ro.build.fingerprint=" + deviceInfo.brand + "/" + deviceInfo.device + "/" + deviceInfo.device + ":" + deviceInfo.release + "/" + deviceInfo.display + "/" + randomId + ":user/release-keys"
                    )
                FileUtil.saveFile(hardwareInfo, "/sdcard/build.prop")
                //一定要还原权限 否则手机启动不了
                CMDUtil.execShell("cp /sdcard/build.prop /system/build.prop")
                SystemClock.sleep(1000)
                CMDUtil.execShell("chmod 0644 /system/build.prop")
                SystemClock.sleep(1000)
                CMDUtil.execShell("reboot")
            })
        }

        /**
         * 替换手机硬件信息
         *
         * @param scrStr
         * @param property
         * @return
         */
        fun getHardwareInfo(scrStr: String, property: String): String {
            var descStr =
                scrStr.substring(scrStr.indexOf(property) + property.length)
            descStr = descStr.substring(0, descStr.indexOf("\n"))
            return descStr
        }
    }




}