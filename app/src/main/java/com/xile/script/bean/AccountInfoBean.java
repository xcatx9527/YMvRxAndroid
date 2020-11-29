package com.xile.script.bean;


/**
 * account info
 * Created by chsh on 3/28/17.
 */

public class AccountInfoBean {
    private int viewType;  //条目类型
    private int ResId;     //条目图标id
    private String SectionTitle; //条目标题内容

    public static final int ACTION_VIEWTYPE_0 = 0;
    public static final int ACTION_VIEWTYPE_1 = 1;
    public static final int ACTION_VIEWTYPE_2 = 2;
    public static final int ACTION_VIEWTYPE_3 = 3;
    public static final int ACTION_VIEWTYPE_4 = 4;
    public static final int ACTION_VIEWTYPE_5 = 5;
    public static final int ACTION_VIEWTYPE_6 = 6;
    public static final int ACTION_VIEWTYPE_7 = 7;
    public static final int ACTION_VIEWTYPE_8 = 8;
    public static final int ACTION_VIEWTYPE_9 = 9;
    public static final int ACTION_VIEWTYPE_10 = 10;
    public static final int ACTION_VIEWTYPE_11 = 11;



    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }


    public int getResId() {
        return ResId;
    }

    public void setResId(int resId) {
        ResId = resId;
    }

    public String getSectionTitle() {
        return SectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        SectionTitle = sectionTitle;
    }

    public AccountInfoBean() {

    }


}
