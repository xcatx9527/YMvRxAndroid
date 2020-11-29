package com.xile.script.utils.script;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.chenyang.lloglib.LLog;
import com.xile.script.base.ScriptApplication;
import com.xile.script.base.ui.view.floatview.RecordFloatView;
import com.xile.script.bean.CommentInfo;
import com.xile.script.config.Constants;
import com.xile.script.config.GameConfig;
import com.xile.script.config.PlatformConfig;
import com.xile.script.config.PlayEnum;
import com.xile.script.http.helper.brush.mina.BrushOrderHelper;
import com.xile.script.service.FloatingService;
import com.xile.script.utils.common.FileHelper;
import com.xile.script.utils.common.SpUtil;
import com.yzy.example.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.xile.script.config.Constants.SCRIPT_FOLDER_LOCAL_COMMENT_PATH;


/**
 * date: 2017/5/6 21:30
 *
 * @scene 执行工具类
 */
public class ExecuteUtil {


    /**
     * 执行本地脚本
     *
     * @param count    执行的次数
     * @param execPath 脚本路径
     */

    public static void execLocalScript(final int count, String execPath) {
        RecordFloatView.bigFloatState = RecordFloatView.PLAY;
        Constants.PLAY_STATE = PlayEnum.START_PLAY;
        LLog.d("加载local_popup图片");
        PopupUtil.clear();
        GameConfig.clear();
        SpUtil.putKeyBoolean(PlatformConfig.NEED_KILL_APP, false);
        initLocalPicNames(Constants.SCRIPT_FOLDER_ROOT_POP_LOCAL_IMG_PATH);
        initPicList(Constants.SCRIPT_FOLDER_POP_LOCAL_IMG_PATH);
        initPicList(Constants.SCRIPT_FOLDER_ALERT_LOCAL_IMG_PATH);
        initPicList(Constants.SCRIPT_FOLDER_PLATFORM_LOCAL_IMG_PATH);
        initPicList(Constants.SCRIPT_FOLDER_COMPARE_LOCAL_IMG_PATH);
        initPicList(Constants.SCRIPT_FOLDER_SAMPLE_LOCAL_IMG_PATH);
        initPicList(Constants.SCRIPT_FOLDER_MODULE_LOCAL_IMG_PATH);
        initLocalComments(Constants.SCRIPT_FOLDER_LOCAL_COMMENT_PATH);
        RecordFloatView.updateMessage(ScriptApplication.getInstance().getResources().getString(R.string.text_begin_play));
        ScriptApplication.getInstance().startService(new Intent(ScriptApplication.getInstance(), FloatingService.class));
        final List<String> scriptFile = FileHelper.readFile(execPath);
        if (scriptFile != null && scriptFile.size() > 0) {
            SocketUtil.initInstruct(scriptFile, count);
        }
    }


    /**
     * 执行服务器脚本
     *
     * @param count      执行的次数
     * @param scriptList 脚本
     */

    public static void execServerScript(final int count, List<String> scriptList) {
       ScriptApplication.getService().execute(()->{
           RecordFloatView.bigFloatState = RecordFloatView.EXEC;
           Constants.PLAY_STATE = PlayEnum.START_PLAY;
           LLog.e("加载popup图片");
           GameConfig.clear();
           initPicList(Constants.SCRIPT_FOLDER_ROOT_POP_IMG_PATH);
           RecordFloatView.updateMessage(ScriptApplication.getInstance().getResources().getString(R.string.text_begin_play));
           ScriptApplication.getInstance().startService(new Intent(ScriptApplication.getInstance(), FloatingService.class));
           if (scriptList != null && scriptList.size() > 0) {
               SocketUtil.initInstruct(scriptList, count);
           }
       });
    }


    /**
     * 初始化弹窗图片
     *
     * @param imgPath 路径   popup或local_popup
     */
    private static void initPicList(final String imgPath) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(imgPath)) {
                    ScriptUtil.initPopupImgs(imgPath);
                    LLog.e("图片数量:" + PopupUtil.getPopupImgsList().size() + "");
                }
            }
        });
    }


    /**
     * 初始化本地弹窗图片名字
     *
     * @param imgPath 路径
     */
    private static void initLocalPicNames(final String imgPath) {
        if (!TextUtils.isEmpty(imgPath)) {
            File dir = new File(imgPath);
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                for (File f : files) {
                    if (f.isDirectory()) {
                        initLocalPicNames(f.getAbsolutePath());
                    } else {
                        if (dir.getName().equals("popup")) {
                            PopupUtil.getPopupNameList().add(f.getName());
                        } else if (dir.getName().equals("alert")) {
                            PopupUtil.getAlertNameList().add(f.getName());
                        } else if (dir.getName().equals("platform")) {
                            PopupUtil.getPlatformNameList().add(f.getName());
                        } else if (dir.getName().equals("compare")) {
                            PopupUtil.getCompareNameList().add(f.getName());
                        } else if (dir.getName().equals("sample")) {
                            PopupUtil.getSampleNameList().add(f.getName());
                        } else if (dir.getName().equals("module")) {
                            PopupUtil.getModuleNameList().add(f.getName());
                        }
                    }
                }
            }
        }
    }


    /**
     * 初始化本地话库
     *
     * @param localCommentPath 路径
     */
    private static void initLocalComments(String localCommentPath) {
        BrushOrderHelper.getInstance().TvComments = new ArrayList<>();
        if (TextUtils.isEmpty(localCommentPath)) {
            return;
        }
        File localCommentDir = new File(localCommentPath);
        if (localCommentDir.exists() && localCommentDir.isDirectory()) {
            File[] commentFiles = localCommentDir.listFiles();
            if (commentFiles.length <= 0) {
                return;
            }
            for (File commentFile : commentFiles) {
                List<String> comments = FileHelper.readFile(SCRIPT_FOLDER_LOCAL_COMMENT_PATH + commentFile.getName());
                if (comments != null && comments.size() > 0) {
                    CommentInfo commentInfo = new CommentInfo(commentFile.getName(), comments, 0);
                    BrushOrderHelper.getInstance().TvComments.add(commentInfo);
                }
            }
        }


    }


}
