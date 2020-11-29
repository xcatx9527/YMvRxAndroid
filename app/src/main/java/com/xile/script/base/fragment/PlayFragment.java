package com.xile.script.base.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chenyang.lloglib.LLog;
import com.xile.script.adapter.PlayAdapter;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.ItemTypeDialog;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.bean.FileInfo;
import com.xile.script.config.Constants;
import com.xile.script.config.PlatformConfig;
import com.xile.script.http.common.HttpConstants;
import com.xile.script.http.helper.other.UploadFileHelper;
import com.xile.script.service.FloatingService;
import com.xile.script.utils.AppUtil;
import com.xile.script.utils.SortListUtil;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.script.ExecuteUtil;
import com.xile.script.utils.script.ReorganizeUitl;
import com.yzy.example.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayFragment extends Fragment implements View.OnClickListener {
    private PlayAdapter playAdapter;
    private ListView list_view;
    private ImageView img_sort_way;
    private LinearLayout zhuxian_server_script;
    private LinearLayout ashes_server_script;
    private List<FileInfo> fileInfoList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        list_view = view.findViewById(R.id.list_view);
        img_sort_way = view.findViewById(R.id.img_sort_way);
        ashes_server_script = view.findViewById(R.id.ashes_server_script);
        img_sort_way.setOnClickListener(this);
        ashes_server_script.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fileInfoList = new ArrayList<>();
        playAdapter = new PlayAdapter(getActivity());
        list_view.setAdapter(playAdapter);
        list_view.setOnItemClickListener((parent, view, position, id) -> {
            FileInfo fileInfo = playAdapter.getItem(position);
            Log.e("", "fileInfo:" + Constants.SCRIPT_FOLDER_PATH + fileInfo.getName());
            FileHelper.copyFile(Constants.SCRIPT_FOLDER_PATH + fileInfo.getName(), Constants.SCRIPT_FOLDER_TEMP_PATH);
            FileHelper.copyFile(Constants.SCRIPT_FOLDER_PATH + fileInfo.getName().substring(0, fileInfo.getName().length() - 4) + ".jpg", Constants.SCRIPT_FOLDER_TEMP_PNG_PATH);
            Constants.execServerScript = false;
            Constants.currentPlatform = "";
            AppUtil.runToBackground(getActivity());
            ScriptApplication.getService().execute(() -> {
                ExecuteUtil.execLocalScript(loopNum, Constants.SCRIPT_FOLDER_TEMP_PATH);
            });
        });

        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final FileInfo item = playAdapter.getItem(position);
                ItemTypeDialog itemTypeDialog = new ItemTypeDialog(getActivity(),
                        "管理后台",
                        "优化平台",
                        "删除脚本",
                        "转成竖屏",
                        "转成横屏",
                        new ItemTypeDialog.OnInputMileageChanged() {
                            @Override
                            public void onConfirm1() {  //上传管理后台
                                uploadScriptFile(item.getName(), HttpConstants.uploadManagerPicAndShellUrl);
                            }

                            @Override
                            public void onConfirm2() { //上传优化平台
                                uploadScriptFile(item.getName(), HttpConstants.uploadCenterControlPicAndShellUrl);
                            }

                            @Override
                            public void onConfirm3() { //删除脚本
                                FileHelper.deleteFile(Constants.SCRIPT_FOLDER_PATH + item.getName());
                                new Handler().postDelayed(PlayFragment.this::initAdapter,1000);
                            }

                            @Override
                            public void onConfirm4() { //转成竖屏
                                List<String> strings = FileHelper.readFile(Constants.SCRIPT_FOLDER_PATH + item.getName());
                                changeMothoed(strings,item.getName(),true);
                            }

                            @Override
                            public void onConfirm5() { //转成横屏
                                List<String> strings = FileHelper.readFile(Constants.SCRIPT_FOLDER_PATH + item.getName());
                                changeMothoed(strings,item.getName(),false);
                            }
                        });
                itemTypeDialog.show();

                return true;
            }
        });
    }

    /**
     * 上传指定路径的脚本文件
     *
     * @param name
     */
    private void uploadScriptFile(String name, String url) {
        File file = new File(Constants.SCRIPT_FOLDER_PATH + name);
        if (file.exists()) {
            new UploadFileHelper().uploadFileAction(getActivity(), url, file, new UploadFileHelper.OnUploadFileListener() {
                @Override
                public void onSuccess(Object o) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        int code = jsonObject.optInt("code");
                        String message = jsonObject.optString("message");
                        if (code == 200 && message.equals("ok")) {
                            LLog.e("脚本上传成功!");
                            RecordFloatView.updateMessage("脚本上传成功!");
                        } else {
                            LLog.e("脚本上传失败!" + "code=" + code + ",message=" + message);
                            RecordFloatView.updateMessage("脚本上传失败!" + "code=" + code + ",message=" + message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int errorCode, String msg) {
                    RecordFloatView.updateMessage("脚本上传失败!" + "errorCode=" + errorCode + ",msg=" + msg);
                }
            });
        } else {
            Toast.makeText(getActivity(), "脚本不存在!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        initAdapter();
    }

    public void initAdapter() {
        ScriptApplication.getService().execute(() -> {
            try {
                fileInfoList = FileHelper.getFileLastModified(Constants.SCRIPT_FOLDER_PATH);
                fileInfoList = (List<FileInfo>) SortListUtil.sort(fileInfoList, "time", SortListUtil.DESC);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (playAdapter != null) {
                        playAdapter.setObjects(fileInfoList);
                        playAdapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        List<FileInfo> fileInfoList = FileHelper.getFileLastModified(Constants.SCRIPT_FOLDER_PATH);

        switch (v.getId()) {

            case R.id.img_sort_way:
                if (img_sort_way.getDrawable().getCurrent().getConstantState() == getResources().getDrawable(R.drawable.date_sort).getConstantState()) {//从时间排序变成名称排序
                    //当ImageView的src属性为R.drawable.A时，设置ImageView的src属性为R.drawable.B
                    img_sort_way.setImageResource(R.drawable.name_sort);
                    fileInfoList = (List<FileInfo>) SortListUtil.sort(fileInfoList, "name", SortListUtil.ASC);
                    playAdapter.setObjects(fileInfoList);
                    playAdapter.notifyDataSetChanged();
                } else {
                    //否则设置ImageView的src属性为R.drawable.A
                    img_sort_way.setImageResource(R.drawable.date_sort);
                    fileInfoList = (List<FileInfo>) SortListUtil.sort(fileInfoList, "time", SortListUtil.DESC);
                    playAdapter.setObjects(fileInfoList);
                    playAdapter.notifyDataSetChanged();
                }
                break;

            case R.id.ashes_server_script://执行客服链接游戏优化自动脚本
                try {
                    Constants.currentPlatform = PlatformConfig.getPlatform(PlatformConfig.ASHES_PACKAGE_NAME);
                    RecordFloatView.bigFloatState = RecordFloatView.EXEC;
                    Constants.execServerScript = true;
                    AppUtil.runToBackground(getActivity());
                    getActivity().startService(new Intent(ScriptApplication.getInstance(), FloatingService.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    private int loopNum = 1;

    /**
     * 立即播放
     */
    public void playRightNow() {
        if (loopNum == -1) {
            return;
        } else if (loopNum == 0) {  //无限循环
            loopNum = Integer.MAX_VALUE;
        }
        ScriptApplication.getService().execute(() -> {
            ExecuteUtil.execLocalScript(loopNum, Constants.SCRIPT_FOLDER_TEMP_PATH);
        });
    }



    private void changeMothoed(List<String> strings, String name, boolean flag){
        if (strings!=null && strings.size()>0){
            String instruct = "";
            for (int i = 0;i<strings.size();i++){
                if (flag){
                    instruct = ReorganizeUitl.landscapeToPortraitInChainess(strings.get(i))+"\n";
                }else {
                    instruct = ReorganizeUitl.portraitToLandscapeInChainess(strings.get(i))+"\n";
                }
                FileHelper.addFileContent2(Constants.SCRIPT_FOLDER_PATH+(flag?("竖屏_"+name):("横屏_"+name)), TextUtils.isEmpty(instruct)?"\n":instruct);
            }
        }else {
            Toast.makeText(getContext(),"转换出错,此文件内容有问题,请查询后再转",Toast.LENGTH_LONG).show();
        }
        new Handler().postDelayed(PlayFragment.this::initAdapter,1000);
    }


}
