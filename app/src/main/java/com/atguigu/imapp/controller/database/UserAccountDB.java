package com.atguigu.imapp.controller.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.atguigu.imapp.controller.domain.UserAccountTab;

/**
 * Created by acer on 2017/3/22 9:41.
 * 作用：创建用户账号数据库
 */
public class UserAccountDB extends SQLiteOpenHelper
{

    public UserAccountDB(Context context)
    {
        super(context, "account.db", null, 1);
    }

    /**
     * 在这里面创建表
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(UserAccountTab.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
