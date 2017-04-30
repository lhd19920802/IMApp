package com.atguigu.imapp;

import android.app.Application;
import android.content.Context;

import com.atguigu.imapp.modle.Modle;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

/**
 * Created by acer on 2017/3/21 20:49.
 * 作用：初始化EaseUI
 */
public class IMApplication extends Application
{
    private static Context mContext;
    @Override
    public void onCreate()
    {
        super.onCreate();

        EMOptions options=new EMOptions();
        options.setAcceptInvitationAlways(false);
        options.setAutoAcceptGroupInvitation(false);

        //初始化EaseUI
        EaseUI.getInstance().init(this, options);

        //初始化数据模型层
        Modle.getInstance().init(this);

        //初始化全局上下文
        mContext=this;
    }

    // 获取全局上下文对象
    public static Context getGlobalApplication(){
        return mContext;
    }
}
