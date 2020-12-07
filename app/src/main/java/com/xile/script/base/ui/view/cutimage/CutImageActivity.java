package com.xile.script.base.ui.view.cutimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.activity.BaseActivity;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.config.Constants;
import com.xile.script.utils.BitmapUtil;
import com.yzy.example.R;

import java.io.File;

/**
 * date: 2017/6/20 21:03
 */
public class CutImageActivity extends BaseActivity  {
    private CilpImageLayout layout;
    MaterialButton sure_image;
    MaterialButton cancel_image;
    ImageView expand_image;
    ImageView shrink_image;
    boolean isNeedRotate=false;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cilp_image);
        sure_image= findViewById(R.id.sure_image);
        cancel_image= findViewById(R.id.cancel_image);
        expand_image= findViewById(R.id.expand_image);
        shrink_image= findViewById(R.id.shrink_image);
        layout = findViewById(R.id.cilpimage_layout);
        File mFile=new File(Constants.SCRIPT_FOLDER_TAKE_TEMP_PNG_PATH);
        //若该文件存在
        if (mFile.exists()) {
            bitmap=BitmapFactory.decodeFile(Constants.SCRIPT_FOLDER_TAKE_TEMP_PNG_PATH);
            //   此处因为横竖屏问题有bug,解决——将manifest中强制设置为竖屏
            isNeedRotate= BitmapUtil.needRotate(bitmap);
            if (isNeedRotate) {
                bitmap = BitmapUtil.rotateBitmap(bitmap, 90);
           }
            layout.setImageBitmap(bitmap);
        }else{
            finish();
        }
        sure_image.setOnClickListener(this);
        cancel_image.setOnClickListener(this);
        expand_image.setOnClickListener(this);
        shrink_image.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.sure_image://这边需要弹出对话框保存输入图片名进行保存图片
                RecordFloatView.updateMessage(ScriptApplication.getInstance().getResources().getString(R.string.text_capture_done));
                ScriptApplication.bitmapTemp=layout.cilpBitmap();
                Constants.needSave = true;
                finish();
                break;
            case R.id.cancel_image:
                RecordFloatView.updateMessage(ScriptApplication.getInstance().getResources().getString(R.string.text_capture_cancel));
                finish();
                break;
            case R.id.expand_image:
                layout.setExpandImage();
                break;
            case R.id.shrink_image:
                layout.setShrinkImage();
                break;
        }
    }
}

