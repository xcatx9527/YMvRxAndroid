package com.xile.script.base.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenyang.lloglib.LLog;
import com.xile.script.adapter.PhotoWallAdapter;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.ItemTypeDialog;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.config.Constants;
import com.xile.script.http.common.HttpConstants;
import com.xile.script.http.helper.other.UploadFileHelper;
import com.yzy.example.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.MediaType;


/**
 * Created by Administrator on 2017/3/28.
 */

public class PictureWallActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, PhotoWallAdapter.PhtoWallClickCallBack {
    public static HashMap<Integer, Boolean> checkMap = new HashMap<>();  //传图checkbox选中状态集合
    public static boolean pic_flag;  //图片选中状态
    public static final int UPLOAD_PHOTO = 1;//上传图片
    public static final int DELETE_PHOTO = 2;//删除图片

    /**
     * 用于展示照片墙的GridView
     */
    private GridView mPhotoWall;

    /**
     * GridView的适配器
     */
    private PhotoWallAdapter mAdapter;
    private TextView tvTitle;

    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private ImageView btn_upload_pic;
    private CheckBox btn_check_all;
    private ImageView btn_delete_pic;
    private List<Object> list;
    private List<Object> uploadList;


    @Override
    protected void setView() {
        setContentView(R.layout.activity_picwall);
    }

