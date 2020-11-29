package com.xile.script.bean;

import java.io.Serializable;

/**
 * 作者：赵小飞<br>
 * 时间 2017/3/1.
 */

public class ScriptInfo implements Serializable {
    private long scriptTime;//脚本时间
    private String scriptStr;//具体脚本

    public ScriptInfo(long scriptTime, String scriptStr) {
        this.scriptTime = scriptTime;
        this.scriptStr = scriptStr;
    }

    public ScriptInfo() {

    }

    public ScriptInfo(String scriptStr) {
        this.scriptStr = scriptStr;
    }

    @Override
    public String toString() {
        return "ScriptInfo{" +
                "scriptTime=" + scriptTime +
                ", scriptStr='" + scriptStr + '\'' +
                '}';
    }

    public long getScriptTime() {
        return scriptTime;
    }

    public void setScriptTime(long scriptTime) {
        this.scriptTime = scriptTime;
    }

    public String getScriptStr() {
        return scriptStr;
    }

    public void setScriptStr(String scriptStr) {
        this.scriptStr = scriptStr;
    }


}
