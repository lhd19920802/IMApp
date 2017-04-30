package com.atguigu.imapp.controller.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.atguigu.imapp.controller.domain.ContactTab;
import com.atguigu.imapp.controller.domain.InviteTable;

/**
 * Created by acer on 2017/3/23 20:55.
 * 作用：联系人和邀请信息的数据库
 */
public class DBHelper extends SQLiteOpenHelper
{
    public DBHelper(Context context, String name)
    {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(ContactTab.CREATE_TAB);
        db.execSQL(InviteTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