    @Override
    protected void init() {
        tvTitle = findViewById(R.id.tl_title);
        tvTitle.setText("图片列表");
        btn_upload_pic = findViewById(R.id.btn_upload_pic);
        btn_check_all = findViewById(R.id.btn_check_all);
        btn_delete_pic = findViewById(R.id.btn_delete_pic);
        btn_upload_pic.setOnClickListener(this);
        btn_check_all.setOnCheckedChangeListener(this);
        btn_delete_pic.setOnClickListener(this);
        mImageThumbSize = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_spacing);
        mPhotoWall = findViewById(R.id.photo_wall);
        mAdapter = new PhotoWallAdapter(this,
                mPhotoWall, this);
        mPhotoWall.setAdapter(mAdapter);
        mPhotoWall.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        final int numColumns = (int) Math.floor(mPhotoWall
                                .getWidth()
                                / (mImageThumbSize + mImageThumbSpacing));
                        if (numColumns > 0) {
                            int columnWidth = (mPhotoWall.getWidth() / numColumns)
                                    - mImageThumbSpacing;
                            mAdapter.setItemHeight(columnWidth);
                            mPhotoWall.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                    }
                });

    }

    private void reappearInitData() {
        list.clear();
        checkMap.clear();
        getData();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_upload_pic:
                ItemTypeDialog itemTypeDialog = new ItemTypeDialog(this,
                        "管理后台",
                        "优化平台",
                        new ItemTypeDialog.OnInputMileageChanged() {
                            @Override
                            public void onConfirm1() {
                                Toast.makeText(getApplicationContext(), "正在上传图片...", Toast.LENGTH_SHORT).show();
                                dealPhoto(UPLOAD_PHOTO, list, HttpConstants.uploadManagerPicAndShellUrl);
                            }

                            @Override
                            public void onConfirm2() {
                                Toast.makeText(getApplicationContext(), "正在上传图片...", Toast.LENGTH_SHORT).show();
                                dealPhoto(UPLOAD_PHOTO, list, HttpConstants.uploadCenterControlPicAndShellUrl);
                            }

                            @Override
                            public void onConfirm3() {

                            }

                            @Override
                            public void onConfirm4() {

                            }

                            @Override
                            public void onConfirm5() {

                            }
                        });
                itemTypeDialog.show();


                break;
            case R.id.btn_delete_pic:
                Toast.makeText(this, "正在删除图片...", Toast.LENGTH_SHORT).show();
                dealPhoto(DELETE_PHOTO, list, "");
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出程序时结束所有的下载任务
    }


    @Override
    public void ItemClick(View v, Integer position) {
        pic_flag = false;
        RelativeLayout rl_pic_wall = v.findViewById(R.id.rl_pic_wall);
        ImageView img = (ImageView) rl_pic_wall.getChildAt(0);
        CheckBox xbox = (CheckBox) rl_pic_wall.getChildAt(1);
        xbox.setChecked(!xbox.isChecked());
        if (xbox.isChecked()) {
            checkMap.put(position, true);
        } else {
            checkMap.put(position, false);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        getData();
        Set<Integer> set = checkMap.keySet();
        Iterator<Integer> it = set.iterator();
        while (it.hasNext()) {
            Integer position = it.next();
            if (isChecked) {
                checkMap.put(position, true);
            } else {
                checkMap.put(position, false);
            }
        }
        if (isChecked) {
            pic_flag = false;
            mAdapter.setMap(checkMap);
        } else {
            mAdapter.setMap(new HashMap<>());
        }
        mAdapter.notifyDataSetChanged();
    }

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private List<File> dealfiles = new ArrayList<>(); // 用于需要处理图片的文件的集合

    public void getData() {
        list = new ArrayList<>();
        uploadList = new ArrayList<>();
        checkMap = new HashMap<>();
        List<Object> list = new ArrayList<>();

        // 得到sd卡内image文件夹的路径   File.separator(/)
//        String filePath = Environment.getExternalStorageDirectory().toString() + File.separator
//                + "Download" + File.separator + "img";//
        String filePath = Constants.SCRIPT_TAKE_SMALL_PHOTO_PATH;
        File dirFile = new File(filePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        } else {
            // 得到该路径文件夹下所有的文件
            File fileAll = new File(filePath);
            File[] files = fileAll.listFiles();
            if (files != null && files.length > 0) {
                // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (checkIsImageFile(file.getPath())) {
                        list.add(file.getPath());
                        PictureWallActivity.checkMap.put(i, false);
                    }
                }
            }
        }
        this.list = list;
        mAdapter.setObject(list);
        mAdapter.notifyDataSetChanged();
    }

    public void dealPhoto(int dealFalg, List<Object> list, String uploadUrl) {
        dealfiles.clear();
        PictureWallActivity.pic_flag = true;
        Iterator iter = PictureWallActivity.checkMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int posiont = (Integer) entry.getKey();
            Boolean val = (Boolean) entry.getValue();
            if (val) {  //是否被选中
                String url = (String) list.get(posiont); //图片的路径
                File file = new File(url); //对应的图片文件
                dealfiles.add(file);
            }
        }
        if (dealfiles.size() > 0 && dealFalg == PictureWallActivity.UPLOAD_PHOTO) {  //上传文件
            upLoadPicfiles(dealfiles, uploadUrl);
        } else if (dealfiles.size() > 0) { //删除文件
            for (int i = 0; i < dealfiles.size(); i++) {
                dealfiles.get(i).delete();
            }
        }
        //重新初始化数据
        reappearInitData();
    }

    /**
     * 上传图片
     *
     * @param dealfiles
     */
    private void upLoadPicfiles(List<File> dealfiles, String url) {
        RequestParams requestParams = new RequestParams();
        requestParams.addFormDataPartFiles("files", dealfiles);
        new UploadFileHelper().uploadFileListAction(ScriptApplication.getInstance(), url, requestParams, new UploadFileHelper.OnUploadFileListListener() {
            @Override
            public void onSuccess(Object o) {
                try {
                    JSONObject jsonObject = new JSONObject(o.toString());
                    int code = jsonObject.optInt("code");
                    String message = jsonObject.optString("message");
                    if (code == 200 && message.equals("ok")) {
                        LLog.e("图片上传成功!");
                        RecordFloatView.updateMessage("图片上传成功!");
                    } else {
                        LLog.e("图片上传失败!" + "code=" + code + ",message=" + message);
                        RecordFloatView.updateMessage("图片上传失败!" + "code=" + code + ",message=" + message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                LLog.e("图片上传失败!" + "errorCode=" + errorCode + ",msg=" + msg);
                RecordFloatView.updateMessage("图片上传失败!" + "errorCode=" + errorCode + ",msg=" + msg);
            }
        });
    }


    /**
     * 检查扩展名，得到图片格式的文件
     *
     * @param fName 文件名
     * @return
     */
    @SuppressLint("DefaultLocale")
    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1
        ).toLowerCase();
        isImageFile = FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg") || FileEnd.equals("bmp");
        return isImageFile;
    }
}
