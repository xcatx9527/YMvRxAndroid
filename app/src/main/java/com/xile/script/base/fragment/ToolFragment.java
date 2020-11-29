package com.xile.script.base.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xile.script.adapter.ToolAdapter;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.activity.AppInfoActivity;
import com.xile.script.base.activity.IMEInfoActivity;
import com.xile.script.base.activity.PictureWallActivity;
import com.xile.script.base.ui.promptdialog.PopupDialog;
import com.xile.script.base.ui.view.TitleLayout;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.bean.AccountInfoBean;
import com.xile.script.config.CollectEnum;
import com.xile.script.config.Constants;
import com.xile.script.service.FloatingService;
import com.xile.script.utils.AppUtil;
import com.xile.script.utils.appupdate.util.UpdateUtils;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.script.ScriptUtil;
import com.yzy.example.R;

import java.util.ArrayList;
import java.util.List;

/**
 * date: 2017/6/20 21:03
 */

public class ToolFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TitleLayout tl_title_layout;
    private ListView lv_tool_list;
    private List<AccountInfoBean> listData = null;
    private ToolAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tool, container, false);
        initView(view);
        initAccountData();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        tl_title_layout = view.findViewById(R.id.tl_title_layout);
        tl_title_layout.setTitle("工具");
        tl_title_layout.setLeftBtnVisiable(false);
        listData = new ArrayList<>();
        lv_tool_list = view.findViewById(R.id.lv_tool_list);
        mAdapter = new ToolAdapter(getActivity(), listData, this);
        lv_tool_list.setAdapter(mAdapter);
        lv_tool_list.setOnItemClickListener(this);
    }


    //初始化数据
    private void initAccountData() {
        listData.clear();
        AccountInfoBean bean = null;

        bean = new AccountInfoBean();
        bean.setViewType(AccountInfoBean.ACTION_VIEWTYPE_1);
        bean.setSectionTitle("查看应用包名");
        bean.setResId(R.drawable.findbundle_id);
        listData.add(bean);

        bean = new AccountInfoBean();
        bean.setViewType(AccountInfoBean.ACTION_VIEWTYPE_2);
        bean.setSectionTitle("查看输入法");
        bean.setResId(R.drawable.findbundle_id);
        listData.add(bean);

        bean = new AccountInfoBean();
        bean.setViewType(AccountInfoBean.ACTION_VIEWTYPE_3);
        bean.setSectionTitle("录图取图");
        bean.setResId(R.drawable.find_image);
        listData.add(bean);

        bean = new AccountInfoBean();
        bean.setViewType(AccountInfoBean.ACTION_VIEWTYPE_4);
        bean.setSectionTitle("上传图片");
        bean.setResId(R.drawable.upload_image);
        listData.add(bean);


        bean = new AccountInfoBean();
        bean.setViewType(AccountInfoBean.ACTION_VIEWTYPE_7);
        bean.setSectionTitle("卸载手机应用");
        bean.setResId(R.drawable.collect_role);
        listData.add(bean);

        bean = new AccountInfoBean();
        bean.setViewType(AccountInfoBean.ACTION_VIEWTYPE_9);
        try {
            bean.setSectionTitle("检查版本更新" + " (v" + getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName + ")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        bean.setResId(R.drawable.collect_role);
        listData.add(bean);


        bean = new AccountInfoBean();
        bean.setViewType(AccountInfoBean.ACTION_VIEWTYPE_10);
        bean.setSectionTitle("清除手机缓存");
        bean.setResId(R.drawable.upload_image);
        listData.add(bean);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            AppUtil.startActivity(getActivity(), AppInfoActivity.class);
        } else if (position == 1) {
            AppUtil.startActivity(getActivity(), IMEInfoActivity.class);
        } else if (position == 3) {
            Intent intent = new Intent(getActivity(), PictureWallActivity.class);
            getActivity().startActivity(intent);
        } else if (position == 2) {
            AppUtil.runToBackground(getActivity());
            getActivity().startService(new Intent(getActivity(), FloatingService.class));
            RecordFloatView.bigFloatState = RecordFloatView.COLLECT;
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Constants.COLLECT_STATE = CollectEnum.COLLECT_PHOTO;
            } else {
                Toast.makeText(getActivity(), "当前系统版本低于5.1无法进行取图功能", Toast.LENGTH_SHORT).show();
            }

        } else if (position == 3) { //卸载手机应用
            final PopupDialog dialog = new PopupDialog(ScriptApplication.getInstance(), 2, false);
            dialog.setTitle("是否要卸载手机应用?");
            dialog.setOneButtonText("确定");
            dialog.setTwoButtonText("取消");
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
            dialog.setOnPositiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ScriptApplication.getService().execute(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            final String operaDownload = "/storage/emulated/0/Download/";
                            FileHelper.deleteFolderFile(operaDownload);
                            ScriptUtil.dealWithUninstallOtherApp();
                            RecordFloatView.updateMessage("卸载手机应用成功!");
                        }
                    });
                }
            });
            dialog.setOnNegativeListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        } else if (position == 8) { //检查版本更新
            UpdateUtils.changeUpdateVersion(getActivity(), "script");
        } else if (position == 9) { //清除手机缓存
            final PopupDialog dialog = new PopupDialog(ScriptApplication.getInstance(), 2, false);
            dialog.setTitle("是否清除手机缓存");
            dialog.setOneButtonText("确定");
            dialog.setTwoButtonText("取消");
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
            dialog.setOnPositiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ScriptApplication.getService().execute(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            FileHelper.deletCacheFile();
                            FileHelper.deleteFileWithoutFolder(Constants.SCRIPT_FOLDER_PATH);
                            FileHelper.deleteFileWithoutFolder(Constants.SCRIPT_MATCH_PATH);
                            FileHelper.deleteFileWithoutFolder(Constants.SCRIPT_FOLDER_ROOT_POP_IMG_PATH);
                            RecordFloatView.updateMessage("清除缓存成功!");
                        }
                    });
                }
            });
            dialog.setOnNegativeListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        }

    }


}
