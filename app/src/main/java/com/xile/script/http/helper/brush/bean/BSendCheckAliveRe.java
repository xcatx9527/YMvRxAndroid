package com.xile.script.http.helper.brush.bean;

/**
 * date: 2017/7/6 14:52
 *
 * @scene 回应检测心跳
 */
public class BSendCheckAliveRe{
    private String name;
    private String type;
    private String phoneName;

    public BSendCheckAliveRe() {
    }

    public BSendCheckAliveRe(String name, String type, String phoneName) {
        this.name = name;
        this.type = type;
        this.phoneName = phoneName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    @Override
    public String toString() {
        return "BSendCheckAliveRe{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", phoneName='" + phoneName + '\'' +
                '}';
    }
}
