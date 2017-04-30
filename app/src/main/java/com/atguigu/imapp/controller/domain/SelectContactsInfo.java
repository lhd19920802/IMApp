package com.atguigu.imapp.controller.domain;

/**
 * Created by acer on 2017/3/27 19:22.
 * 作用：
 */
public class SelectContactsInfo
{
    private UserInfo user;      // 联系人
    private boolean isChecked;  // 是否被选择的标记

    public SelectContactsInfo()
    {
    }

    public SelectContactsInfo(UserInfo user, boolean isChecked)
    {
        this.user = user;
        this.isChecked = isChecked;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
