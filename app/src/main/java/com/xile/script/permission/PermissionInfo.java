package com.xile.script.permission;

/**
 * 作者：赵小飞<br>
 * 时间 2017/3/21.
 */
public class PermissionInfo {
    private String mName;
    private String mShortName;

    public PermissionInfo(String name) {
        this.mName = name;
        this.mShortName = name.substring(name.lastIndexOf(".") + 1);
    }

    public String getName() {
        return mName;
    }


    public void setName(String mName) {
        this.mName = mName;
    }

    public String getShortName() {
        return mShortName;
    }

}
