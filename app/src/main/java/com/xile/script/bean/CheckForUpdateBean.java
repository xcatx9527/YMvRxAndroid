package com.xile.script.bean;

/**
 * Created by chsh on 2018/1/2.
 */

public class CheckForUpdateBean {

    //{"changelog":"haha","code":0,"installUrl":"http://118.145.5.141:4080/shell/gameApk/gameapkfile.apk","version":"1"}
    //{"changelog":"","version":"2","installUrl":"url"}
    private String changelog;   //更新的日志
    private String version;     //更新的版本号
    private String installUrl;  //新版本的链接
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(String installUrl) {
        this.code = code;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
