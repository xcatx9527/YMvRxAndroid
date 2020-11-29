package com.xile.script.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.chenyang.lloglib.LLog;
import com.google.gson.JsonSyntaxException;
import com.xile.script.base.ScriptApplication;
import com.xile.script.config.Constants;
import com.xile.script.http.helper.brush.bean.BSendKeychainFile;
import com.xile.script.http.helper.brush.mina.BrushOrderHelper;
import com.xile.script.utils.common.FileHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * date: 2018/6/25 14:41
 *
 * @scene 上传Keychain的服务
 */
public class UploadKeychainService extends Service {
    public static boolean isUploading = false;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("UploadKeychainService.onCreate");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (isUploading) return;
                    isUploading = true;
                    //LLog.i("上传keychain服务正在运行中...");
                    //LLog.i("上传keychain 111111111111111");
                    String rootPath = Constants.SCRIPT_FOLDER_GAME_KEYCHAIN_UPLOAD_PATH;
                    File rootDir = new File(rootPath);
                    if (rootDir.exists() && rootDir.isDirectory()) {
                        File[] userIds = rootDir.listFiles();
                        if (userIds != null && userIds.length > 0) {
                            File firstUserIdDir = userIds[new Random().nextInt(userIds.length)];
                            File[] keychains = firstUserIdDir.listFiles();
                            if (keychains != null && keychains.length > 0) {
                                for (int j = 0; j < keychains.length; j++) {
                                    if (keychains[j].getName().endsWith(".cfg")) {
                                        try {
                                            //LLog.i("上传keychain 444444444444444");
                                            BSendKeychainFile bSendKeychainFile = ScriptApplication.getGson().fromJson(FileHelper.getFileString(keychains[j].getAbsolutePath()), BSendKeychainFile.class);
                                            if (bSendKeychainFile == null) {
                                                LLog.e("上传keychain BSendKeychainFile 为空!");
                                                continue;
                                            } else {
                                                //LLog.i("上传keychain 555555555555555");
                                                JSONObject jsonObject = new JSONObject();
                                                jsonObject.put("name", bSendKeychainFile.getName());
                                                jsonObject.put("type", bSendKeychainFile.getType());
                                                JSONObject data = new JSONObject();
                                                data.put("keychainName", bSendKeychainFile.getKeychainName());
                                                data.put("keychainData", bSendKeychainFile.getKeychainData());
                                                data.put("userId", bSendKeychainFile.getUserId());
                                                data.put("timestamp", bSendKeychainFile.getTimestamp());
                                                jsonObject.put("data", data.toString());
                                                BrushOrderHelper.getInstance().uploadKeychain(keychains[j].getAbsolutePath(), bSendKeychainFile.getUserId(), jsonObject);
                                                break;
                                            }
                                        } catch (JsonSyntaxException e) {
                                            e.printStackTrace();
                                            isUploading = false;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            isUploading = false;
                                        }
                                    }
                                    //LLog.i("上传keychain 3333333333333333");
                                    continue;//不包含配置但包含keychain的情况
                                }
                            } else {
                                //LLog.i("上传keychain 22222222222222222");
                                isUploading = false;
                                if ((System.currentTimeMillis() - firstUserIdDir.lastModified()) > 10 * 60 * 1000) {
                                    LLog.e(firstUserIdDir.getName() + " 空文件夹，删除!");
                                    FileHelper.deleteFolderFile(firstUserIdDir.getAbsolutePath());
                                }
                            }
                        } else {
                            isUploading = false;
                            //LLog.i("暂无可传的keychain文件");
                        }
                    } else {
                        isUploading = false;
                        //LLog.e("根目录 " + Constants.SCRIPT_FOLDER_GAME_KEYCHAIN_UPLOAD_PATH + " 不存在");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isUploading = false;
                }
            }
        }, 30000, 10000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("UploadKeychainService.onStartCommand");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("UploadKeychainService.onDestroy");
    }

}
