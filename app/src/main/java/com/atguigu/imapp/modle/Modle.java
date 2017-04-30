package com.atguigu.imapp.modle;

import android.content.Context;

import com.atguigu.imapp.controller.dao.UserAccountDao;
import com.atguigu.imapp.controller.database.DBManager;
import com.atguigu.imapp.controller.domain.UserInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by acer on 2017/3/21 21:29.
 * 作用：数据模型层全局类
 */
public class Modle
{
    private static Modle modle = new Modle();
    private Context mContext;
    //创建线程池
    private ExecutorService executors = Executors.newCachedThreadPool();
    //数据库操作对象
    private UserAccountDao userAccountDao;
    private DBManager dbManager;

    private Modle()
    {

    }

    public static Modle getInstance()
    {
        return modle;
    }

    public void init(Context context)
    {
        mContext = context;
        //得到用户账号的操作dao
        userAccountDao = new UserAccountDao(context);
        //创建全局监听
        EventListener eventListener = new EventListener(mContext);

    }

    public ExecutorService getExecutors()
    {
        return executors;
    }

    //得到数据库操作对象
    public UserAccountDao getAccountDao()
    {

        return userAccountDao;
    }

    //用户登录成功后的处理方法
    public void loginSuccess(UserInfo account)
    {
        if(account==null) {
            return;
        }
        if(dbManager!=null) {
            dbManager.close();
        }
        dbManager = new DBManager(mContext, account.getName());
    }

    public DBManager getDBManager()
    {

        return dbManager;
    }
}

