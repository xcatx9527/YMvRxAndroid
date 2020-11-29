package com.xile.script.bean;

import java.io.Serializable;

/**
 * 作者：赵小飞<br>
 * 时间 2017/3/20.
 */

public class FileInfo implements Serializable {
        private String name;
        private long time;

    public FileInfo(String name, long time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "name='" + name + '\'' +
                ", time=" + time +
                '}';
    }
}
