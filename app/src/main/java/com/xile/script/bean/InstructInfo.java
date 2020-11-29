package com.xile.script.bean;

import java.io.Serializable;

/**
 * 作者：赵小飞<br>
 * 时间 2017/2/25.
 */

public class InstructInfo implements Serializable{

    private String touchId;
    private String instructId;
    private String type;
    private String code;
    private String value;
    private long time;

    public InstructInfo(long time,String code, String value) {
        this.time = time;
        this.value = value;
        this.code = code;
    }

    @Override
    public String toString() {
        return "InstructInfo{" +
                "touchId='" + touchId + '\'' +
                ", instructId='" + instructId + '\'' +
                ", type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", value='" + value + '\'' +
                ", time=" + time +
                '}';
    }

    public InstructInfo(String touchId, String instructId, String type, String code, String value, long time) {
        this.touchId = touchId;
        this.instructId = instructId;
        this.type = type;
        this.code = code;
        this.value = value;
        this.time = time;
    }

    public String getInstructId() {
        return instructId;
    }

    public void setInstructId(String instructId) {
        this.instructId = instructId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTouchId() {
        return touchId;
    }

    public void setTouchId(String touchId) {
        this.touchId = touchId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
