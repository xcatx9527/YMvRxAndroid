package com.xile.script.bean;

import java.util.List;

/**
 * date: 2017/8/14 19:07
 *
 * @scene 话库实体类
 */
public class CommentInfo {
    private String name;//话库名字
    private List<String> comments;//话库内容
    private int commentNum;//顺序时当前第几条评论数

    public CommentInfo() {
    }

    public CommentInfo(String name, List<String> comments, int commentNum) {
        this.name = name;
        this.comments = comments;
        this.commentNum = commentNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getComments() {
        return comments;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "CommentInfo{" +
                "name='" + name + '\'' +
                ", comments=" + comments +
                ", commentNum=" + commentNum +
                '}';
    }
}
