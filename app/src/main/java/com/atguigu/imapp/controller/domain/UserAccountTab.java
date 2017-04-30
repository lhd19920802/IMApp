package com.atguigu.imapp.controller.domain;

/**
 * Created by acer on 2017/3/22 9:44.
 * 作用：创建表的类
 */
public class UserAccountTab
{
    public static final String TAB_NAME = "tab_account";//表名
    public static final String COL_NAME = "name";
    public static final String COL_HXID = "hxid";
    public static final String COL_NICK = "nick";
    public static final String COL_PHOTO = "photo";

    public static final String CREATE_TAB = "create table " + TAB_NAME + "(" + COL_HXID + " text "
                                            + "" + "" + "primary key," + COL_NAME + " text," +
                                            COL_NICK + " text," + COL_PHOTO + " text);";


}


