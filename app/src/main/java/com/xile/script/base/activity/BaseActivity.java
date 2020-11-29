package com.xile.script.base.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.xile.script.base.ui.promptdialog.PopupDialog;
import com.xile.script.permission.PermissionInfo;
import com.xile.script.permission.PermissionUtil;
import com.xile.script.permission.callback.PermissionOriginResultCallBack;
import com.xile.script.utils.script.SocketUtil;

import java.util.List;


public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
    protected InputMethodManager inputMethodManager;
    public PopupDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setView();
        init();
        getData();
        if (SocketUtil.server == null) {
            SocketUtil.flag = true;
            SocketUtil.receiveInstruct();//启动服务器socket
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                new PermissionUtil().request(BaseActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_FORMAT_FILESYSTEMS, Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS, Manifest.permission.CAMERA}, new PermissionOriginResultCallBack() {

                    @Override
                    public void onResult(List<PermissionInfo> acceptList, List<PermissionInfo> rationalList, List<PermissionInfo> deniedList) {
                        if (!acceptList.isEmpty()) {
                            Toast.makeText(BaseActivity.this, acceptList.get(0).getName() + " is accepted", Toast.LENGTH_SHORT).show();
                        }
                        if (!rationalList.isEmpty()) {
                            Toast.makeText(BaseActivity.this, rationalList.get(0).getName() + " is rational", Toast.LENGTH_SHORT).show();
                        }
                        if (!deniedList.isEmpty()) {
                            Toast.makeText(BaseActivity.this, deniedList.get(0).getName() + " is denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        dialog = new PopupDialog(BaseActivity.this, 1, false);
    }

    public void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                if (getCurrentFocus().getWindowToken() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //检查是否已经授予悬浮窗权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this) && !dialog.isShowing()) {
                dialog.setTitle("请赋予应用悬浮窗权限");
                dialog.setOnPositiveListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 0);
                    }
                });
                dialog.show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    /**
     * 设置布局
     */
    protected void setView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 加载数据
     */
    protected void getData() {
    }

    /**
     * 控件初始化
     */
    protected void init() {
    }

    /**
     * 点击事件的处理
     */
    public void onClick(View v) {
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            hideSoftKeyboard();
        }
        return super.onTouchEvent(event);
    }


}
