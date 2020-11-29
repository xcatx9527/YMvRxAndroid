package com.xile.script.http.helper.manager.bean;

/**
 * date: 2017/7/6 14:52
 *
 * @scene 收到检测心跳
 */
public class ReceiveCheckAlive {
    private String robotName;

    public ReceiveCheckAlive() {
    }

    public ReceiveCheckAlive(String robotName) {
        this.robotName = robotName;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    @Override
    public String toString() {
        return "ReceiveCheckAlive{" +
                "robotName='" + robotName + '\'' +
                '}';
    }
}
