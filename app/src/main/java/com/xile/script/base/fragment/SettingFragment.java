package com.xile.script.base.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.TitleLayout;
import com.xile.script.bean.DeviceInfo;
import com.xile.script.config.Constants;
import com.xile.script.config.PlatformConfig;
import com.xile.script.utils.CMDUtil;
import com.xile.script.utils.FileUtil;
import com.xile.script.utils.common.DeviceUtil;
import com.xile.script.utils.common.RandomUtil;
import com.xile.script.utils.common.SpUtil;
import com.xile.script.utils.common.StringUtil;
import com.xile.script.utils.script.ScriptUtil;
import com.yzy.example.R;

import script.tools.config.DeviceConfig;


/**
 * Home {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    private TitleLayout tl_title_layout;
    private Button btnSave;
    private Button btn_change;
    private EditText phone_name;
    private EditText edt_brand;
    private EditText edt_admin;
    private EditText edt_pwd;
    private EditText vpn_userName;
    private EditText vpn_passWord;
    private CheckBox cb_need_reboot;
    private CheckBox cb_boolean_recharge;
    private CheckBox cb_adb_cmd;
    private CheckBox cb_screen_on;
    private CheckBox cb_change_api_level;
    private CheckBox cb_change_finger;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_setting, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        tl_title_layout = view.findViewById(R.id.tl_title_layout);
        edt_admin = view.findViewById(R.id.edt_admin);
        edt_pwd = view.findViewById(R.id.edt_pwd);
        phone_name = view.findViewById(R.id.phone_name);
        edt_brand = view.findViewById(R.id.edt_brand);
        btnSave = view.findViewById(R.id.btn_save);
        btn_change = view.findViewById(R.id.btn_change);
        vpn_userName = view.findViewById(R.id.vpn_userName);
        vpn_passWord = view.findViewById(R.id.vpn_passWord);
        cb_need_reboot = view.findViewById(R.id.cb_need_reboot);
        cb_boolean_recharge = view.findViewById(R.id.cb_boolean_recharge);
        cb_adb_cmd = view.findViewById(R.id.cb_adb_cmd);
        cb_screen_on = view.findViewById(R.id.cb_screen_on);
        cb_change_api_level = view.findViewById(R.id.cb_change_api_level);
        cb_change_finger = view.findViewById(R.id.cb_change_finger);
        btnSave.setOnClickListener(this);
        btn_change.setOnClickListener(this);
        tl_title_layout.setTitle("设置");
        tl_title_layout.setLeftBtnVisiable(false);
    }

    private void initData() {
        edt_admin.setText(SpUtil.getKeyString(PlatformConfig.CURRENT_ADMIN, ""));
        edt_pwd.setText(SpUtil.getKeyString(PlatformConfig.CURRENT_PASSWORD, ""));
        phone_name.setText(SpUtil.getKeyString(PlatformConfig.PHONE_NAME, ""));
        edt_brand.setText(FileUtil.readFile(DeviceConfig.DEVICE_PATH));
        vpn_userName.setText(SpUtil.getKeyString(PlatformConfig.VPN_USERNAME, ""));
        vpn_passWord.setText(SpUtil.getKeyString(PlatformConfig.VPN_PASSWORD, ""));
        cb_need_reboot.setChecked(SpUtil.getKeyBoolean(PlatformConfig.CURRENT_NEED_REBOOT, false));
        cb_boolean_recharge.setChecked(SpUtil.getKeyBoolean(PlatformConfig.CURRENT_BOOLEAN_RECHARGE, false));
        cb_adb_cmd.setChecked(SpUtil.getKeyBoolean(PlatformConfig.CURRENT_ADB_CMD, false));
        cb_screen_on.setChecked(SpUtil.getKeyBoolean(PlatformConfig.CURRENT_SCREEN_ON, true));
        cb_change_api_level.setChecked(!"false".equals(FileUtil.readFile(Constants.LOCAL_HOOK_API)));
        cb_change_finger.setChecked("true".equals(FileUtil.readFile(Constants.LOCAL_HOOK_FINGER)));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
       //         ArrayList<Map> ime = IMEUtil.getInstance().getIME(getActivity());
                if (StringUtil.isEmpty(edt_brand.getText().toString())) {
                    Toast.makeText(ScriptApplication.getInstance(), "请输入手机品牌!", Toast.LENGTH_SHORT).show();
                }
                SpUtil.putKeyString(PlatformConfig.PHONE_NAME, phone_name.getText().toString());
                SpUtil.putKeyString(PlatformConfig.CURRENT_ADMIN, edt_admin.getText().toString().trim());
                SpUtil.putKeyString(PlatformConfig.CURRENT_PASSWORD, edt_pwd.getText().toString().trim());
                FileUtil.saveFile(edt_brand.getText().toString().trim(), DeviceConfig.DEVICE_PATH);
                FileUtil.saveFile(phone_name.getText().toString().trim(), Constants.PHONE_NAME_PATH);
                SpUtil.putKeyString(PlatformConfig.VPN_USERNAME, vpn_userName.getText().toString());
                SpUtil.putKeyString(PlatformConfig.VPN_PASSWORD, vpn_passWord.getText().toString());
                SpUtil.putKeyBoolean(PlatformConfig.CURRENT_NEED_REBOOT, cb_need_reboot.isChecked());
                SpUtil.putKeyBoolean(PlatformConfig.CURRENT_BOOLEAN_RECHARGE, cb_boolean_recharge.isChecked());
                SpUtil.putKeyBoolean(PlatformConfig.CURRENT_ADB_CMD, cb_adb_cmd.isChecked());
                SpUtil.putKeyBoolean(PlatformConfig.CURRENT_SCREEN_ON, cb_screen_on.isChecked());
                FileUtil.saveFile(String.valueOf(cb_change_api_level.isChecked()), Constants.LOCAL_HOOK_API);
                FileUtil.saveFile(String.valueOf(cb_change_finger.isChecked()), Constants.LOCAL_HOOK_FINGER);
                Toast.makeText(ScriptApplication.getInstance(), "保存成功!", Toast.LENGTH_SHORT).show();
                CMDUtil.checkRootPermission();
                break;

            case R.id.btn_change:
                changeDevice();
                break;

        }
    }


    private static void changeDevice(){
        ScriptApplication.getService().execute(new Runnable() {
            @Override
            public void run() {
                String imei_1 = DeviceUtil.getIMEI(ScriptApplication.getInstance());
                ScriptUtil.dealWithChangeDeviceInfo();
                SystemClock.sleep(3000);
                String imei_2 = DeviceUtil.getIMEI(ScriptApplication.getInstance());
                LLog.i("修改前imei_1==========: " + imei_1);
                LLog.i("修改后imei_2==========: " + imei_2);
                if (TextUtils.isEmpty(imei_1) || TextUtils.isEmpty(imei_2) || imei_1.equals(imei_2)) {
                    LLog.e("修改imei失败");
                    return;
                }
                CMDUtil.execShell("chmod 777 /system");
                SystemClock.sleep(1000);
                CMDUtil.execShell("chmod 777 /system/build.prop");
                SystemClock.sleep(1000);
                CMDUtil.execShell("mount -o rw,remount -t rootfs /system");
                SystemClock.sleep(1000);

                String hardwareInfo = FileUtil.readFile("/system/build.prop");
                String deviceStr = SpUtil.getKeyString(PlatformConfig.CURRENT_DEVICE_INFO, "");//取当前设备信息
                DeviceInfo deviceInfo = ScriptApplication.getGson().fromJson(deviceStr, DeviceInfo.class);
                //写build.prop
                String buildId = getHardwareInfo(hardwareInfo, "ro.build.id=");
                String displayId = getHardwareInfo(hardwareInfo, "ro.build.display.id=");
                String brand = getHardwareInfo(hardwareInfo, "ro.product.brand=");
                String model = getHardwareInfo(hardwareInfo, "ro.product.model=");
                String device = getHardwareInfo(hardwareInfo, "ro.product.device=");
                String name = getHardwareInfo(hardwareInfo, "ro.product.name=");
                String product = getHardwareInfo(hardwareInfo, "ro.build.product=");
                String manufacturer = getHardwareInfo(hardwareInfo, "ro.product.manufacturer=");
                String description = getHardwareInfo(hardwareInfo, "ro.build.description=");
                String fingerprint = getHardwareInfo(hardwareInfo, "ro.build.fingerprint=");
                String randomId = RandomUtil.getRandomNum(5, 9) + "." + RandomUtil.getRandomNum(0, 9) + "." + RandomUtil.getRandomNum(0, 9);
                hardwareInfo = hardwareInfo.replace("ro.build.id=" + buildId, "ro.build.id=" + deviceInfo.getDisplay()).replace("ro.build.display.id=" + displayId, "ro.build.display.id=" + deviceInfo.getDisplay())
                        .replace("ro.product.brand=" + brand, "ro.product.brand=" + deviceInfo.getBrand()).replace("ro.product.model=" + model, "ro.product.model=" + deviceInfo.getModel())
                        .replace("ro.product.device=" + device, "ro.product.device=" + deviceInfo.getBrand()).replace("ro.product.name=" + name, "ro.product.name=" + deviceInfo.getBrand())
                        .replace("ro.build.product=" + product, "ro.build.product=" + deviceInfo.getBrand())
                        .replace("ro.product.manufacturer=" + manufacturer, "ro.product.manufacturer=" + deviceInfo.getManufacturer())
                        .replace("ro.build.description=" + description, "ro.build.description=" + deviceInfo.getBrand() + "-user " + deviceInfo.getRelease() + " " + deviceInfo.getDisplay() + " " + randomId + " release-keys")
                        .replace("ro.build.fingerprint=" + fingerprint, "ro.build.fingerprint=" + deviceInfo.getBrand() + "/" + deviceInfo.getDevice() + "/" + deviceInfo.getDevice() + ":" + deviceInfo.getRelease() + "/" + deviceInfo.getDisplay() + "/" + randomId + ":user/release-keys");
                FileUtil.saveFile(hardwareInfo, "/sdcard/build.prop");
                //一定要还原权限 否则手机启动不了
                CMDUtil.execShell("cp /sdcard/build.prop /system/build.prop");
                SystemClock.sleep(1000);
                CMDUtil.execShell("chmod 0644 /system/build.prop");
                SystemClock.sleep(1000);
                CMDUtil.execShell("reboot");
            }
        });

    }

    /**
     * 替换手机硬件信息
     *
     * @param scrStr
     * @param property
     * @return
     */
    public static String getHardwareInfo(String scrStr, String property) {
        String descStr = scrStr.substring(scrStr.indexOf(property) + property.length());
        descStr = descStr.substring(0, descStr.indexOf("\n"));
        return descStr;
    }


}
