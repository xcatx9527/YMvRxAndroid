package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/7/6 14:52
 *
 * @scene 收到检测心跳
 */
public class BReceiveCheckAlive {
    private String phoneName;

    public BReceiveCheckAlive() {
    }

    public BReceiveCheckAlive(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    @Override
    public String toString() {
        return "BReceiveCheckAlive{" +
                "phoneName='" + phoneName + '\'' +
                '}';
    }
}
